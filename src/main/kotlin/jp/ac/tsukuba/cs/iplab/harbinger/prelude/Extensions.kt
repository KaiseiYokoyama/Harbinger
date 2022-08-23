package jp.ac.tsukuba.cs.iplab.harbinger.prelude

import jp.ac.tsukuba.cs.iplab.catalyst.server.Vector3
import jp.ac.tsukuba.cs.iplab.catalyst.server.Rotation as RotationRecord
import jp.ac.tsukuba.cs.iplab.catalyst.server.event.LivingEvent
import jp.ac.tsukuba.cs.iplab.catalyst.server.record.DimensionType
import jp.ac.tsukuba.cs.iplab.catalyst.server.record.ChunkPos as ChunkPosRecord
import jp.ac.tsukuba.cs.iplab.catalyst.server.record.EffectInstance as EffectInstanceRecord
import jp.ac.tsukuba.cs.iplab.catalyst.server.record.Hand as HandRecord
import jp.ac.tsukuba.cs.iplab.catalyst.server.record.Direction as DirectionRecord
import jp.ac.tsukuba.cs.iplab.catalyst.server.record.SleepResult as SleepResultRecord
import jp.ac.tsukuba.cs.iplab.catalyst.server.record.ItemStack as ItemStackRecord
import jp.ac.tsukuba.cs.iplab.catalyst.server.record.FoodStatsRecord
import jp.ac.tsukuba.cs.iplab.catalyst.server.record.PlayerInventoryRecord
import jp.ac.tsukuba.cs.iplab.catalyst.server.record.Container as ContainerRecord
import jp.ac.tsukuba.cs.iplab.catalyst.server.record.DamageSource as DamageSourceRecord
import jp.ac.tsukuba.cs.iplab.catalyst.server.record.Entity as EntityRecord
import jp.ac.tsukuba.cs.iplab.catalyst.server.record.ItemEntity as ItemEntityRecord
import jp.ac.tsukuba.cs.iplab.catalyst.server.record.ExperienceOrbEntity as ExperienceOrbEntityRecord
import jp.ac.tsukuba.cs.iplab.catalyst.server.record.LivingEntity as LivingEntityRecord
import jp.ac.tsukuba.cs.iplab.catalyst.server.record.PlayerEntity as PlayerEntityRecord
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.item.ExperienceOrbEntity
import net.minecraft.entity.item.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.entity.player.PlayerEntity.SleepResult
import net.minecraft.inventory.EquipmentSlotType
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.container.Container
import net.minecraft.item.ItemStack
import net.minecraft.potion.EffectInstance
import net.minecraft.util.*
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.util.registry.Registry
import net.minecraft.util.text.StringTextComponent
import net.minecraft.world.World

fun BlockPos.asRecord(): Vector3<Int> = Vector3(
    x = this.x,
    y = this.y,
    z = this.z,
)

fun ChunkPos.asRecord() = ChunkPosRecord(
    x = this.x,
    z = this.z
)

fun Vector3d.asRecord(): Vector3<Double> = Vector3(
    x = this.x,
    y = this.y,
    z = this.z,
)

fun Hand.asRecord() = when (this) {
    Hand.MAIN_HAND -> HandRecord.MAIN_HAND
    Hand.OFF_HAND -> HandRecord.OFF_HAND
}

fun ItemStack.asRecord(): ItemStackRecord = if (isEmpty) {
    ItemStackRecord.Empty()
} else {
    ItemStackRecord.NotEmpty(
        itemId = Registry.ITEM.getId(this.item),
        resourceLocation = Registry.ITEM.getKey(this.item).toString(),
        count = count,
    )
}

fun NonNullList<ItemStack>.asHashMap() = this.asIterable()
    .foldIndexed<ItemStack, HashMap<Int, ItemStackRecord>>(HashMap()) { idx, map, item ->
        map[idx] = item.asRecord()
        map
    }

fun IInventory.asHashMap() = (0..sizeInventory)
    .fold(HashMap<Int, ItemStackRecord>()) { map, idx ->
        map[idx] = getStackInSlot(idx).asRecord()
        map
    }

fun Container.asRecord() = ContainerRecord(
    items = this.inventory.asHashMap(),
    type = this.javaClass.canonicalName,
)

fun PlayerInventory.asRecord() = PlayerInventoryRecord(
    main = mainInventory.asHashMap(),
    armour = armorInventory.asHashMap(),
    offhand = offHandInventory.map { it.asRecord() }.getOrNull(0),
    currentItem = currentItem,
)

fun FoodStats.asRecord() = FoodStatsRecord(
    foodLevel = foodLevel,
    foodSaturationLevel = saturationLevel,
)

fun SleepResult.asRecord() = when (this) {
    SleepResult.NOT_POSSIBLE_NOW -> SleepResultRecord.NOT_POSSIBLE_NOW
    SleepResult.NOT_POSSIBLE_HERE -> SleepResultRecord.NOT_POSSIBLE_HERE
    SleepResult.TOO_FAR_AWAY -> SleepResultRecord.TOO_FAR_AWAY
    SleepResult.OBSTRUCTED -> SleepResultRecord.OBSTRUCTED
    SleepResult.OTHER_PROBLEM -> SleepResultRecord.OTHER_PROBLEM
    SleepResult.NOT_SAFE -> SleepResultRecord.NOT_SAFE
}

