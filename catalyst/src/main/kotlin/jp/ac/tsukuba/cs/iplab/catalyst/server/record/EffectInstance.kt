package jp.ac.tsukuba.cs.iplab.catalyst.server.record

import kotlinx.serialization.Serializable

@Serializable
data class EffectInstance(
    /**
     * Effect.getName()
     */
    val name: String,
    val duration: Int,
    val amplifier: Int,
    val ambient: Boolean,
    val showParticles: Boolean,
    val showIcon: Boolean,
)