package jp.ac.tsukuba.cs.iplab.catalyst.client

import jp.ac.tsukuba.cs.iplab.catalyst.client.softwarekeyboard.Output
import kotlinx.serialization.Serializable

sealed interface BufferedTextWidgetOutput {
    data class Return(val output: Output) : BufferedTextWidgetOutput

    @Serializable
    data class SelectedCandidate(
        val candidate: String
    ) : KeyboardRecord(), BufferedTextWidgetOutput
}