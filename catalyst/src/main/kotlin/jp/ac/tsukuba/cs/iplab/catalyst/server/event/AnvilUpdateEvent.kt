package jp.ac.tsukuba.cs.iplab.catalyst.server.event

import jp.ac.tsukuba.cs.iplab.catalyst.server.record.ItemStack
import kotlinx.serialization.Serializable

/**
 * 鉄床の左右のスロットにアイテムを配置したとき
 */
@Serializable
data class AnvilUpdateEvent(
    val left: ItemStack,
    val right: ItemStack,
    val name: String?,
    val output: ItemStack,
    val cost: Int,
    val materialCost: Int,
): EventRecord()
