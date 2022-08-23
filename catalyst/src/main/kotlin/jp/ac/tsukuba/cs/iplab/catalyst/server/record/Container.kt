package jp.ac.tsukuba.cs.iplab.catalyst.server.record

import kotlinx.serialization.Serializable

@Serializable
data class Container(
    val items: HashMap<Int, ItemStack>,
    /**
     * Container.javaClass.getCanonicalName()
     */
    val type: String,
)