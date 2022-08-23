package jp.ac.tsukuba.cs.iplab.catalyst.server.event

import jp.ac.tsukuba.cs.iplab.catalyst.server.Vector3
import jp.ac.tsukuba.cs.iplab.catalyst.server.record.Entity
import jp.ac.tsukuba.cs.iplab.catalyst.server.record.ItemStack
import jp.ac.tsukuba.cs.iplab.catalyst.server.record.PlayerEntity
import kotlinx.serialization.Serializable

sealed interface Block {
    val blockPos: Vector3<Int>

    /**
     * BlockState.getBlock().class.simpleName
     */
    val state: String

    /**
     * ツールを持ってブロックを右クリックしたとき？
     */
    @Serializable
    data class BlockToolInteractEvent(
        override val blockPos: Vector3<Int>,
        override val state: String,
        val itemStack: ItemStack,
        /**
         * ToolType.name
         */
        val toolType: String,
        var playerEntity: PlayerEntity,
    ) : EventRecord(), Block

    /**
     * ブロックがプレイヤーに壊されそうになったとき
     */
    @Serializable
    data class BreakEvent(
        override val blockPos: Vector3<Int>,
        override val state: String,
        val exp: Int,
        var playerEntity: PlayerEntity,
    ) : EventRecord(), Block

    /**
     * 作物が成長したとき
     * BlockEvent.CropGrowEvent.Postに対応
     */
    @Serializable
    data class CropGrownEvent(
        override val blockPos: Vector3<Int>,
        override val state: String,
        val originalState: String,
    ) : EventRecord(), Block

    /**
     * プレイヤーがブロックを設置したとき
     */
    @Serializable
    data class PlayerPlaceEvent(
        override val blockPos: Vector3<Int>,
        override val state: String,
        /**
         * EntityPlaceEvent().placedBlock.getBlock().class.simpleName
         */
        val placedBlock: String,
        val placedAgainst: String,
        var playerEntity: PlayerEntity,
    ) : EventRecord(), Block

    /**
     * 畑が踏み荒らされたとき
     */
    @Serializable
    data class FarmlandTrampleEvent(
        override val blockPos: Vector3<Int>,
        override val state: String,
        val entity: Entity,
        val fallDistance: Float,
    ) : EventRecord(), Block

    /**
     * ネザーポータルが開いたとき
     */
    @Serializable
    data class PortalSpawnEvent(
        override val blockPos: Vector3<Int>,
        override val state: String,
    ): EventRecord(), Block
}