package jp.ac.tsukuba.cs.iplab.catalyst.server.event

import jp.ac.tsukuba.cs.iplab.catalyst.server.record.PlayerEntity
import kotlinx.serialization.Serializable

@Serializable
data class ServerChatEvent(
    val message: String,
    val userName: String,
    val playerEntity: PlayerEntity,
    /**
     * ServerChatEvent.component.getUnformattedComponentText()
     */
    val componentString: String,
) : EventRecord()