package jp.ac.tsukuba.cs.iplab.catalyst.db

import jp.ac.tsukuba.cs.iplab.catalyst.Record
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.toList
import org.litote.kmongo.eq
import org.litote.kmongo.gte
import org.litote.kmongo.lt
import org.litote.kmongo.reactivestreams.KMongo
import org.litote.kmongo.reactivestreams.getCollection
import org.litote.kmongo.util.KMongoUtil
import java.nio.file.Path
import kotlin.io.path.readText
import kotlin.reflect.KClass
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

open class DBDriver(
    val dbConfig: DBConfig,
) {
    constructor(path: Path) : this(
        Json.decodeFromString<DBConfig>(path.readText())
    )

    val client = KMongo.createClient(dbConfig.connectionString)
    val database = client.getDatabase(dbConfig.dbName)

    suspend fun expList() = database.getCollection<DBWriter.ExpConfig>().find().toList()

    suspend fun <R : Record> read(clazz: KClass<R>, expID: String) = database.getCollection(
        KMongoUtil.defaultCollectionName(clazz),
        clazz.java,
    ).find(
        Record::expID eq expID
    ).toList()

    @OptIn(ExperimentalTime::class)
    suspend fun <R : Record> readDuring(clazz: KClass<R>, expID: String, from: Instant, duration: Duration) =
        database.getCollection(
            KMongoUtil.defaultCollectionName(clazz),
            clazz.java,
        ).find(
            and(
                Record::expID eq expID,
                Record::date gte from,
                Record::date lt from + duration,
            )
        ).toList()

    @Serializable
    data class DBConfig(
        val userName: String,
        val password: String,
        val address: String,
        val dbName: String,
    ) {
        companion object {
            fun fromConnectionString(conStr: String, dbName: String): DBConfig? {
                var string = conStr.substringAfter("://").also { if (it == conStr) return null }

                val userName = string.substringBefore(':').also { if (it == string) return null }
                string = string.substringAfter(':')

                val password = string.substringBefore('@').also { if (it == string) return null }
                string = string.substringAfter('@')

                val address = string

                return DBConfig(
                    userName = userName,
                    password = password,
                    address = address,
                    dbName = dbName
                )
            }
        }

        val connectionString: String
            get() = "mongodb://$userName:$password@$address"

        fun json() = Json { prettyPrint = true }.encodeToString(this)
    }
}