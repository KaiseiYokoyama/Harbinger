package jp.ac.tsukuba.cs.iplab.catalyst.server

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.Binary
import java.time.Instant

/**
 * 実験終了直後に保存したワールドデータのzip、およびその取扱いに関する情報
 */
@Serializable
data class World(
    @SerialName("_id")
    val expID: String,
    @Contextual
    val date: Instant,
    @Contextual
    val zipBinary: Binary,
) {
    constructor(
        expID: String,
        binary: ByteArray,
    ) : this(
        expID,
        Instant.now(),
        Binary(binary),
    )
}