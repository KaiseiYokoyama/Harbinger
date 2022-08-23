package jp.ac.tsukuba.cs.iplab.catalyst.client

import jp.ac.tsukuba.cs.iplab.catalyst.Record
import jp.ac.tsukuba.cs.iplab.catalyst.client.softwarekeyboard.Keyboard
import jp.ac.tsukuba.cs.iplab.catalyst.client.softwarekeyboard.Output
import jp.ac.tsukuba.cs.iplab.catalyst.server.record.PlayerEntity
import jp.ac.tsukuba.cs.iplab.catalyst.client.BufferedTextWidgetOutput as BOutput
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

/**
 * キーボードの操作に関するレコード
 */
@Serializable
abstract class KeyboardRecord(
    /**
     * どのプレイヤーの記録なのかを示すためのプロパティ
     * サーバがこのイベントを受け取ったとき、このプロパティに適切な値を設定する
     *
     * ref. net.minecraftforge.fml.network.NetworkEvent.Context.sender
     */
    var player: PlayerEntity?
) : Record() {
    constructor(): this(null)
    companion object {
        val serializersModule: SerializersModule
            get() = SerializersModule {
                polymorphic(KeyboardRecord::class) {
                    subclass(Message::class, Message.serializer())
                    subclass(Keyboard.Open::class, Keyboard.Open.serializer())
                    subclass(Keyboard.Switch::class, Keyboard.Switch.serializer())
                    subclass(Keyboard.Close::class, Keyboard.Close.serializer())
                    subclass(Output.Char::class, Output.Char.serializer())
                    subclass(Output.Delete::class, Output.Delete.serializer())
                    subclass(Output.Modify::class, Output.Modify.serializer())
                    subclass(Output.CaretMove::class, Output.CaretMove.serializer())
                    subclass(Output.ConversionUpdate.CursorMove::class, Output.ConversionUpdate.CursorMove.serializer())
                    subclass(Output.ConversionUpdate.Select::class, Output.ConversionUpdate.Select.serializer())
                    subclass(
                        Output.RecordOnly.KanaSyllabary.CursorMoveTo::class,
                        Output.RecordOnly.KanaSyllabary.CursorMoveTo.serializer()
                    )
                    subclass(
                        Output.RecordOnly.KanaSyllabary.FocusOnCandidates::class,
                        Output.RecordOnly.KanaSyllabary.FocusOnCandidates.serializer()
                    )
                    subclass(
                        Output.RecordOnly.KanaSyllabary.FocusOnKeyboard::class,
                        Output.RecordOnly.KanaSyllabary.FocusOnKeyboard.serializer()
                    )
                    subclass(
                        Output.RecordOnly.JoyFlick.SelectConsonant::class,
                        Output.RecordOnly.JoyFlick.SelectConsonant.serializer()
                    )
                    subclass(
                        Output.RecordOnly.JoyFlick.SelectVowel::class,
                        Output.RecordOnly.JoyFlick.SelectVowel.serializer()
                    )
                    subclass(
                        BOutput.SelectedCandidate::class,
                        BOutput.SelectedCandidate.serializer(),
                    )
                }
            }
    }
}