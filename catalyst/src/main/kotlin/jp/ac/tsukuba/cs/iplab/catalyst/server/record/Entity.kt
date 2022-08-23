package jp.ac.tsukuba.cs.iplab.catalyst.server.record

import jp.ac.tsukuba.cs.iplab.catalyst.server.Rotation
import jp.ac.tsukuba.cs.iplab.catalyst.server.Vector3
import kotlinx.serialization.Serializable

/**
 * net.minecraft.entity.Entityのレコード用クラス
 */
@Serializable
open class Entity(
    /**
     * 位置
     */
    val position: Vector3<Double>,
    /**
     * 動き
     */
    val motion: Vector3<Double>,
    /**
     * 回転
     */
    val rotation: Rotation,
    val fallDistance: Float,
    val dimension: DimensionType,
    /**
     * 接地しているか否か
     */
    val onGround: Boolean,
    /**
     * 生存経過時間
     */
    val ticksExisted: Int,
    val fire: Int,
    val inWater: Boolean,
    val inLava: Boolean,
//    val eyesInWater: Boolean,
//    val inPortal: Boolean,
//    val portalCounter: Int,
    val cachedUniqueIdString: String,
    val eyeHeight: Float,
    /**
     * entityUniqueID.toString()
     */
    val uuid: String,
    /**
     * class.getCanonicalName()
     */
    val entityType: String,
) : jp.ac.tsukuba.cs.iplab.catalyst.Record() {
    constructor(
        ths: Entity
    ) : this(
        position = ths.position,
        motion = ths.motion,
        rotation = ths.rotation,
        fallDistance = ths.fallDistance,
        dimension = ths.dimension,
        onGround = ths.onGround,
        ticksExisted = ths.ticksExisted,
        fire = ths.fire,
        inWater = ths.inWater,
        inLava = ths.inLava,
        cachedUniqueIdString = ths.cachedUniqueIdString,
        eyeHeight = ths.eyeHeight,
        uuid = ths.uuid,
        entityType = ths.entityType,
    )
}