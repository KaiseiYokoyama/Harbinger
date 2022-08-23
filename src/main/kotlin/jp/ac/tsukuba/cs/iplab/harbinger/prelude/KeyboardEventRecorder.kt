package jp.ac.tsukuba.cs.iplab.harbinger.prelude

import jp.ac.tsukuba.cs.iplab.harbinger.KeyboardRecordPacket
import jp.ac.tsukuba.cs.iplab.catalyst.client.KeyboardRecord
import jp.ac.tsukuba.cs.iplab.catalyst.db.DBWriter
import jp.ac.tsukuba.cs.iplab.harbinger.Server as Harbinger
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.eventbus.api.Event
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.network.NetworkEvent
import java.util.function.Supplier

class KeyboardEventRecorder(
    val dbWriter: DBWriter
) {
    companion object {
        fun receive(packet: KeyboardRecordPacket, context: Supplier<NetworkEvent.Context>) {
            Harbinger.Prelude.LOGGER.info("Packet:${packet.record}")
            context.get().enqueueWork {
                val record = packet.record
                record.player = context.get().sender!!.asRecord()

                MinecraftForge.EVENT_BUS.post(LocalKeyboardEvent(record))
            }
            context.get().packetHandled = true
        }
    }

    @SubscribeEvent
    fun receive(event: LocalKeyboardEvent) {
        dbWriter.record(event.record)
    }

    data class LocalKeyboardEvent(
        val record: KeyboardRecord
    ) : Event()
}