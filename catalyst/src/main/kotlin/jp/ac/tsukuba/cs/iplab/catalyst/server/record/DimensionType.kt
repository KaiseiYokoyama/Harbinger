package jp.ac.tsukuba.cs.iplab.catalyst.server.record

import kotlinx.serialization.Serializable

@Serializable
enum class DimensionType {
    Overworld, Nether, End
}