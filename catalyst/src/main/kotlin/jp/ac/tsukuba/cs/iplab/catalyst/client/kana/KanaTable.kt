package jp.ac.tsukuba.cs.iplab.catalyst.client.kana

/**
 * かな文字表
 */
object KanaTable {
    /**
     * コンパイラの網羅性検査を活かすため、Mapではなくwhen文を用いる
     */
    fun get(c: Consonant, v: Vowel) = when (c) {
        Consonant.A -> when (v) {
            Vowel.A -> 'あ'
            Vowel.I -> 'い'
            Vowel.U -> 'う'
            Vowel.E -> 'え'
            Vowel.O -> 'お'
        }
        Consonant.XA -> when (v) {
            Vowel.A -> 'ぁ'
            Vowel.I -> 'ぃ'
            Vowel.U -> 'ぅ'
            Vowel.E -> 'ぇ'
            Vowel.O -> 'ぉ'
        }
        Consonant.K -> when (v) {
            Vowel.A -> 'か'
            Vowel.I -> 'き'
            Vowel.U -> 'く'
            Vowel.E -> 'け'
            Vowel.O -> 'こ'
        }
        Consonant.G -> when (v) {
            Vowel.A -> 'が'
            Vowel.I -> 'ぎ'
            Vowel.U -> 'ぐ'
            Vowel.E -> 'げ'
            Vowel.O -> 'ご'
        }
        Consonant.S -> when(v) {
            Vowel.A -> 'さ'
            Vowel.I -> 'し'
            Vowel.U -> 'す'
            Vowel.E -> 'せ'
            Vowel.O -> 'そ'
        }
        Consonant.Z -> when(v) {
            Vowel.A -> 'ざ'
            Vowel.I -> 'じ'
            Vowel.U -> 'ず'
            Vowel.E -> 'ぜ'
            Vowel.O -> 'ぞ'
        }
        Consonant.T -> when(v) {
            Vowel.A -> 'た'
            Vowel.I -> 'ち'
            Vowel.U -> 'つ'
            Vowel.E -> 'て'
            Vowel.O -> 'と'
        }
        Consonant.D -> when(v) {
            Vowel.A -> 'だ'
            Vowel.I -> 'ぢ'
            Vowel.U -> 'づ'
            Vowel.E -> 'で'
            Vowel.O -> 'ど'
        }
        Consonant.XT -> when(v) {
            Vowel.U -> 'っ'
            Vowel.A,
            Vowel.I,
            Vowel.E,
            Vowel.O -> throw IllegalStateException("($c,$v) has no kana character")
        }
        Consonant.N -> when(v) {
            Vowel.A -> 'な'
            Vowel.I -> 'に'
            Vowel.U -> 'ぬ'
            Vowel.E -> 'ね'
            Vowel.O -> 'の'
        }
        Consonant.H -> when(v) {
            Vowel.A -> 'は'
            Vowel.I -> 'ひ'
            Vowel.U -> 'ふ'
            Vowel.E -> 'へ'
            Vowel.O -> 'ほ'
        }
        Consonant.B ->when(v) {
            Vowel.A -> 'ば'
            Vowel.I -> 'び'
            Vowel.U -> 'ぶ'
            Vowel.E -> 'べ'
            Vowel.O -> 'ぼ'
        }
        Consonant.P -> when(v) {
            Vowel.A -> 'ぱ'
            Vowel.I -> 'ぴ'
            Vowel.U -> 'ぷ'
            Vowel.E -> 'ぺ'
            Vowel.O -> 'ぽ'
        }
        Consonant.M -> when(v) {
            Vowel.A -> 'ま'
            Vowel.I -> 'み'
            Vowel.U -> 'む'
            Vowel.E -> 'め'
            Vowel.O -> 'も'
        }
        Consonant.Y -> when(v) {
            Vowel.A -> 'や'
            Vowel.I -> '！'
            Vowel.U -> 'ゆ'
            Vowel.E -> '？'
            Vowel.O -> 'よ'
        }
        Consonant.XY -> when(v) {
            Vowel.A -> 'ゃ'
            Vowel.I -> '（'
            Vowel.U -> 'ゅ'
            Vowel.E -> '）'
            Vowel.O -> 'ょ'
        }
        Consonant.R -> when(v) {
            Vowel.A -> 'ら'
            Vowel.I -> 'り'
            Vowel.U -> 'る'
            Vowel.E -> 'れ'
            Vowel.O -> 'ろ'
        }
        Consonant.W -> when(v) {
            Vowel.A -> 'わ'
            Vowel.I -> 'を'
            Vowel.U -> 'ん'
            Vowel.E -> 'ー'
            Vowel.O -> '～'
        }
        Consonant.XW -> when(v) {
            Vowel.A -> 'ゎ'
            Vowel.I,
            Vowel.U,
            Vowel.E,
            Vowel.O -> throw IllegalStateException("($c,$v) has no kana character")
        }
    }
}