package jp.ac.tsukuba.cs.iplab.catalyst.server

import kotlinx.serialization.Serializable

@Serializable
data class Vector3<N: Number>(val x: N, val y: N, val z: N)