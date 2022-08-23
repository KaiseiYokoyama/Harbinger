package jp.ac.tsukuba.cs.iplab.catalyst.server.record

import kotlinx.serialization.Serializable
import jp.ac.tsukuba.cs.iplab.catalyst.server.record.Entity

@Serializable
data class ExperienceOrbEntity(
    val xpColor: Int,
    val xpOrbAge: Int,
    val delayBeforeCanPickup: Int,
    val xpValue: Int,
    val entity: Entity,
) : Entity(entity)