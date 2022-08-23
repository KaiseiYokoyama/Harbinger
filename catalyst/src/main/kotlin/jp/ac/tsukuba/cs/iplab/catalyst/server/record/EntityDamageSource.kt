package jp.ac.tsukuba.cs.iplab.catalyst.server.record

import kotlinx.serialization.Serializable

@Serializable
class EntityDamageSource(
    val damageSourceEntity: Entity?,
    val isThornsDamage: Boolean,
    val damageSource: DamageSource,
) : DamageSource(damageSource)