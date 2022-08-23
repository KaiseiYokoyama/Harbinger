package jp.ac.tsukuba.cs.iplab.catalyst.client.softwarekeyboard

import jp.ac.tsukuba.cs.iplab.catalyst.client.KeyboardRecord
import kotlinx.serialization.Serializable

@Serializable
enum class Keyboard {
    JoyFlick, KanaSyllabary, Kai;

    @Serializable
    data class Open(
        val keyboard: Keyboard
    ) : KeyboardRecord()

    @Serializable
    data class Switch(
        val from: Keyboard,
        val to: Keyboard,
    ) : KeyboardRecord()

    @Serializable
    data class Close(
        val keyboard: Keyboard
    ) : KeyboardRecord()
}