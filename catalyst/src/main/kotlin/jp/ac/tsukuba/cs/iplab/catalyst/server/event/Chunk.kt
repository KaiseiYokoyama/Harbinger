package jp.ac.tsukuba.cs.iplab.catalyst.server.event

import jp.ac.tsukuba.cs.iplab.catalyst.server.record.ChunkPos
import jp.ac.tsukuba.cs.iplab.catalyst.server.record.DimensionType
import jp.ac.tsukuba.cs.iplab.catalyst.server.record.PlayerEntity
import kotlinx.serialization.Serializable

sealed interface Chunk {
    @Serializable
    data class Watch(
        val player: PlayerEntity,
        val pos: ChunkPos,
        val dimension: DimensionType,
    ) : EventRecord(), Chunk

    @Serializable
    data class UnWatch(
        val player: PlayerEntity,
        val pos: ChunkPos,
        val dimension: DimensionType,
    ) : EventRecord(), Chunk
}