fun Direction.asRecord() = when (this) {
    Direction.DOWN -> DirectionRecord.DOWN
    Direction.UP -> DirectionRecord.UP
    Direction.NORTH -> DirectionRecord.NORTH
    Direction.SOUTH -> DirectionRecord.SOUTH
    Direction.WEST -> DirectionRecord.WEST
    Direction.EAST -> DirectionRecord.EAST
}

fun toEntityRecord(source: DamageSource): DamageSourceRecord {
    val record = DamageSourceRecord(
        isUnblockable = source.isUnblockable,
        isDamageAllowedInCreativeMode = source.canHarmInCreative(),
        damageIsAbsolute = source.isDamageAbsolute,
        hungerDamage = source.hungerDamage,
        fireDamage = source.isFireDamage,
        projectile = source.isProjectile,
        difficultyScaled = source.isDifficultyScaled,
        magicDamage = source.isMagicDamage,
        explosion = source.isExplosion,
        damageType = source.damageType
    )

    return if (source is EntityDamageSource) {
        jp.ac.tsukuba.cs.iplab.catalyst.server.record.EntityDamageSource(
            damageSourceEntity = source.trueSource?.let {
                toEntityRecord(it)
            },
            isThornsDamage = source.isThornsDamage,
            damageSource = record
        )
    } else {
        record
    }
}

fun EquipmentSlotType.asRecord() = when (this) {
    EquipmentSlotType.MAINHAND -> LivingEvent.EquipmentSlotType.MAINHAND
    EquipmentSlotType.OFFHAND -> LivingEvent.EquipmentSlotType.OFFHAND
    EquipmentSlotType.HEAD -> LivingEvent.EquipmentSlotType.HEAD
    EquipmentSlotType.CHEST -> LivingEvent.EquipmentSlotType.CHEST
    EquipmentSlotType.LEGS -> LivingEvent.EquipmentSlotType.LEGS
    EquipmentSlotType.FEET -> LivingEvent.EquipmentSlotType.FEET
}

fun World.dimensionType() = dimensionKey.dimensionType()

fun RegistryKey<World>.dimensionType() = when (this) {
    World.OVERWORLD -> DimensionType.Overworld
    World.THE_NETHER -> DimensionType.Nether
    World.THE_END -> DimensionType.End
    else -> throw IllegalStateException("Unknown Dimension")
}

fun EffectInstance.asRecord() = EffectInstanceRecord(
    name = potion.name,
    duration = duration,
    amplifier = amplifier,
    ambient = isAmbient,
    showParticles = doesShowParticles(),
    showIcon = isShowIcon,
)

fun toEntityRecord(entity: Entity) = when (entity) {
    is PlayerEntity -> entity.asRecord()
    is LivingEntity -> entity.asRecord()
    is ItemEntity -> entity.asRecord()
    is ExperienceOrbEntity -> entity.asRecord()
    else -> entity.asRecord()
}

fun toLivingRecord(entity: LivingEntity) = when (entity) {
    is PlayerEntity -> entity.asRecord()
    else -> entity.asRecord()
}

fun Entity.asRecord() = EntityRecord(
    position = this.positionVec.asRecord(),
    motion = this.motion.asRecord(),
    rotation = RotationRecord(this.rotationYaw, this.rotationPitch),
    fallDistance = fallDistance,
    onGround = this.isOnGround,
    dimension = this.world.dimensionType(),
    ticksExisted = ticksExisted,
    fire = fireTimer,
    inWater = isInWater,
    inLava = isInLava,
    cachedUniqueIdString = cachedUniqueIdString,
    eyeHeight = eyeHeight,
    uuid = this.uniqueID.toString(),
    entityType = this.javaClass.canonicalName,
)

fun ItemEntity.asRecord() = ItemEntityRecord(
    thrower = throwerId?.toString(),
    owner = ownerId?.toString(),
    hoverStart = hoverStart,
    lifespan = lifespan,
    itemStack = item.asRecord(),
    entity = (this as Entity).asRecord(),
)

fun ExperienceOrbEntity.asRecord() = ExperienceOrbEntityRecord(
    xpColor = xpColor,
    xpOrbAge = xpOrbAge,
    delayBeforeCanPickup = delayBeforeCanPickup,
    xpValue = xpValue,
    entity = (this as Entity).asRecord(),
)

fun LivingEntity.asRecord() = LivingEntityRecord(
    health = health,
    isSwingInProgress = isSwingInProgress,
    swingingHand = swingingHand?.asRecord(),
    arrowHitTimer = arrowHitTimer,
    beeStingRemovalCooldown = beeStingRemovalCooldown,
    hurtTime = hurtTime,
    maxHurtTime = maxHurtTime,
    attackedAtYaw = attackedAtYaw,
    deathTime = deathTime,
    swingProgress = swingProgress,
    limbSwingAmount = limbSwingAmount,
    limbSwing = limbSwing,
    rotationYawHead = rotationYawHead,
    idleTime = idleTime,
    moveStrafing = moveStrafing,
    moveVertical = moveVertical,
    moveForward = moveForward,
    activeItemStack = activeItemStack.asRecord(),
    entity = (this as Entity).asRecord(),
)

fun PlayerEntity.asRecord() = PlayerEntityRecord(
    playerEntityUUID = cachedUniqueIdString,
    playerName = (name as StringTextComponent).text,
    inventory = inventory.asRecord(),
    cameraYaw = cameraYaw,
    foodStats = foodStats.asRecord(),
    sleepTimer = sleepTimer,
    livingEntity = (this as LivingEntity).asRecord(),
    experienceTotal = experienceTotal,
    experienceLevel = experienceLevel,
    experience = experience,
)