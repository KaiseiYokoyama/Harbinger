package jp.ac.tsukuba.cs.iplab.catalyst.server.event

import jp.ac.tsukuba.cs.iplab.catalyst.server.record.ItemEntity
import jp.ac.tsukuba.cs.iplab.catalyst.server.record.PlayerEntity
import kotlinx.serialization.Serializable

sealed interface ItemEvent {
    @Serializable
    data class ItemExpireEvent(
        val item: ItemEntity,
        val extraLive: Int,
    ) : EventRecord(), ItemEvent

    /**
     * net.minecraftforge.event.entity.item.ItemTossEvent
     * プレイヤーがアイテムを投げ捨てたとき
     */
    @Serializable
    data class ItemTossEvent(
        val item: ItemEntity,
        var playerEntity: PlayerEntity,
    ) : EventRecord(), ItemEvent
}