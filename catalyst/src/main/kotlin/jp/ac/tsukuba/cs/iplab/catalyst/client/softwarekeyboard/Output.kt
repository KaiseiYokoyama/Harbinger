package jp.ac.tsukuba.cs.iplab.catalyst.client.softwarekeyboard

import jp.ac.tsukuba.cs.iplab.catalyst.client.KeyboardRecord
import jp.ac.tsukuba.cs.iplab.catalyst.client.kana.Consonant
import jp.ac.tsukuba.cs.iplab.catalyst.client.kana.KanaCharacter
import jp.ac.tsukuba.cs.iplab.catalyst.client.kana.Vowel
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * ソフトウェアキーボードの出力を表すクラス
 */
@Serializable
sealed class Output(
    @Transient open val repeated: Boolean = false,
): KeyboardRecord() {
    lateinit var keyboard: Keyboard

    sealed interface Executable

    /**
     * 文字の入力
     */
    @Serializable
    data class Char(
        val consonant: Consonant,
        val vowel: Vowel,
        val doubleFlicked: Boolean,
        override val repeated: Boolean = false,
    ) : Output(repeated), Executable {
        val char: KanaCharacter
            get() = KanaCharacter(consonant, vowel)
    }

    /**
     * 削除（特殊出力）
     */
    @Serializable
    data class Delete(
        override val repeated: Boolean = false
    ) : Output(repeated), Executable

    /**
     * 濁音・半濁音の付加・除去
     */
    @Serializable
    data class Modify(
        override val repeated: Boolean = false
    ) : Output(repeated), Executable

    /**
     * キャレットを左右に動かす
     */
    @Serializable
    data class CaretMove(
        val direction: Direction,
        override val repeated: Boolean = false,
    ) : Output(repeated), Executable {
        @Serializable
        enum class Direction { Left, Right }
    }

    sealed interface ConversionUpdate {
        /**
         * 変換の候補のカーソルを左右に動かす
         */
        @Serializable
        data class CursorMove(
            val direction: Direction,
            override val repeated: Boolean = false
        ) : Output(repeated), ConversionUpdate, Executable {
            @Serializable
            enum class Direction { Next, Prev }
        }

        @Serializable
        data class Select(
            override val repeated: Boolean = false
        ) : Output(repeated), ConversionUpdate, Executable
    }

    sealed interface RecordOnly {
        sealed interface KanaSyllabary {
            /**
             * 50音キーボード上のカーソルを動かした記録
             */
            @Serializable
            data class CursorMoveTo(
                val consonant: Consonant,
                val vowel: Vowel,
                val source: Source,
                val direction: Direction,
                override val repeated: Boolean = false,
            ) : Output(repeated), RecordOnly, KanaSyllabary

            /**
             * 50音キーボード上のカーソルを、予測変換に移した記録
             */
            @Serializable
            data class FocusOnCandidates(
               val source: Source,
               override val repeated: Boolean = false
            ) : Output(repeated), RecordOnly, KanaSyllabary

            /**
             * 50音キーボードの予測変換上のカーソルを、キーボードに戻した記録
             */
            @Serializable
            data class FocusOnKeyboard(
                val source: Source,
                override val repeated: Boolean = false
            ) : Output(repeated), RecordOnly, KanaSyllabary
        }

        sealed interface JoyFlick {
            /**
             * JoyFlickの子音を新たに選択した記録
             */
            @Serializable
            data class SelectConsonant(
                val consonant: Consonant,
            ) : Output(false), RecordOnly, JoyFlick

            /**
             * JoyFlickの母音を新たに選択した記録
             */
            @Serializable
            data class SelectVowel(
                val vowel: Vowel,
            ) : Output(false), RecordOnly, JoyFlick
        }
    }
}