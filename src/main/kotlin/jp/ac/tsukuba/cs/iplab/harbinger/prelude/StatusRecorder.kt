package jp.ac.tsukuba.cs.iplab.harbinger.prelude

import jp.ac.tsukuba.cs.iplab.catalyst.db.DBWriter
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.server.ServerLifecycleHooks

class StatusRecorder(
    val dbWriter: DBWriter
) {
    var count = 0

    @SubscribeEvent
    fun tick(event: TickEvent.ServerTickEvent) {
        count++
        if (count >= 20) {
            // reset
            count = 0

            playerEntities().forEach { player ->
                val status = player.asRecord()
                dbWriter.record(status)
            }
        }
    }

    private fun playerEntities(): List<ServerPlayerEntity> {
        return ServerLifecycleHooks.getCurrentServer().playerList.players
    }
}