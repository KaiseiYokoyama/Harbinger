package jp.ac.tsukuba.cs.iplab.harbinger.prelude

import jp.ac.tsukuba.cs.iplab.catalyst.db.DBWriter
import jp.ac.tsukuba.cs.iplab.catalyst.server.Vector3
import jp.ac.tsukuba.cs.iplab.catalyst.server.event.Block
import jp.ac.tsukuba.cs.iplab.catalyst.server.event.Chunk
import jp.ac.tsukuba.cs.iplab.catalyst.server.record.ItemStack
import net.minecraft.entity.monster.ZombieEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraftforge.event.AnvilUpdateEvent
import net.minecraftforge.event.ServerChatEvent
import net.minecraftforge.event.brewing.PlayerBrewedPotionEvent
import net.minecraftforge.event.brewing.PotionBrewEvent
import net.minecraftforge.event.enchanting.EnchantmentLevelSetEvent
import net.minecraftforge.event.entity.EntityEvent
import net.minecraftforge.event.entity.EntityMountEvent
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent
import net.minecraftforge.event.entity.ProjectileImpactEvent
import net.minecraftforge.event.entity.item.ItemEvent
import net.minecraftforge.event.entity.item.ItemExpireEvent
import net.minecraftforge.event.entity.item.ItemTossEvent
import net.minecraftforge.event.entity.living.*
import net.minecraftforge.event.entity.living.PotionEvent.PotionAddedEvent
import net.minecraftforge.event.entity.living.PotionEvent.PotionExpiryEvent
import net.minecraftforge.event.entity.living.PotionEvent.PotionRemoveEvent
import net.minecraftforge.event.entity.player.AdvancementEvent
import net.minecraftforge.event.entity.player.AnvilRepairEvent
import net.minecraftforge.event.entity.player.ArrowLooseEvent
import net.minecraftforge.event.entity.player.ArrowNockEvent
import net.minecraftforge.event.entity.player.AttackEntityEvent
import net.minecraftforge.event.entity.player.BonemealEvent
import net.minecraftforge.event.entity.player.CriticalHitEvent
import net.minecraftforge.event.entity.player.FillBucketEvent
import net.minecraftforge.event.entity.player.ItemFishedEvent
import net.minecraftforge.event.entity.player.PlayerContainerEvent
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.event.entity.player.PlayerEvent.Clone
import net.minecraftforge.event.entity.player.PlayerEvent.HarvestCheck
import net.minecraftforge.event.entity.player.PlayerEvent.ItemCraftedEvent
import net.minecraftforge.event.entity.player.PlayerEvent.ItemPickupEvent
import net.minecraftforge.event.entity.player.PlayerEvent.ItemSmeltedEvent
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.event.entity.player.PlayerSetSpawnEvent
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent
import net.minecraftforge.event.entity.player.PlayerXpEvent
import net.minecraftforge.event.entity.player.SleepingLocationCheckEvent
import net.minecraftforge.event.entity.player.SleepingTimeCheckEvent
import net.minecraftforge.event.village.VillageSiegeEvent
import net.minecraftforge.event.world.*
import net.minecraftforge.eventbus.api.Event
import net.minecraftforge.eventbus.api.SubscribeEvent

