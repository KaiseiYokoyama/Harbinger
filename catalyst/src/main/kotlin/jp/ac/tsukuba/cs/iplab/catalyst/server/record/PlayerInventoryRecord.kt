package jp.ac.tsukuba.cs.iplab.catalyst.server.record

import kotlinx.serialization.Serializable

@Serializable
data class PlayerInventoryRecord(
    val main: HashMap<Int, ItemStack>,
    val armour: HashMap<Int, ItemStack>,
    val offhand: ItemStack?,
    val currentItem: Int,
): jp.ac.tsukuba.cs.iplab.catalyst.Record()