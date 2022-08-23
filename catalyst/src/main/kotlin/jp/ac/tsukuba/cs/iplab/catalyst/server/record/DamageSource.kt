package jp.ac.tsukuba.cs.iplab.catalyst.server.record

import kotlinx.serialization.Serializable

@Serializable
open class DamageSource(
    val isUnblockable: Boolean,
    val isDamageAllowedInCreativeMode: Boolean,
    val damageIsAbsolute: Boolean,
    val hungerDamage: Float,
    val fireDamage: Boolean,
    val projectile: Boolean,
    val difficultyScaled: Boolean,
    val magicDamage: Boolean,
    val explosion: Boolean,
    val damageType: String?,
) {
    constructor(
        ths: DamageSource
    ) : this(
        isUnblockable = ths.isUnblockable,
        isDamageAllowedInCreativeMode = ths.isDamageAllowedInCreativeMode,
        damageIsAbsolute = ths.damageIsAbsolute,
        hungerDamage = ths.hungerDamage,
        fireDamage = ths.fireDamage,
        projectile = ths.projectile,
        difficultyScaled = ths.difficultyScaled,
        magicDamage = ths.magicDamage,
        explosion = ths.explosion,
        damageType = ths.damageType,
    )
}