class EventRecorder(
    val dbWriter: DBWriter
) {
    @SubscribeEvent
    fun record(event: Event) {
        when (event) {
            is AnvilUpdateEvent -> {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.AnvilUpdateEvent(
                    left = event.left.asRecord(),
                    right = event.right.asRecord(),
                    name = event.name,
                    output = event.output.asRecord(),
                    cost = event.cost,
                    materialCost = event.materialCost,
                )
                dbWriter.record(record)
            }
            is BabyEntitySpawnEvent -> {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.BabyEntitySpawnEvent(
                    parentA = toLivingRecord(event.parentA),
                    parentB = toLivingRecord(event.parentB),
                    child = event.child?.let { toLivingRecord(it) },
                    player = event.causedByPlayer?.let { it.asRecord() },
                )
                dbWriter.record(record)
            }
            is EnchantmentLevelSetEvent -> {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.EnchantmentLevelSetEvent(
                    dimension = event.world.dimensionType(),
                    pos = event.pos.asRecord(),
                    enchantRow = event.enchantRow,
                    power = event.power,
                    itemStack = event.item.asRecord(),
                    originalLevel = event.originalLevel,
                    level = event.level,
                )
                dbWriter.record(record)
            }
            is PotionBrewEvent -> {
                val stacks = mutableListOf<ItemStack>()
                for (i in 0..event.length) {
                    stacks.add(event.getItem(i).asRecord())
                }

                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.PotionBrewEvent(
                    stacks = stacks
                )
                dbWriter.record(record)
            }
            is ServerChatEvent -> {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.ServerChatEvent(
                    message = event.message,
                    userName = event.username,
                    playerEntity = event.player.asRecord(),
                    componentString = event.component.unformattedComponentText,
                )
                dbWriter.record(record)
            }
            is VillageSiegeEvent -> {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.VillageSiegeEvent(
                    playerEntity = event.player.asRecord(),
                    spawnPos = event.attemptedSpawnPos.asRecord(),
                )
                dbWriter.record(record)
            }
            is BlockEvent -> blockEvent(event)
            is ChunkWatchEvent -> chunkWatchEvent(event)
            is EntityEvent -> entityEvent(event)
            is WorldEvent -> worldEvent(event)
        }
    }

    private fun worldEvent(event: WorldEvent) {
        when (event) {
            is SaplingGrowTreeEvent -> {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.WorldEvent.SaplingGrowTreeEvent(
                    pos = event.pos.asRecord()
                )
                dbWriter.record(record)
            }
            is SleepFinishedTimeEvent -> {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.WorldEvent.SleepFinishedTimeEvent(
                    newTime = event.newTime
                )
                dbWriter.record(record)
            }
        }
    }

    private fun entityEvent(event: EntityEvent) {
        when (event) {
            is EntityMountEvent -> {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.EntityEvent.MountEvent(
                    mounting = toEntityRecord(event.entityMounting),
                    mounted = toEntityRecord(event.entityBeingMounted),
                    isMounting = event.isMounting,
                )
                dbWriter.record(record)
            }
            is EntityTravelToDimensionEvent -> {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.EntityEvent.TravelToDimensionEvent(
                    entity = toEntityRecord(event.entity),
                    dimension = event.dimension.dimensionType(),
                )
                dbWriter.record(record)
            }
            is ItemEvent -> itemEvent(event)
            is LivingEvent -> livingEvent(event)
            is ProjectileImpactEvent -> projectileImpactEvent(event)
        }
    }

    private fun livingEvent(event: LivingEvent) {
        when (event) {
            is AnimalTameEvent -> {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.LivingEvent.AnimalTameEvent(
                    animal = toEntityRecord(event.animal),
                    player = event.tamer.asRecord(),
                )
                dbWriter.record(record)
            }
            is EnderTeleportEvent -> if (event.entityLiving is PlayerEntity) {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.LivingEvent.EnderTeleportEvent(
                    target = Vector3(
                        x = event.targetX,
                        y = event.targetY,
                        z = event.targetZ,
                    ),
                    damage = event.attackDamage,
                    playerEntity = (event.entityLiving as PlayerEntity).asRecord(),
                )
                dbWriter.record(record)
            }
            is LivingDamageEvent -> {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.LivingEvent.DamagedEvent(
                    source = toEntityRecord(event.source),
                    amount = event.amount,
                    livingEntity = toLivingRecord(event.entityLiving),
                )
                dbWriter.record(record)
            }
            is LivingHurtEvent -> {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.LivingEvent.HurtEvent(
                    source = toEntityRecord(event.source),
                    amount = event.amount,
                    livingEntity = toLivingRecord(event.entityLiving),
                )
                dbWriter.record(record)
            }
            is LivingDeathEvent -> {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.LivingEvent.DeathEvent(
                    source = toEntityRecord(event.source),
                    livingEntity = toLivingRecord(event.entityLiving),
                )
                dbWriter.record(record)
            }
            is LivingDestroyBlockEvent -> {
                // 何故かゾンビが0,0,0にあるブロックを壊そうとするイベントがかなり大量に発生する
                // ので、そのイベントを無視する
                if (event.entityLiving is ZombieEntity && event.pos == BlockPos.ZERO) return

                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.LivingEvent.DestroyBlockEvent(
                    blockPos = event.pos.asRecord(),
                    state = event.state.block.javaClass.canonicalName,
                    livingEntity = toLivingRecord(event.entityLiving),
                )
                dbWriter.record(record)
            }
            is LivingDropsEvent -> {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.LivingEvent.DropsEvent(
                    source = toEntityRecord(event.source),
                    drops = event.drops.map { it.asRecord() }.toSet(),
                    lootingLevel = event.lootingLevel,
                    recentlyHit = event.isRecentlyHit,
                    livingEntity = toLivingRecord(event.entityLiving),
                )
                dbWriter.record(record)
            }
            is LivingEntityUseItemEvent -> {
                val item = event.item.asRecord()
                val duration = event.duration

                when (event) {
                    is LivingEntityUseItemEvent.Start -> {
                        val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.LivingEvent.UseItemEvent.Start(
                            item = item,
                            duration = duration,
                            livingEntity = toLivingRecord(event.entityLiving),
                        )
                        dbWriter.record(record)
                    }
                    is LivingEntityUseItemEvent.Stop -> {
                        val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.LivingEvent.UseItemEvent.Stop(
                            item = item,
                            duration = duration,
                            livingEntity = toLivingRecord(event.entityLiving),
                        )
                        dbWriter.record(record)
                    }
                    is LivingEntityUseItemEvent.Finish -> {
                        val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.LivingEvent.UseItemEvent.Finish(
                            item = item,
                            duration = duration,
                            livingEntity = toLivingRecord(event.entityLiving),
                        )
                        dbWriter.record(record)
                    }
                    is LivingEntityUseItemEvent.Tick -> {
                        // ignore
                    }
                }
            }
            is LivingEquipmentChangeEvent -> {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.LivingEvent.EquipmentChangeEvent(
                    slot = event.slot.asRecord(),
                    from = event.from.asRecord(),
                    to = event.to.asRecord(),
                    livingEntity = toLivingRecord(event.entityLiving),
                )
                dbWriter.record(record)
            }
            is LivingExperienceDropEvent -> {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.LivingEvent.ExperienceDropEvent(
                    attackingPlayer = event.attackingPlayer?.asRecord(),
                    originalExperiencePoints = event.originalExperience,
                    livingEntity = toLivingRecord(event.entityLiving),
                )
                dbWriter.record(record)
            }
            is LivingFallEvent -> {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.LivingEvent.FallEvent(
                    distance = event.distance,
                    damageMultiplier = event.damageMultiplier,
                    livingEntity = toLivingRecord(event.entityLiving),
                )
                dbWriter.record(record)
            }
            is LivingHealEvent -> {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.LivingEvent.HealEvent(
                    amount = event.amount,
                    livingEntity = toLivingRecord(event.entityLiving),
                )
                dbWriter.record(record)
            }
            is LivingKnockBackEvent -> {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.LivingEvent.KnockBackedEvent(
                    strength = event.strength,
                    ratioX = event.ratioX,
                    ratioZ = event.ratioZ,
                    originalStrength = event.originalStrength,
                    originalRatioX = event.originalRatioX,
                    originalRatioZ = event.originalRatioZ,
                    livingEntity = toLivingRecord(event.entityLiving),
                )
                dbWriter.record(record)
            }
            is PotionEvent -> {
                val effect = event.potionEffect?.asRecord()
                val livingEntity = toLivingRecord(event.entityLiving)

                when (event) {
                    is PotionAddedEvent -> {
                        val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.LivingEvent.Potion.AddedEvent(
                            effect = effect,
                            oldEffect = event.oldPotionEffect?.asRecord(),
                            livingEntity = livingEntity,
                        )
                        dbWriter.record(record)
                    }
                    is PotionRemoveEvent -> {
                        val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.LivingEvent.Potion.RemoveEvent(
                            effect = effect,
                            potion = event.potion.name,
                            livingEntity = livingEntity,
                        )
                        dbWriter.record(record)
                    }
                    is PotionExpiryEvent -> {
                        val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.LivingEvent.Potion.ExpiryEvent(
                            effect = effect,
                            livingEntity = livingEntity,
                        )
                        dbWriter.record(record)
                    }
                }
            }
            is SleepingLocationCheckEvent -> {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.LivingEvent.SleepingLocationCheckEvent(
                    location = event.sleepingLocation.asRecord(),
                    livingEntity = toLivingRecord(event.entityLiving),
                )
                dbWriter.record(record)
            }
            is PlayerEvent -> playerEvent(event)
        }
    }

    private fun playerEvent(event: PlayerEvent) {
        val player = event.player.asRecord()
        when(event) {
            is PlayerLoggedInEvent -> {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.PlayerEvent.LoggedInEvent(
                    playerEntity = player
                )
                dbWriter.record(record)
            }
            is PlayerLoggedOutEvent -> {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.PlayerEvent.LoggedOutEvent(
                    playerEntity = player
                )
                dbWriter.record(record)
            }
            is PlayerRespawnEvent -> {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.PlayerEvent.RespawnEvent(
                    endConquered = event.isEndConquered,
                    playerEntity = player,
                )
                dbWriter.record(record)
            }
            is PlayerWakeUpEvent -> {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.PlayerEvent.WakeUpEvent(
                    wakeImmediately = event.wakeImmediately(),
                    updateWorld = event.updateWorld(),
                    playerEntity = player,
                )
                dbWriter.record(record)
            }
            is PlayerSleepInBedEvent -> {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.PlayerEvent.SleepInBedEvent(
                    result = event.resultStatus.asRecord(),
                    playerEntity = player,
                )
                dbWriter.record(record)
            }
            is SleepingTimeCheckEvent -> {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.PlayerEvent.SleepingTimeCheckEvent(
                    location = event.sleepingLocation.map { it.asRecord() }.orElse(null),
                    playerEntity = player,
                )
                dbWriter.record(record)
            }
            is PlayerSetSpawnEvent -> {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.PlayerEvent.SetSpawnEvent(
                    forced = event.isForced,
                    position = event.newSpawn?.asRecord(),
                    playerEntity = player,
                )
                dbWriter.record(record)
            }
            is ArrowNockEvent -> {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.PlayerEvent.ArrowNockEvent(
                    bow = event.bow.asRecord(),
                    hand = event.hand.asRecord(),
                    hasAmmo = event.hasAmmo(),
                    playerEntity = player,
                )
                dbWriter.record(record)
            }
            is ArrowLooseEvent -> {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.PlayerEvent.ArrowLooseEvent(
                    bow = event.bow.asRecord(),
                    hasAmmo = event.hasAmmo(),
                    charge = event.charge,
                    playerEntity = player,
                )
                dbWriter.record(record)
            }
            is ItemSmeltedEvent -> {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.PlayerEvent.ItemSmeltedEvent(
                    smelting = event.smelting.asRecord(),
                    playerEntity = player,
                )
                dbWriter.record(record)
            }
            // クライアント側のみだった
//            is ItemTooltipEvent -> {}
            is Clone -> {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.PlayerEvent.Clone(
                    playerEntity = player,
                )
                dbWriter.record(record)
            }
            is AnvilRepairEvent -> {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.PlayerEvent.AnvilRepairEvent(
                    item = event.itemInput.asRecord(),
                    ingredient = event.ingredientInput.asRecord(),
                    output = event.itemResult.asRecord(),
                    breakChance = event.breakChance,
                    playerEntity = player
                )
                dbWriter.record(record)
            }
            is AdvancementEvent -> {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.PlayerEvent.AdvancementEvent(
                    advancementId = event.advancement.id.toString(),
                    playerEntity = player,
                )
                dbWriter.record(record)
            }
            is PlayerXpEvent -> {
                when(event) {
                    is PlayerXpEvent.PickupXp -> {
                        val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.PlayerEvent.XpEvent.PickupXp(
                            orb = event.orb.asRecord(),
                            playerEntity = player,
                        )
                        dbWriter.record(record)
                    }
                    is PlayerXpEvent.XpChange -> {
                        val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.PlayerEvent.XpEvent.XpChange(
                            amount = event.amount,
                            playerEntity = player,
                        )
                        dbWriter.record(record)
                    }
                    is PlayerXpEvent.LevelChange -> {
                        val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.PlayerEvent.XpEvent.LevelChange(
                            levels = event.levels,
                            playerEntity = player,
                        )
                        dbWriter.record(record)
                    }
                }
            }
            is PlayerDestroyItemEvent -> {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.PlayerEvent.DestroyItemEvent(
                    original = event.original.asRecord(),
                    hand = event.hand?.asRecord(),
                    playerEntity = player,
                )
                dbWriter.record(record)
            }
            is FillBucketEvent -> {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.PlayerEvent.FillBucketEvent(
                    empty = event.emptyBucket.asRecord(),
                    filled = event.filledBucket.asRecord(),
                    playerEntity = player,
                )
                dbWriter.record(record)
            }
            is BonemealEvent -> {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.PlayerEvent.BonemealEvent(
                    stack = event.stack.asRecord(),
                    blockPos = event.pos.asRecord(),
                    playerEntity = player,
                )
                dbWriter.record(record)
            }
            is PlayerInteractEvent -> {
                val hand = event.hand.asRecord()
                val pos = event.pos.asRecord()
                val face = event.face?.asRecord()

                when(event) {
                    is PlayerInteractEvent.RightClickBlock -> {
                        val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.PlayerEvent.Interact.RightClickBlock(
                            hand = hand,
                            pos = pos,
                            face = face,
                            playerEntity = player,
                        )
                        dbWriter.record(record)
                    }
                    is PlayerInteractEvent.RightClickItem -> {
                        val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.PlayerEvent.Interact.RightClickItem(
                            hand = hand,
                            pos = pos,
                            face = face,
                            playerEntity = player,
                        )
                        dbWriter.record(record)
                    }
                    is PlayerInteractEvent.RightClickEmpty -> {
                        val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.PlayerEvent.Interact.RightClickEmpty(
                            hand = hand,
                            pos = pos,
                            face = face,
                            playerEntity = player,
                        )
                        dbWriter.record(record)
                    }
                    is PlayerInteractEvent.LeftClickBlock -> {
                        val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.PlayerEvent.Interact.LeftClickBlock(
                            hand = hand,
                            pos = pos,
                            face = face,
                            playerEntity = player,
                        )
                        dbWriter.record(record)
                    }
                    is PlayerInteractEvent.LeftClickEmpty -> {
                        val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.PlayerEvent.Interact.LeftClickEmpty(
                            hand = hand,
                            pos = pos,
                            face = face,
                            playerEntity = player,
                        )
                        dbWriter.record(record)
                    }
                    is PlayerInteractEvent.EntityInteract -> {
                        val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.PlayerEvent.Interact.EntityInteract(
                            target = toEntityRecord(event.target),
                            hand = hand,
                            pos = pos,
                            face = face,
                            playerEntity = player,
                        )
                        dbWriter.record(record)
                    }
                    is PlayerInteractEvent.EntityInteractSpecific -> {
                        val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.PlayerEvent.Interact.EntityInteractSpecific(
                            localPos = event.localPos.asRecord(),
                            target = toEntityRecord(event.target),
                            hand = hand,
                            pos = pos,
                            face = face,
                            playerEntity = player,
                        )
                        dbWriter.record(record)
                    }
                }
            }
            is ItemPickupEvent -> {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.PlayerEvent.ItemPickupEvent(
                    entity = event.originalEntity.asRecord(),
                    stack = event.stack.asRecord(),
                    playerEntity = player
                )
                dbWriter.record(record)
            }
            is HarvestCheck -> {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.PlayerEvent.HarvestCheck(
                    success = event.canHarvest(),
                    block = event.targetBlock.block.javaClass.canonicalName,
                    playerEntity = player,
                )
                dbWriter.record(record)
            }
            is PlayerChangedDimensionEvent -> {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.PlayerEvent.ChangedDimensionEvent(
                    from = event.from.dimensionType(),
                    to = event.from.dimensionType(),
                    playerEntity = player,
                )
                dbWriter.record(record)
            }
            is PlayerContainerEvent -> {
                val container = event.container.asRecord()

                when(event) {
                    is PlayerContainerEvent.Open -> {
                        val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.PlayerEvent.ContainerEvent.Open(
                            container = container,
                            playerEntity = player,
                        )
                        dbWriter.record(record)
                    }
                    is PlayerContainerEvent.Close -> {
                        val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.PlayerEvent.ContainerEvent.Close(
                            container = container,
                            playerEntity = player,
                        )
                        dbWriter.record(record)
                    }
                }
            }
            is CriticalHitEvent -> {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.PlayerEvent.CriticalHitEvent(
                    damageModifier = event.damageModifier,
                    oldDamageModifier = event.oldDamageModifier,
                    target = toEntityRecord(event.target),
                    vanillaCritical = event.isVanillaCritical,
                    playerEntity = player,
                )
                dbWriter.record(record)
            }
            is ItemFishedEvent -> {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.PlayerEvent.ItemFishedEvent(
                    stacks = event.drops.asHashMap(),
                    rodDamage = event.rodDamage,
                    playerEntity = player,
                )
                dbWriter.record(record)
            }
            is ItemCraftedEvent -> {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.PlayerEvent.ItemCraftedEvent(
                    crafting = event.crafting.asRecord(),
                    craftMatrix = event.inventory.asHashMap(),
                    playerEntity = player
                )
                dbWriter.record(record)
            }
            is AttackEntityEvent -> {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.PlayerEvent.AttackEntityEvent(
                    target = toEntityRecord(event.target),
                    playerEntity = player,
                )
                dbWriter.record(record)
            }
            is PlayerBrewedPotionEvent -> {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.PlayerEvent.BrewedPotionEvent(
                    stack = event.stack.asRecord(),
                    playerEntity = player,
                )
                dbWriter.record(record)
            }
        }
    }

    private fun itemEvent(event: ItemEvent) {
        when (event) {
            is ItemExpireEvent -> {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.ItemEvent.ItemExpireEvent(
                    item = event.entityItem.asRecord(),
                    extraLive = event.extraLife,
                )
                dbWriter.record(record)
            }
            is ItemTossEvent -> {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.ItemEvent.ItemTossEvent(
                    item = event.entityItem.asRecord(),
                    playerEntity = event.player.asRecord(),
                )
                dbWriter.record(record)
            }
        }
    }

    private fun projectileImpactEvent(event: ProjectileImpactEvent) {
        val ray = event.rayTraceResult.hitVec.asRecord()
        when (event) {
            is ProjectileImpactEvent.Arrow -> {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.EntityEvent.ProjectileImpact.Arrow(
                    ray = ray
                )
                dbWriter.record(record)
            }
            is ProjectileImpactEvent.Fireball -> {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.EntityEvent.ProjectileImpact.Fireball(
                    ray = ray
                )
                dbWriter.record(record)
            }
            is ProjectileImpactEvent.Throwable -> {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.EntityEvent.ProjectileImpact.Throwable(
                    ray = ray,
                    throwable = toEntityRecord(event.throwable),
                )
                dbWriter.record(record)
            }
            is ProjectileImpactEvent.FireworkRocket -> {
                val record = jp.ac.tsukuba.cs.iplab.catalyst.server.event.EntityEvent.ProjectileImpact.FireworkRocket(
                    ray = ray,
                    rocket = toEntityRecord(event.fireworkRocket),
                )
                dbWriter.record(record)
            }
        }
    }

    private fun chunkWatchEvent(event: ChunkWatchEvent) {
        when (event) {
            is ChunkWatchEvent.Watch -> {
                val record = Chunk.Watch(
                    player = event.player.asRecord(),
                    pos = event.pos.asRecord(),
                    dimension = event.world.dimensionType(),
                )
                dbWriter.record(record)
            }
            is ChunkWatchEvent.UnWatch -> {
                val record = Chunk.UnWatch(
                    player = event.player.asRecord(),
                    pos = event.pos.asRecord(),
                    dimension = event.world.dimensionType(),
                )
                dbWriter.record(record)
            }
        }
    }

    private fun blockEvent(event: BlockEvent) {
        val blockPos = event.pos.asRecord()
        val state = event.state.block.javaClass.canonicalName

        when (event) {
            is BlockEvent.BlockToolInteractEvent -> {
                val record = Block.BlockToolInteractEvent(
                    blockPos = blockPos,
                    state = state,
                    itemStack = event.heldItemStack.asRecord(),
                    toolType = event.toolType.name,
                    playerEntity = event.player.asRecord(),
                )
                dbWriter.record(record)
            }
            is BlockEvent.BreakEvent -> {
                val record = Block.BreakEvent(
                    blockPos = blockPos,
                    state = state,
                    exp = event.expToDrop,
                    playerEntity = event.player.asRecord(),
                )
                dbWriter.record(record)
            }
            is BlockEvent.CropGrowEvent.Post -> {
                val record = Block.CropGrownEvent(
                    blockPos = blockPos,
                    state = state,
                    originalState = event.originalState.block.javaClass.canonicalName,
                )
                dbWriter.record(record)
            }
            is BlockEvent.EntityPlaceEvent -> when (event.entity) {
                is PlayerEntity -> {
                    val record = Block.PlayerPlaceEvent(
                        blockPos = blockPos,
                        state = state,
                        placedBlock = event.placedBlock.block.javaClass.canonicalName,
                        placedAgainst = event.placedAgainst.block.javaClass.canonicalName,
                        playerEntity = (event.entity as PlayerEntity).asRecord(),
                    )
                    dbWriter.record(record)
                }
            }
            is BlockEvent.FarmlandTrampleEvent -> {
                val record = Block.FarmlandTrampleEvent(
                    blockPos = blockPos,
                    state = state,
                    entity = toEntityRecord(event.entity),
                    fallDistance = event.fallDistance,
                )
                dbWriter.record(record)
            }
            is BlockEvent.PortalSpawnEvent -> {
                val record = Block.PortalSpawnEvent(
                    blockPos = blockPos,
                    state = state,
                )
                dbWriter.record(record)
            }
            else -> {}
        }
    }
}