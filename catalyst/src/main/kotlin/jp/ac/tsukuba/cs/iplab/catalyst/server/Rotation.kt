package jp.ac.tsukuba.cs.iplab.catalyst.server

import kotlinx.serialization.Serializable

@Serializable
data class Rotation(val yaw: Float, val pitch: Float)