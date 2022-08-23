package jp.ac.tsukuba.cs.iplab.catalyst.server.event

import jp.ac.tsukuba.cs.iplab.catalyst.server.Vector3
import jp.ac.tsukuba.cs.iplab.catalyst.server.record.*
import kotlinx.serialization.Serializable

sealed interface PlayerEvent {
    val playerEntity: PlayerEntity

    @Serializable
    data class LoggedInEvent(
        override val playerEntity: PlayerEntity,
    ) : EventRecord(), PlayerEvent

    @Serializable
    data class LoggedOutEvent(
        override val playerEntity: PlayerEntity,
    ) : EventRecord(), PlayerEvent

    @Serializable
    data class RespawnEvent(
        val endConquered: Boolean,
        override val playerEntity: PlayerEntity,
    ) : EventRecord(), PlayerEvent

    /**
     * プレイヤーが目を覚ましたとき
     */
    @Serializable
    data class WakeUpEvent(
        val wakeImmediately: Boolean,
        val updateWorld: Boolean,
        override val playerEntity: PlayerEntity,
    ) : EventRecord(), PlayerEvent

    /**
     * プレイヤーがベッドで眠ったとき
     */
    @Serializable
    data class SleepInBedEvent(
        val result: SleepResult,
        override val playerEntity: PlayerEntity,
    ) : EventRecord(), PlayerEvent

    /**
     * 眠れるかどうかをチェックしたとき
     */
    @Serializable
    data class SleepingTimeCheckEvent(
        val location: Vector3<Int>?,
        override val playerEntity: PlayerEntity,
    ) : EventRecord(), PlayerEvent

    /**
     * プレイヤーのスポーン位置が設定されたとき
     */
    @Serializable
    data class SetSpawnEvent(
        val forced: Boolean,
        val position: Vector3<Int>?,
        override val playerEntity: PlayerEntity,
    ) : EventRecord(), PlayerEvent

    @Serializable
    data class ArrowNockEvent(
        val bow: ItemStack,
        val hand: Hand,
        val hasAmmo: Boolean,
        override val playerEntity: PlayerEntity,
    ) : EventRecord(), PlayerEvent

    /**
     * 弓の使用をやめたとき（撃ったとき？）
     */
    @Serializable
    class ArrowLooseEvent(
        val bow: ItemStack,
        val hasAmmo: Boolean,
        val charge: Int,
        override val playerEntity: PlayerEntity,
    ) : EventRecord(), PlayerEvent

    /**
     * アイテムが溶けたとき
     */
    @Serializable
    data class ItemSmeltedEvent(
        val smelting: ItemStack,
        override val playerEntity: PlayerEntity,
    ) : EventRecord(), PlayerEvent

    /**
     * UI上でアイテムのツールチップを表示したとき
     */
    @Serializable
    data class ItemTooltipEvent(
        /**
         * ITooltipFlag.isAdvanced()
         */
        val tooltipFlag: Boolean,
        val itemStack: ItemStack,
        /**
         * ItemTooltipEvent.toolTip.map { it.getUnformattedComponentText() }
         */
        val tooltips: List<String>,
        override val playerEntity: PlayerEntity,
    ) : EventRecord(), PlayerEvent

//    /**
//     * クワを使おうとしたとき
//     */
//    @Serializable
//    data class UseHoeEvent(
//        override val playerEntity: PlayerEntity,
//    ) : EventRecord(), Player

    @Serializable
    data class Clone(
        override val playerEntity: PlayerEntity,
    ) : EventRecord(), PlayerEvent

    /**
     * 金床からアイテムを取り出したとき
     */
    @Serializable
    data class AnvilRepairEvent(
        val item: ItemStack,
        val ingredient: ItemStack,
        val output: ItemStack,
        val breakChance: Float,
        override val playerEntity: PlayerEntity,
    ) : EventRecord(), PlayerEvent

    /**
     * プレイヤーが実績を解除したとき
     */
    @Serializable
    data class AdvancementEvent(
        /**
         * Advancement.getId().toString()
         */
        val advancementId: String,
        override val playerEntity: PlayerEntity,
    ) : EventRecord(), PlayerEvent

