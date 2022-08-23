package jp.ac.tsukuba.cs.iplab.catalyst.server.record

import kotlinx.serialization.Serializable

@Serializable
sealed class ItemStack : jp.ac.tsukuba.cs.iplab.catalyst.Record() {
    @Serializable
    data class NotEmpty(
        /**
         * アイテムID。Registry.ITEM.getId(ItemStack.item)を使えばわかる。
         */
        val itemId: Int,
        /**
         * アイテムのリソースの位置。ex. "minecraft:stone"
         */
        val resourceLocation: String,
        val count: Int,
    ): ItemStack()

    @Serializable
    class Empty: ItemStack()
}