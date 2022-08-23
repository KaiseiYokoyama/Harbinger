package jp.ac.tsukuba.cs.iplab.catalyst.server.record

import kotlinx.serialization.Serializable

@Serializable
class ItemEntity(
    val thrower: String?,
    val owner: String?,
    val hoverStart: Float,
    var lifespan: Int,
    val itemStack: ItemStack,
    val entity: Entity,
) : Entity(entity)