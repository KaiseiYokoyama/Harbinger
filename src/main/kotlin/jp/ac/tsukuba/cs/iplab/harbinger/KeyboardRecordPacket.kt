package jp.ac.tsukuba.cs.iplab.harbinger

import jp.ac.tsukuba.cs.iplab.catalyst.client.KeyboardRecord
import jp.ac.tsukuba.cs.iplab.harbinger.prelude.KeyboardEventRecorder
import jp.ac.tsukuba.cs.iplab.harbinger.prelude.asRecord
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.minecraft.network.PacketBuffer
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.network.NetworkEvent
import net.minecraftforge.fml.network.NetworkRegistry
import java.util.function.Supplier

const val PROTOCOL_VERSION = "1"

/**
 * クライアントからサーバへ送信するパケット
 * キーボード操作のイベントが入っている
 */
class KeyboardRecordPacket(
    val record: KeyboardRecord
) {
    companion object {
        val channel = NetworkRegistry.newSimpleChannel(
            ResourceLocation(MOD_NAME, "packet"),
            { PROTOCOL_VERSION },
            { it.equals(PROTOCOL_VERSION) },
            { it.equals(PROTOCOL_VERSION) },
        )

        val parser = Json {
            serializersModule = KeyboardRecord.serializersModule
        }

        fun decode(buf: PacketBuffer): KeyboardRecordPacket {
            val recordJson = buf.readString(Int.MAX_VALUE / 5)
            val record = parser.decodeFromString<KeyboardRecord>(recordJson)

            return KeyboardRecordPacket(record)
        }

        fun handle(packet: KeyboardRecordPacket, context: Supplier<NetworkEvent.Context>) {
            context.get().enqueueWork {
                val record = packet.record
                record.player = context.get().sender!!.asRecord()

                // 受け取ったイベントをサーバのイベントチャンネルに流す
                MinecraftForge.EVENT_BUS.post(KeyboardEventRecorder.LocalKeyboardEvent(record))
            }
            context.get().packetHandled = true
        }
    }

    fun encode(buf: PacketBuffer) {
        buf.writeString(
            parser.encodeToString(record)
        )
    }
}