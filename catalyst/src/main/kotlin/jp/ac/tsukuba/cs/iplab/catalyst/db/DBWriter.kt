package jp.ac.tsukuba.cs.iplab.catalyst.db

import com.mongodb.MongoWriteException
import com.mongodb.client.model.CreateCollectionOptions
import com.mongodb.client.model.ReplaceOptions
import com.mongodb.client.model.TimeSeriesOptions
import com.mongodb.client.result.InsertOneResult
import jp.ac.tsukuba.cs.iplab.catalyst.Record
import jp.ac.tsukuba.cs.iplab.catalyst.client.KeyboardRecord
import jp.ac.tsukuba.cs.iplab.catalyst.server.World
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.awaitLast
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.coroutine.replaceOne
import org.litote.kmongo.reactivestreams.getCollection
import org.litote.kmongo.serialization.registerModule
import org.litote.kmongo.util.KMongoUtil
import org.reactivestreams.Publisher
import java.nio.file.Path
import kotlin.io.path.readText
import kotlin.reflect.KClass

class DBWriter(
    val expConfig: ExpConfig,
): DBDriver(expConfig.dbConfig) {
    constructor(path: Path) : this(
        Json.decodeFromString<ExpConfig>(path.readText())
    )

    val recordedClasses = mutableSetOf<KClass<*>>()

    init {
        registerModule(KeyboardRecord.serializersModule)
    }

    val tx = Channel<Publisher<InsertOneResult>>(1000)
    private val worker = GlobalScope.launch(Dispatchers.IO) {
        while (true) {
            val pub = tx.receive()
            pub.awaitLast()
        }
    }

    data class ExpIdDuplicationException(
        val duplicatedID: String,
    ) : RuntimeException()

    fun recordExpConfig(expConfig: ExpConfig) {
        runBlocking {
            val collection = database.coroutine.getCollection<ExpConfig>()
            if (expConfig.dbConfig.dbName == "debug") {
                collection.replaceOne(expConfig, ReplaceOptions().upsert(true))
            } else {
                try {
                    collection.insertOne(expConfig)
                } catch (e: MongoWriteException) {
                    throw ExpIdDuplicationException(expConfig.expID)
                }
            }
        }
    }

    fun recordWorld(world: World) {
        runBlocking {
            val collection = database.coroutine.getCollection<World>()
            if (expConfig.dbConfig.dbName == "debug") {
                collection.replaceOne(world, ReplaceOptions().upsert(true))
            } else {
                collection.insertOne(world)
            }
        }
    }

    inline fun <reified R : Record> record(record: R) {
        // 実験に関する情報を追加
//        val mail = config.participants.get(record)        // メールアドレス
        record.expID = expConfig.expID                      // 実験ID
        record.timeStamp = record.date.toJavaInstant()      // timestamp

        // 必要に応じてTime Seriesコレクションの作成を行う
        val clazz: KClass<*> = if (record is KeyboardRecord) {
            KeyboardRecord::class // ソフトウェアキーボードの操作に関するイベントは一つのコレクションにまとめる
        } else {
            record::class
        }
        if (!recordedClasses.contains(clazz)) {
            // テーブルを更新
            recordedClasses.add(clazz)

            // 実験*レコードの型ごとの頻度でしか実行されないので、ここでブロックしてしまって問題ないはず
            runBlocking {
                // コレクションの有無を確認
                if (!collectionExists(clazz)) {
                    // コレクションがないとき、Time Seriesコレクションを作成
                    createTimeSeriesCollection(clazz)
                } else {
                    // NOTE: コレクションを取得し、TimeSeriesとなっていることを確認したい
                }
            }
        }

        tx.trySend(
            database.getCollection<R>().insertOne(record)
        )
    }

    /**
     * 型に対応するコレクションの有無を確認する
     */
    suspend inline fun collectionExists(clazz: KClass<*>) =
        database.coroutine.listCollectionNames()
            .contains(KMongoUtil.defaultCollectionName(clazz))

    /**
     * 型に対応するTime Seriesコレクションを作成する
     */
    suspend inline fun createTimeSeriesCollection(clazz: KClass<*>) {
        database.coroutine.createCollection(
            KMongoUtil.defaultCollectionName(clazz),
            CreateCollectionOptions().timeSeriesOptions(TimeSeriesOptions("timeStamp")),
        )
    }

    @Serializable
    data class ExpConfig(
        val dbConfig: DBConfig,
        /**
         * 実験ID
         */
        @SerialName("_id")
        val expID: String,
        /**
         * 実験開始（サーバ起動）時間
         */
        @EncodeDefault val date: Instant = kotlinx.datetime.Clock.System.now(),
    ) {
        companion object {
            private val EMPTY = ExpConfig(
                dbConfig = DBConfig(
                    userName = "",
                    password = "",
                    address = "",
                    dbName = "",
                ),
                expID = "",
            )

            val EMPTY_JSON = Json { prettyPrint = true }.encodeToString(EMPTY)
        }
    }
}