package jp.ac.tsukuba.cs.iplab.catalyst.server.event

import jp.ac.tsukuba.cs.iplab.catalyst.server.Vector3
import jp.ac.tsukuba.cs.iplab.catalyst.server.record.*
import kotlinx.serialization.Serializable

sealed interface LivingEvent {
    /**
     * プレイヤーが動物を手懐けたとき
     */
    @Serializable
    data class AnimalTameEvent(
        val animal: Entity,
        val player: PlayerEntity,
    ) : EventRecord(), LivingEvent

    /**
     * プレイヤーがエンダーパールなどを使ってテレポートしたとき
     */
    @Serializable
    data class EnderTeleportEvent(
        val target: Vector3<Double>,
        val damage: Float,
        var playerEntity: PlayerEntity,
    ) : EventRecord(), LivingEvent

//    /**
//     * net.minecraftforge.event.entity.living.LivingDamageEvent
//     * プレイヤーが敵Mobや溶岩からダメージを受けたとき
//     */
//    @Serializable
//    data class PlayerDamagedEvent(
//        val source: DamageSource,
//        val amount: Float,
//        var playerEntity: PlayerEntity,
//    ) : EventRecord(), Living
//
//    /**
//     * net.minecraftforge.event.entity.living.LivingDamageEvent
//     * Mobがプレイヤーにダメージを受けたとき
//     */
//    @Serializable
//    data class MobDamagedEvent(
//        val source: EntityDamageSource,
//        val amount: Float,
//        val livingEntity: LivingEntity,
//    ) : EventRecord(), Living

    /**
     * net.minecraftforge.event.entity.living.LivingDamageEvent
     * LivingEntityがダメージを受けたとき
     *
     * ref. EntityDamageSource, PlayerEntity
     */
    @Serializable
    data class DamagedEvent(
        val source: DamageSource,
        val amount: Float,
        val livingEntity: LivingEntity,
    ) : EventRecord(), LivingEvent

    /**
     * net.minecraftforge.event.entity.living.LivingDeathEvent
     * LivingEntityが死んだとき
     */
    @Serializable
    data class DeathEvent(
        val source: DamageSource,
        var livingEntity: LivingEntity,
    ) : EventRecord(), LivingEvent

    /**
     * net.minecraftforge.event.entity.living.LivingDropsEvent
     * LivingEntityが死んで、アイテムを落としたとき
     */
    @Serializable
    data class DropsEvent(
        val source: DamageSource,
        val drops: Set<ItemEntity>,
        val lootingLevel: Int,
        val recentlyHit: Boolean,
        var livingEntity: LivingEntity,
    ) : EventRecord(), LivingEvent

    sealed interface UseItemEvent {
        val item: ItemStack
        val duration: Int

        @Serializable
        data class Start(
            override val item: ItemStack,
            override val duration: Int,
            var livingEntity: LivingEntity,
        ) : EventRecord(), LivingEvent, UseItemEvent

        @Serializable
        data class Stop(
            override val item: ItemStack,
            override val duration: Int,
            var livingEntity: LivingEntity,
        ) : EventRecord(), LivingEvent, UseItemEvent

        @Serializable
        data class Finish(
            override val item: ItemStack,
            override val duration: Int,
            var livingEntity: LivingEntity,
        ) : EventRecord(), LivingEvent, UseItemEvent

        @Deprecated("This causes redundant logs.")
        @Serializable
        data class Tick(
            override val item: ItemStack,
            override val duration: Int,
            var livingEntity: LivingEntity,
        ) : EventRecord(), LivingEvent, UseItemEvent
    }

    @Serializable
    enum class EquipmentSlotType {
        MAINHAND,
        OFFHAND,
        FEET,
        LEGS,
        CHEST,
        HEAD,
    }

    /**
     * net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent
     * LivingEntityが装備を変更したとき
     * 装備変更には、アイテム数の増減も含む（例：土を掘る、丸石を置くなど）
     */
    @Serializable
    data class EquipmentChangeEvent(
        val slot: EquipmentSlotType,
        val from: ItemStack,
        val to: ItemStack,
        var livingEntity: LivingEntity,
    ) : EventRecord(), LivingEvent

    /**
     * net.minecraftforge.event.entity.living.LivingExperienceDropEvent
     * LivingEntityが経験値を落としたとき
     */
    @Serializable
    data class ExperienceDropEvent(
        val attackingPlayer: PlayerEntity?,
        val originalExperiencePoints: Int,
        var livingEntity: LivingEntity,
    ) : EventRecord(), LivingEvent

    /**
     * net.minecraftforge.event.entity.living.LivingFallEvent
     * LivingEntityが落ちたとき
     */
    @Serializable
    data class FallEvent(
        val distance: Float,
        val damageMultiplier: Float,
        var livingEntity: LivingEntity,
    ) : EventRecord(), LivingEvent

    /**
     * LivingEntityが回復したとき
     */
    @Serializable
    data class HealEvent(
        val amount: Float,
        var livingEntity: LivingEntity,
    ) : EventRecord(), LivingEvent

    /**
     * LivingEntityがダメージを負ったとき
     */
    @Serializable
    data class HurtEvent(
        val source: DamageSource,
        val amount: Float,
        var livingEntity: LivingEntity,
    ) : EventRecord(), LivingEvent

    /**
     * LivingEntityがノックバックさせられたとき
     */
    @Serializable
    data class KnockBackedEvent(
        val strength: Float,
        val ratioX: Double,
        val ratioZ: Double,
        val originalStrength: Float,
        val originalRatioX: Double,
        val originalRatioZ: Double,
        var livingEntity: LivingEntity,
    ) : EventRecord(), LivingEvent

    /**
     * エンダードラゴンやウィザーがブロックを壊そうとしたとき
     * プレイヤーは含まれてないかも
     */
    @Serializable
    data class DestroyBlockEvent(
        val blockPos: Vector3<Int>,
        /**
         * BlockState.getBlock().class.simpleName
         */
        val state: String,
        var livingEntity: LivingEntity,
    ) : EventRecord(), LivingEvent

    sealed interface Potion {
        @Serializable
        data class AddedEvent(
            val effect: EffectInstance?,
            val oldEffect: EffectInstance?,
            val livingEntity: LivingEntity,
        ) : EventRecord(), LivingEvent, Potion

        @Serializable
        data class RemoveEvent(
            val effect: EffectInstance?,
            /**
             * Effect.getName()
             */
            val potion: String,
            val livingEntity: LivingEntity,
        ) : EventRecord(), LivingEvent, Potion

        @Serializable
        data class ExpiryEvent(
            val effect: EffectInstance?,
            val livingEntity: LivingEntity,
        ) : EventRecord(), LivingEvent, Potion
    }

    @Serializable
    data class SleepingLocationCheckEvent(
        val location: Vector3<Int>,
        val livingEntity: LivingEntity,
    ) : EventRecord(), LivingEvent
}