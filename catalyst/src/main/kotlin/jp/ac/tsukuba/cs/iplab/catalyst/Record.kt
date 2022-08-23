package jp.ac.tsukuba.cs.iplab.catalyst

import kotlinx.datetime.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
abstract class Record(
    /**
     * 発生または測定の時間
     */
    var date: Instant,
    @Contextual
    var timeStamp: java.time.Instant?,
    /**
     * 実験ID
     * NOTE: レコードをデータベースに書き込む前に、この値を設定するのを忘れないこと
     */
    var expID: String,
) {
//    /**
//     * 個人識別子（恐らくメールアドレス）
//     */
//    @Indexed("identity")

//    lateinit var identity: String

    constructor() : this(kotlinx.datetime.Clock.System.now(), null, "")
}