package jp.ac.tsukuba.cs.iplab.catalyst.server.record

import kotlinx.serialization.Serializable

/**
 * LivingEntity特有のフィールドを記録するためのレコードクラス
 */
@Serializable
open class LivingEntity(
    /**
     * LivingEntity.getHealth()
     */
    val health: Float,
    val isSwingInProgress: Boolean,
    val swingingHand: Hand?,
//    val swingProgressInt: Int,
    val swingProgress: Float,
    val arrowHitTimer: Int,
    val beeStingRemovalCooldown: Int,
    val hurtTime: Int,
    val maxHurtTime: Int,
    val attackedAtYaw: Float,
    val deathTime: Int,
//    val ticksSinceLastSwing: Int,
    val limbSwingAmount: Float,
    val limbSwing: Float,
    val rotationYawHead: Float,
//    val recentlyHit: Int,
//    val dead: Boolean,
    val idleTime: Int,
//    val onGroundSpeedFactor: Float,
//    val movedDistance: Float,
//    val scoreValue: Int,
//    val lastDamage: Float,
//    val isJumping: Boolean,
//    val jumpTicks: Int,
    val moveStrafing: Float,
    val moveVertical: Float,
    val moveForward: Float,
    val activeItemStack: ItemStack,
//    val activeItemStackUseCount: Int,
    val entity: Entity,
) : Entity(entity) {
    constructor(ths: LivingEntity) : this(
        health = ths.health,
        isSwingInProgress = ths.isSwingInProgress,
        swingingHand = ths.swingingHand,
        swingProgress = ths.swingProgress,
        arrowHitTimer = ths.arrowHitTimer,
        beeStingRemovalCooldown = ths.beeStingRemovalCooldown,
        hurtTime = ths.hurtTime,
        maxHurtTime = ths.maxHurtTime,
        attackedAtYaw = ths.attackedAtYaw,
        deathTime = ths.deathTime,
        limbSwingAmount = ths.limbSwingAmount,
        limbSwing = ths.limbSwing,
        rotationYawHead = ths.rotationYawHead,
        idleTime = ths.idleTime,
        moveStrafing = ths.moveStrafing,
        moveVertical = ths.moveVertical,
        moveForward = ths.moveForward,
        activeItemStack = ths.activeItemStack,
        entity = ths.entity,
    )
}