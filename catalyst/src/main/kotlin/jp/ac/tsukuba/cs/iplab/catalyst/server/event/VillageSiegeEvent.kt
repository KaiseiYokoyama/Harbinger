package jp.ac.tsukuba.cs.iplab.catalyst.server.event

import jp.ac.tsukuba.cs.iplab.catalyst.server.Vector3
import jp.ac.tsukuba.cs.iplab.catalyst.server.record.PlayerEntity
import kotlinx.serialization.Serializable

@Serializable
data class VillageSiegeEvent(
    val playerEntity: PlayerEntity,
    val spawnPos: Vector3<Double>,
) : EventRecord()
