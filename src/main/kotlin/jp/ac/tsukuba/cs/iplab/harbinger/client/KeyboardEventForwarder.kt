package jp.ac.tsukuba.cs.iplab.harbinger.client

import jp.ac.tsukuba.cs.iplab.catalyst.client.KeyboardRecord
import jp.ac.tsukuba.cs.iplab.harbinger.Client
import jp.ac.tsukuba.cs.iplab.harbinger.KeyboardRecordPacket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import net.minecraftforge.eventbus.api.SubscribeEvent

/**
 * キーボード操作に関するイベントをキャッチし、サーバへ転送する
 */
class KeyboardEventForwarder {
    // バッファサイズ100のチャンネルを作成
    private val tx = Channel<KeyboardRecord>(100)

    private val worker = GlobalScope.launch(Dispatchers.IO) {
        while (true) {
            val record = tx.receive()
            send(record)
        }
    }

    private fun send(record: KeyboardRecord) {
        Client.LOGGER.debug("Send: $record")
        KeyboardRecordPacket.channel.sendToServer(KeyboardRecordPacket(record))
    }
    
    @SubscribeEvent
    fun <T : KeyboardRecord> record(record: KeyboardEvent<T>) {
        tx.trySend(record.record)
    }
}