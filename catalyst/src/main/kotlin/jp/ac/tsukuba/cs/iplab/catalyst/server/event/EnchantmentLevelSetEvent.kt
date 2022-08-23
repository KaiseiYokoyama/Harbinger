package jp.ac.tsukuba.cs.iplab.catalyst.server.event

import jp.ac.tsukuba.cs.iplab.catalyst.server.Vector3
import jp.ac.tsukuba.cs.iplab.catalyst.server.record.DimensionType
import jp.ac.tsukuba.cs.iplab.catalyst.server.record.ItemStack
import kotlinx.serialization.Serializable

/**
 * エンチャント候補の設定時
 */
@Serializable
data class EnchantmentLevelSetEvent(
    val dimension: DimensionType,
    val pos: Vector3<Int>,
    val enchantRow: Int,
    /**
     * エンチャントテーブルを囲む本棚の数
     */
    val power: Int,
    val itemStack: ItemStack,
    val originalLevel: Int,
    val level: Int,
) : EventRecord()