package jp.ac.tsukuba.cs.iplab.catalyst.server.event

import jp.ac.tsukuba.cs.iplab.catalyst.server.Vector3
import kotlinx.serialization.Serializable

sealed interface WorldEvent {
    @Serializable
    data class SaplingGrowTreeEvent(
        val pos: Vector3<Int>,
    ) : EventRecord(), WorldEvent

    @Serializable
    data class SleepFinishedTimeEvent(
        val newTime: Long,
    ) : EventRecord(), WorldEvent
}