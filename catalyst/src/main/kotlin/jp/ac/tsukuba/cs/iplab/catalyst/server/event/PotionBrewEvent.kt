package jp.ac.tsukuba.cs.iplab.catalyst.server.event

import jp.ac.tsukuba.cs.iplab.catalyst.server.record.ItemStack
import kotlinx.serialization.Serializable

/**
 * net.minecraftforge.event.brewing.PotionBrewEvent.Postに対応
 */
@Serializable
data class PotionBrewEvent(
    val stacks: List<ItemStack>
) : EventRecord()