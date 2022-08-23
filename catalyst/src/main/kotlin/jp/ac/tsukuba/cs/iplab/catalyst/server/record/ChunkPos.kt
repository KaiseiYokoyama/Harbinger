package jp.ac.tsukuba.cs.iplab.catalyst.server.record

import kotlinx.serialization.Serializable

@Serializable
data class ChunkPos(
    val x: Int,
    val z: Int,
)