    sealed interface XpEvent {
        @Serializable
        data class PickupXp(
            val orb: ExperienceOrbEntity,
            override val playerEntity: PlayerEntity,
        ) : EventRecord(), PlayerEvent, XpEvent

        @Serializable
        data class XpChange(
            val amount: Int,
            override val playerEntity: PlayerEntity,
        ) : EventRecord(), PlayerEvent, XpEvent

        @Serializable
        data class LevelChange(
            val levels: Int,
            override val playerEntity: PlayerEntity,
        ) : EventRecord(), PlayerEvent, XpEvent
    }

    /**
     * アイテムを破壊したとき
     */
    @Serializable
    data class DestroyItemEvent(
        /**
         * 破壊したアイテム
         */
        val original: ItemStack,
        /**
         * アイテムを持っていた手
         */
        val hand: Hand?,
        override val playerEntity: PlayerEntity,
    ) : EventRecord(), PlayerEvent

    /**
     * バケツを満たしたとき
     */
    @Serializable
    data class FillBucketEvent(
        /**
         * 空のバケツ
         */
        val empty: ItemStack,
        /**
         * 満たされたバケツ
         */
        val filled: ItemStack,
        override val playerEntity: PlayerEntity,
    ) : EventRecord(), PlayerEvent

    /**
     * 骨粉を使おうとしたとき
     */
    @Serializable
    data class BonemealEvent(
        val stack: ItemStack,
        val blockPos: Vector3<Int>,
        override val playerEntity: PlayerEntity,
    ) : EventRecord(), PlayerEvent

    sealed interface Interact {
        val hand: Hand
        val pos: Vector3<Int>
        val face: Direction?

        /**
         * ブロックを右クリックしたとき
         */
        @Serializable
        data class RightClickBlock(
            override val hand: Hand,
            override val pos: Vector3<Int>,
            override val face: Direction?,
            override val playerEntity: PlayerEntity,
        ) : EventRecord(), PlayerEvent, Interact

        /**
         * アイテムを右クリックしたとき
         */
        @Serializable
        data class RightClickItem(
            override val hand: Hand,
            override val pos: Vector3<Int>,
            override val face: Direction?,
            override val playerEntity: PlayerEntity,
        ) : EventRecord(), PlayerEvent, Interact

        /**
         * 空の右手で空の空間を右クリックしたとき
         */
        @Serializable
        data class RightClickEmpty(
            override val hand: Hand,
            override val pos: Vector3<Int>,
            override val face: Direction?,
            override val playerEntity: PlayerEntity,
        ) : EventRecord(), PlayerEvent, Interact

        /**
         * ブロックを左クリックしたとき
         */
        @Serializable
        data class LeftClickBlock(
            override val hand: Hand,
            override val pos: Vector3<Int>,
            override val face: Direction?,
            override val playerEntity: PlayerEntity,
        ) : EventRecord(), PlayerEvent, Interact

        /**
         * 空の左手で空の空間を右クリックしたとき
         */
        @Serializable
        data class LeftClickEmpty(
            override val hand: Hand,
            override val pos: Vector3<Int>,
            override val face: Direction?,
            override val playerEntity: PlayerEntity,
        ) : EventRecord(), PlayerEvent, Interact

        /**
         * エンティティを右クリックしたとき
         */
        @Serializable
        data class EntityInteract(
            val target: Entity,
            override val hand: Hand,
            override val pos: Vector3<Int>,
            override val face: Direction?,
            override val playerEntity: PlayerEntity,
        ) : EventRecord(), PlayerEvent, Interact

        /**
         * エンティティを右クリックしたとき
         * エンティティ上の右クリックされた位置つき（エンティティ最下部中央が原点）
         */
        @Serializable
        data class EntityInteractSpecific(
            val localPos: Vector3<Double>,
            val target: Entity,
            override val hand: Hand,
            override val pos: Vector3<Int>,
            override val face: Direction?,
            override val playerEntity: PlayerEntity,
        ) : EventRecord(), PlayerEvent, Interact
    }

//    /**
//     * プレイヤーが地面に落ちているアイテムエンティティにぶつかったとき
//     */
//    @Serializable
//    data class EntityItemPickupEvent(
//        val item: ItemEntity,
//        override val playerEntity: PlayerEntity,
//    ) : EventRecord(), Player

