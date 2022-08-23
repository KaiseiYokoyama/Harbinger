package jp.ac.tsukuba.cs.iplab.catalyst.server.event

import jp.ac.tsukuba.cs.iplab.catalyst.server.record.LivingEntity
import jp.ac.tsukuba.cs.iplab.catalyst.server.record.PlayerEntity
import kotlinx.serialization.Serializable

@Serializable
data class BabyEntitySpawnEvent(
    val parentA: LivingEntity,
    val parentB: LivingEntity,
    val child: LivingEntity?,
    val player: PlayerEntity?,
): EventRecord()
