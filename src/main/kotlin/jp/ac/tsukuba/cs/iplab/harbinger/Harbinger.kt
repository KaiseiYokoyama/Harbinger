package jp.ac.tsukuba.cs.iplab.harbinger

import jp.ac.tsukuba.cs.iplab.catalyst.db.DBWriter
import jp.ac.tsukuba.cs.iplab.catalyst.db.DBWriter.ExpConfig
import jp.ac.tsukuba.cs.iplab.harbinger.client.KeyboardEventForwarder
import jp.ac.tsukuba.cs.iplab.harbinger.prelude.EventRecorder
import jp.ac.tsukuba.cs.iplab.harbinger.prelude.KeyboardEventRecorder
import jp.ac.tsukuba.cs.iplab.harbinger.prelude.StatusRecorder
import jp.ac.tsukuba.cs.iplab.harbinger.prelude.WorldSaver
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.server.FMLServerStartingEvent
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent
import org.apache.logging.log4j.LogManager
import kotlin.io.path.createFile
import kotlin.io.path.writeText


const val MOD_NAME = "harbinger"

fun initChannel() {
    KeyboardRecordPacket.channel.registerMessage(
        0,
        KeyboardRecordPacket::class.java,
        KeyboardRecordPacket::encode,
        KeyboardRecordPacket.Companion::decode,
        KeyboardRecordPacket.Companion::handle,
    )
}

@Mod(MOD_NAME)
@OnlyIn(Dist.CLIENT)
class Client {
    companion object {
        val LOGGER = LogManager.getLogger()
    }

    init {
        initChannel()

        // クライアントでしか発生しない=サーバでは発生しないイベントを転送する
        // 例：テキスト入力関係のイベントを記録するレコーダをセットアップ
        val keyboardEventForwarder = KeyboardEventForwarder()
        MinecraftForge.EVENT_BUS.register(keyboardEventForwarder)
    }

//    @SubscribeEvent
//    fun onSetup(event: NetworkEvent.) {
//    }
}

@Mod(MOD_NAME)
@OnlyIn(Dist.DEDICATED_SERVER)
class Server {
    val prelude = Prelude()

    init {
        initChannel()
    }

    class Prelude {
        companion object {
            val LOGGER = LogManager.getLogger("prelude")
        }

        lateinit var dbWriter: DBWriter

        init {
            LOGGER.info("Prelude initialized")
            // Register ourselves for server and other game events we are interested in
            MinecraftForge.EVENT_BUS.register(this)
        }

        // You can use SubscribeEvent and let the Event Bus discover methods to call
        @SubscribeEvent
        fun onServerStarting(event: FMLServerStartingEvent) {
            val configFile = java.nio.file.Paths.get("harbinger.json")
            dbWriter = try {
                DBWriter(configFile).also {
                    it.recordExpConfig(it.expConfig)
                }
            } catch (e: java.nio.file.NoSuchFileException) {
                configFile.createFile().writeText(ExpConfig.EMPTY_JSON)
                LOGGER.error("`harbinger.json` was NOT found and created. Write down the config on it and restart server.")
                throw e
            } catch (e: DBWriter.ExpIdDuplicationException) {
                LOGGER.error("`_id` is duplicated in `harbinger.json`. Please specify new `_id`.")
                throw e
            }

            // ステータスの記録
            MinecraftForge.EVENT_BUS.register(StatusRecorder(dbWriter))

            // イベントの記録
            MinecraftForge.EVENT_BUS.register(EventRecorder(dbWriter))

            // キーボード操作の記録（クライアントから転送されてくるイベント）
            MinecraftForge.EVENT_BUS.register(KeyboardEventRecorder(dbWriter))
        }

        @SubscribeEvent
        fun onServerStopping(event: FMLServerStoppedEvent) {
            // worldフォルダをzip化し、MongoDBにアップロードする
            WorldSaver(dbWriter).save()
        }
    }
}
