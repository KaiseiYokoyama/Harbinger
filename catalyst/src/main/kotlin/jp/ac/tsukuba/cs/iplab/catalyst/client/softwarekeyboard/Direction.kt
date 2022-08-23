package jp.ac.tsukuba.cs.iplab.catalyst.client.softwarekeyboard

import kotlinx.serialization.Serializable

@Serializable
enum class Direction {
    Up, Down, Left, Right,
}