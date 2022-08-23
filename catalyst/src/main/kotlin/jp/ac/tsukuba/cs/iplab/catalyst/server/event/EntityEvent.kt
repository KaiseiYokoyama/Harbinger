package jp.ac.tsukuba.cs.iplab.catalyst.server.event

import jp.ac.tsukuba.cs.iplab.catalyst.server.Vector3
import jp.ac.tsukuba.cs.iplab.catalyst.server.record.DimensionType
import jp.ac.tsukuba.cs.iplab.catalyst.server.record.Entity
import kotlinx.serialization.Serializable

sealed interface EntityEvent {
    /**
     * Entityが何かに騎乗したとき
     */
    @Serializable
    data class MountEvent(
        val mounting: Entity,
        val mounted: Entity,
        val isMounting: Boolean,
    ) : EventRecord(), EntityEvent

    /**
     * Entityがディメンションを移動するとき
     */
    @Serializable
    data class TravelToDimensionEvent(
        val entity: Entity,
        val dimension: DimensionType,
    ) : EventRecord(), EntityEvent

    sealed interface ProjectileImpact {
        val ray: Vector3<Double>

        @Serializable
        data class Arrow(
            override val ray: Vector3<Double>
        ) : EventRecord(), EntityEvent, ProjectileImpact

        @Serializable
        data class Fireball(
            override val ray: Vector3<Double>
        ) : EventRecord(), EntityEvent, ProjectileImpact

        @Serializable
        data class Throwable(
            val throwable: Entity,
            override val ray: Vector3<Double>
        ) : EventRecord(), EntityEvent, ProjectileImpact

        @Serializable
        data class FireworkRocket(
            val rocket: Entity,
            override val ray: Vector3<Double>
        ) : EventRecord(), EntityEvent, ProjectileImpact
    }

}