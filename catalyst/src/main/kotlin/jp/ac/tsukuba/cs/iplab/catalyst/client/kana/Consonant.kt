package jp.ac.tsukuba.cs.iplab.catalyst.client.kana

import kotlinx.serialization.Serializable

/**
 * 子音
 */
@Serializable
enum class Consonant {
    A, XA,
    K, G,
    S, Z,
    T, D, XT,
    N,
    H, B, P,
    M,
    Y, XY,
    R,
    W, XW;

    companion object {
        val monograph: Array<Consonant>
            get() = arrayOf(A, K, S, T, N, H, M, Y, R, W)
    }

    fun modified(): Consonant = when (this) {
        A -> XA
        XA -> A
        K -> G
        G -> K
        S -> Z
        Z -> S
        T -> D
        D -> XT
        XT -> T
        H -> B
        B -> P
        P -> H
        Y -> XY
        XY -> Y
        W -> XW
        XW -> W
        N, M, R -> this
    }
}