    /**
     * プレイヤーがアイテムを拾ったとき
     */
    @Serializable
    data class ItemPickupEvent(
        val entity: ItemEntity,
        val stack: ItemStack,
        override val playerEntity: PlayerEntity,
    ) : EventRecord(), PlayerEvent

    /**
     * プレイヤーがブロックを収穫しようとしたとき
     */
    @Serializable
    data class HarvestCheck(
        val success: Boolean,
        val block: String,
        override val playerEntity: PlayerEntity,
    ) : EventRecord(), PlayerEvent

    //    data class FlyableFallEvent

    @Serializable
    data class ChangedDimensionEvent(
        val from: DimensionType,
        val to: DimensionType,
        override val playerEntity: PlayerEntity,
    ) : EventRecord(), PlayerEvent

    sealed interface ContainerEvent {
        @Serializable
        data class Open(
            val container: Container,
            override val playerEntity: PlayerEntity,
        ) : EventRecord(), PlayerEvent, ContainerEvent

        @Serializable
        data class Close(
            val container: Container,
            override val playerEntity: PlayerEntity,
        ) : EventRecord(), PlayerEvent, ContainerEvent
    }

    //    data class LoadFromFile

    @Serializable
    data class CriticalHitEvent(
        val damageModifier: Float,
        val oldDamageModifier: Float,
        val target: Entity,
        val vanillaCritical: Boolean,
        override val playerEntity: PlayerEntity,
    ) : EventRecord(), PlayerEvent

    /**
     * プレイヤーがアイテムを釣り上げたとき
     */
    @Serializable
    data class ItemFishedEvent(
        /**
         * プレイヤーが受け取るアイテム
         */
        val stacks: HashMap<Int, ItemStack>,
        val rodDamage: Int,
        override val playerEntity: PlayerEntity,
    ) : EventRecord(), PlayerEvent

//    /**
//     * プレイヤーが対象のEntityを追いかけ始めたとき
//     */
//    @Serializable
//    data class StartTracking(
//        val target: Entity,
//        override val playerEntity: PlayerEntity,
//    ) : EventRecord(), Player

//    @Serializable
//    data class Visibility(
//        val visibilityModifier: Double,
//        override val playerEntity: PlayerEntity,
//    ) : EventRecord(), Player

//    @Serializable
//    data class InputUpdateEvent(
//        val movementInput: MovementInputRecord,
//        override val playerEntity: PlayerEntity,
//    ) : EventRecord(), Player {
//        @Serializable
//        data class MovementInputRecord(
//            var moveStrafe: Float,
//            var moveForward: Float,
//            var forwardKeyDown: Boolean,
//            var backKeyDown: Boolean,
//            var leftKeyDown: Boolean,
//            var rightKeyDown: Boolean,
//            var jump: Boolean,
//            var sneaking: Boolean,
//        )
//    }

//    data class SaveToFile

    /**
     * アイテムをクラフトしたとき
     */
    @Serializable
    data class ItemCraftedEvent(
        val crafting: ItemStack,
        val craftMatrix: HashMap<Int, ItemStack>,
        override val playerEntity: PlayerEntity,
    ) : EventRecord(), PlayerEvent

//    data class NameFormat

//    data class RenderPlayer

    /**
     * エンティティを攻撃したとき
     */
    @Serializable
    data class AttackEntityEvent(
        val target: Entity,
        override val playerEntity: PlayerEntity,
    ) : EventRecord(), PlayerEvent

    /**
     * 醸造代からポーションを取り出したとき
     */
    @Serializable
    data class BrewedPotionEvent(
        val stack: ItemStack,
        override val playerEntity: PlayerEntity,
    ) : EventRecord(), PlayerEvent
}