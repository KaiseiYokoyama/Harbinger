package jp.ac.tsukuba.cs.iplab.catalyst.client.kana

import kotlinx.serialization.Serializable

@Serializable
data class KanaCharacter(
    val consonant: Consonant,
    val vowel: Vowel,
) {
    fun toChar(): Char = KanaTable.get(consonant, vowel)

    fun modified(): KanaCharacter {
        val cons = when (this.consonant) {
            Consonant.D -> when (this.vowel) {
                Vowel.U -> consonant.modified() // づ -> っ
                Vowel.A, Vowel.I, Vowel.E, Vowel.O -> Consonant.T // ex. だ -> た
            }
            Consonant.W -> when (this.vowel) {
                Vowel.A -> consonant.modified() // わ -> ゎ
                Vowel.I, Vowel.U, Vowel.E, Vowel.O -> Consonant.W // ex. を -> を
            }
            Consonant.A, Consonant.XA,
            Consonant.K, Consonant.G,
            Consonant.S, Consonant.Z,
            Consonant.T, Consonant.XT,
            Consonant.N,
            Consonant.H, Consonant.B, Consonant.P,
            Consonant.M,
            Consonant.Y, Consonant.XY,
            Consonant.R,
            Consonant.XW -> this.consonant.modified()
        }

        return KanaCharacter(cons, this.vowel)
    }
}