package jp.ac.tsukuba.cs.iplab.catalyst.client

import jp.ac.tsukuba.cs.iplab.catalyst.client.softwarekeyboard.Keyboard
import kotlinx.serialization.Serializable

/**
 * 送信されたメッセージ
 */
@Serializable
data class Message(
    val message: String,
    val keyboard: Keyboard,
) : KeyboardRecord()