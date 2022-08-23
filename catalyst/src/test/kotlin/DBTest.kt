//import jp.ac.tsukuba.cs.iplab.catalyst.client.BufferedTextWidgetOutput
//import jp.ac.tsukuba.cs.iplab.catalyst.client.KodeinRecord
//import jp.ac.tsukuba.cs.iplab.catalyst.client.Message
//import jp.ac.tsukuba.cs.iplab.catalyst.client.kana.Consonant
//import jp.ac.tsukuba.cs.iplab.catalyst.client.kana.Vowel
//import jp.ac.tsukuba.cs.iplab.catalyst.client.schedule.Experiment
//import jp.ac.tsukuba.cs.iplab.catalyst.client.schedule.Session
//import jp.ac.tsukuba.cs.iplab.catalyst.client.softwarekeyboard.Direction
//import jp.ac.tsukuba.cs.iplab.catalyst.client.softwarekeyboard.Keyboard
//import jp.ac.tsukuba.cs.iplab.catalyst.client.softwarekeyboard.Output
//import jp.ac.tsukuba.cs.iplab.catalyst.client.softwarekeyboard.Source
//import jp.ac.tsukuba.cs.iplab.catalyst.db.*
//import kotlinx.datetime.Clock
//import kotlinx.serialization.json.Json
//import kotlinx.serialization.decodeFromString
//import kotlinx.serialization.encodeToString
//import org.junit.jupiter.api.AfterEach
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import org.kodein.db.DB
//import org.kodein.db.*
//import org.kodein.db.inmemory.inMemory
//import kotlin.time.Duration
//import kotlin.time.ExperimentalTime
//
//class DBTest {
//    private lateinit var db: DB
//
//    @BeforeEach
//    fun beforeEach() {
//        db = DB.inMemory.openDB("test")
//    }
//
//    @AfterEach
//    fun afterEach() {
//        DB.inMemory.destroy("test")
//    }
//
//    @Test
//    fun test() {
//        val a = Output.Char(Consonant.A, Vowel.A, doubleFlicked = false, repeated = false)
//        a.keyboard = Keyboard.JoyFlick
//        val xtu = Output.Char(Consonant.XT, Vowel.U, doubleFlicked = true, repeated = false)
//        xtu.keyboard = Keyboard.JoyFlick
//        val aa = Output.Char(Consonant.N, Vowel.A, doubleFlicked = false, repeated = true)
//        aa.keyboard = Keyboard.JoyFlick
//
//        val delete0 = Output.Delete()
//        delete0.keyboard = Keyboard.KanaSyllabary
//        val delete1 = Output.Delete(true)
//        delete1.keyboard = Keyboard.KanaSyllabary
//
//        val modify = Output.Modify()
//        modify.keyboard = Keyboard.Kai
//
//        val left = Output.CaretMove(Output.CaretMove.Direction.Left)
//        left.keyboard = Keyboard.JoyFlick
//        val right = Output.CaretMove(Output.CaretMove.Direction.Right)
//        right.keyboard = Keyboard.KanaSyllabary
//
//        val prev = Output.ConversionUpdate.CursorMove(Output.ConversionUpdate.CursorMove.Direction.Prev)
//        prev.keyboard = Keyboard.JoyFlick
//        val next = Output.ConversionUpdate.CursorMove(Output.ConversionUpdate.CursorMove.Direction.Next)
//        next.keyboard = Keyboard.KanaSyllabary
//
//        val message = Message("Test Message", Keyboard.Kai)
//
//        val switch = Keyboard.Switch(Keyboard.KanaSyllabary, Keyboard.JoyFlick)
//
//        val models = listOf(a, xtu, aa, delete0, delete1, modify, left, right, prev, next, message, switch)
//
//        models.forEach {
//            db.put(it)
//        }
//
//        assert(db.find<KodeinRecord>().all().use {
//            it.asModelSequence().toSet().isEmpty()
//        })
//
//    }
//
//    @Test
//    fun testChar() {
//        val a = Output.Char(Consonant.A, Vowel.A, doubleFlicked = false, repeated = false)
//        a.keyboard = Keyboard.JoyFlick
//
//        val xtu = Output.Char(Consonant.XT, Vowel.U, doubleFlicked = true, repeated = false)
//        xtu.keyboard = Keyboard.JoyFlick
//
//        val aa = Output.Char(Consonant.N, Vowel.A, doubleFlicked = false, repeated = true)
//        aa.keyboard = Keyboard.JoyFlick
//
//        // insert
//        val a_key: Key<Output.Char> = db.put(a)
//        val xtu_key = db.put(xtu)
//        val aa_key = db.put(aa)
//
//        // get by key
//        assert(a == db[a_key])
//        assert(xtu == db[xtu_key])
//        assert(aa == db[aa_key])
//
//        // iterate
//        assert(db.find<Output.Char>().all().use {
//            it.asModelSequence().forEach { println("Date: ${it.date}, Keyboard: ${it.keyboard}, $it") }
//            it.asModelSequence().toSet() == setOf(a, xtu, aa)
//        })
//    }
//
//    @Test
//    fun testDelete() {
//        val delete0 = Output.Delete()
//        delete0.keyboard = Keyboard.KanaSyllabary
//        val delete1 = Output.Delete(true)
//        delete1.keyboard = Keyboard.KanaSyllabary
//
//        // insert
//        val key_0 = db.put(delete0)
//        val key_1 = db.put(delete1)
//
//        // get by key
//        assert(delete0 == db[key_0])
//        assert(delete1 == db[key_1])
//
//        // iterate
//        assert(db.find<Output.Delete>().all().use {
//            it.asModelSequence().forEach { println("Date: ${it.date}, Keyboard: ${it.keyboard}, $it") }
//            it.asModelSequence().toSet() == setOf(delete0, delete1)
//        })
//    }
//
//    @Test
//    fun testModify() {
//        val modify = Output.Modify()
//        modify.keyboard = Keyboard.Kai
//
//        // insert
//        val key = db.put(modify)
//
//        // get by key
//        assert(modify == db[key])
//
//        // iterate
//        assert(db.find<Output.Modify>().all().use {
//            it.asModelSequence().forEach { println("Date: ${it.date}, Keyboard: ${it.keyboard}, $it") }
//            it.asModelSequence().toSet() == setOf(modify)
//        })
//    }
//
//    @Test
//    fun testCaretMove() {
//        val left = Output.CaretMove(Output.CaretMove.Direction.Left)
//        left.keyboard = Keyboard.JoyFlick
//        val right = Output.CaretMove(Output.CaretMove.Direction.Right)
//        right.keyboard = Keyboard.KanaSyllabary
//
//        // insert
//        val l_key = db.put(left)
//        val r_key = db.put(right)
//
//        // get by key
//        assert(left == db[l_key])
//        assert(right == db[r_key])
//
//        // iterate
//        assert(db.find<Output.CaretMove>().all().use {
//            it.asModelSequence().forEach { println("Date: ${it.date}, Keyboard: ${it.keyboard}, $it") }
//            it.asModelSequence().toSet() == setOf(left, right)
//        })
//    }
//
//    @Test
//    fun testCursorMove() {
//        val prev = Output.ConversionUpdate.CursorMove(Output.ConversionUpdate.CursorMove.Direction.Prev)
//        prev.keyboard = Keyboard.JoyFlick
//        val next = Output.ConversionUpdate.CursorMove(Output.ConversionUpdate.CursorMove.Direction.Next)
//        next.keyboard = Keyboard.KanaSyllabary
//
//        // insert
//        val p_key = db.put(prev)
//        val n_key = db.put(next)
//
//        // get by key
//        assert(prev == db[p_key])
//        assert(next == db[n_key])
//
//        // iterate
//        assert(db.find<Output.ConversionUpdate.CursorMove>().all().use {
//            it.asModelSequence().forEach { println("Date: ${it.date}, Keyboard: ${it.keyboard}, $it") }
//            it.asModelSequence().toSet() == setOf(prev, next)
//        })
//    }
//
//    @Test
//    fun testMessage() {
//        val message = Message("Test Message", Keyboard.Kai)
//
//        // insert
//        val key = db.put(message)
//
//        // get by key
//        assert(message == db[key])
//
//        // iterate
//        assert(db.find<Message>().all().use {
//            it.asModelSequence().forEach { println("Date: ${it.date}, Keyboard: ${it.keyboard}, $it") }
//            it.asModelSequence().toSet() == setOf(message)
//        })
//    }
//
//    @Test
//    fun testSwitchKeyboard() {
//        val switch = Keyboard.Switch(Keyboard.KanaSyllabary, Keyboard.JoyFlick)
//        val date = switch.date
//
//        // insert
//        val key = db.put(switch)
//
//        // get by key
//        assert(switch == db[key])
//        assert(date == db[key]?.date)
//
//        // iterate
//        assert(db.find<Keyboard.Switch>().all().use {
//            it.asModelSequence().forEach { println("Date: ${it.date}, $it") }
//            it.asModelSequence().toSet() == setOf(switch)
//        })
//    }
//
//
//    @OptIn(ExperimentalTime::class)
//    @Test
//    fun testExperiment() {
//        val start = Clock.System.now()
//
//        val sessions = listOf(
//            Session(start, Duration.minutes(1), Keyboard.JoyFlick),
//            Session(start, Duration.minutes(1), Keyboard.KanaSyllabary),
//            Session(start, Duration.minutes(1), Keyboard.Kai),
//            Session(start, Duration.minutes(1), listOf(Keyboard.JoyFlick, Keyboard.Kai)),
//        )
//
//        val experiment = Experiment(start, sessions, 1)
//
//        // insert
//        val key = db.put(experiment)
//
//        // get by key
//        assert(experiment == db[key])
//
//        // iterate
//        assert(db.find<Experiment>().all().use {
//            it.asModelSequence().forEach { println("$it") }
//            it.asModelSequence().toSet() == setOf(experiment)
//        })
//    }
//
//    @OptIn(ExperimentalTime::class)
//    @Test
//    fun testSerialize() {
//        val format = Json { serializersModule = KodeinRecord.serializersModule }
//
//        val records = listOf(
//            Message("Sample Message", Keyboard.JoyFlick),
////            Participant("Identity", "PlayerName", 1),
////            Schedule(
////                Clock.System.now(),
////                listOf(
////                    Session(Clock.System.now(), Duration.minutes(60), Keyboard.JoyFlick),
////                    Session(Clock.System.now(), Duration.minutes(60), Keyboard.KanaSyllabary),
////                    Session(
////                        Clock.System.now(), Duration.minutes(60), listOf(
////                            Keyboard.JoyFlick, Keyboard.KanaSyllabary, Keyboard.Kai
////                        )
////                    ),
////                )
////            ),
//            Keyboard.Open(Keyboard.JoyFlick),
//            Keyboard.Switch(Keyboard.JoyFlick, Keyboard.KanaSyllabary),
//            Keyboard.Close(Keyboard.Kai),
//            Output.Char(Consonant.N, Vowel.A, false, false),
//            Output.Delete(),
//            Output.Modify(),
//            Output.CaretMove(Output.CaretMove.Direction.Left),
//            Output.ConversionUpdate.CursorMove(Output.ConversionUpdate.CursorMove.Direction.Next),
//            Output.ConversionUpdate.Select(),
//            Output.RecordOnly.KanaSyllabary.CursorMoveTo(
//                Consonant.N, Vowel.A, Source.DPad, Direction.Left
//            ),
//            Output.RecordOnly.KanaSyllabary.FocusOnCandidates(Source.DPad),
//            Output.RecordOnly.KanaSyllabary.FocusOnKeyboard(Source.DPad),
//            Output.RecordOnly.JoyFlick.SelectConsonant(Consonant.N),
//            Output.RecordOnly.JoyFlick.SelectVowel(Vowel.A),
//            BufferedTextWidgetOutput.SelectedCandidate("テスト")
//        )
//        records.forEach {
//            if (it is Output) {
//                it.keyboard = Keyboard.JoyFlick
//            }
//        }
//        println(records.joinToString(","))
//
//        val jsons = records.map { format.encodeToString(it) }
//        println(jsons.joinToString(","))
//
//        val serialized = jsons.map { format.decodeFromString<KodeinRecord>(it) }
//        println(serialized.joinToString(","))
//
//        assert(records == serialized)
//    }
//
////    @Test
////    fun entityRecordTest() {
////        println("entityRecordTest()")
////        val entityRecord = Entity(
////            position = Vector3(2021.0, 4.0, 16.0),
////            motion = Vector3(2.0, .0, 1.0),
////            rotation = Rotation(yaw = 1.0f, pitch = 0.5f),
////            fallDistance = 0.8f,
////            onGround = false,
////            ticksExisted = 280,
////            fire = 12,
////            inWater = false,
////            inLava = false,
////            dimension = "overworld",
//////            eyesInWater = true,
//////            inPortal = false,
//////            portalCounter = 0,
////            cachedUniqueIdString = "UUID",
////            eyeHeight = 0.8f
////        )
////
////        val livingEntityRecord = LivingEntityRecord(
////            health = 12f,
////            isSwingInProgress = false,
////            swingingHand = null,
//////            swingProgressInt = 0,
////            arrowHitTimer = 0,
////            beeStingRemovalCooldown = 0,
////            hurtTime = 0,
////            maxHurtTime = 0,
////            attackedAtYaw = 0f,
////            deathTime = 200,
////            swingProgress = 10f,
//////            ticksSinceLastSwing = 20,
////            limbSwingAmount = 1f,
////            limbSwing = 1f,
////            rotationYawHead = 3f,
//////            recentlyHit = 10,
//////            dead = false,
////            idleTime = 30,
//////            onGroundSpeedFactor = 1f,
//////            movedDistance = 20f,
//////            scoreValue = 1560,
//////            lastDamage = 14.3f,
//////            isJumping = false,
//////            jumpTicks = 0,
////            moveStrafing = 1.1f,
////            moveVertical = 2.0f,
////            moveForward = 1.2f,
////            activeItemStack = ItemStackRecord.NotEmpty(
////                itemId = 14,
////                count = 20,
////            ),
//////            activeItemStackUseCount = 11,
////            ticksElytraFlying = 0,
////            entityRecord = entityRecord,
////        )
////
////        val playerEntityRecord = PlayerEntityRecord(
////            playerEntityUUID = "PlayerUUID",
////            playerName = "KYokoyama",
////            inventory = PlayerInventoryRecord(
////                main = hashMapOf(
////                    1 to ItemStackRecord.NotEmpty(
////                        itemId = 11,
////                        count = 3,
////                    )
////                ),
////                armour = hashMapOf(),
////                offhand = null,
////                currentItem = 1,
////                timesChanged = 3,
////            ),
////            cameraYaw = 0.8f,
////            foodStats = FoodStatsRecord(
////                foodLevel = 4,
//////                foodTimer = 132,
////                foodSaturationLevel = 1f,
//////                foodExhaustionLevel = 13f,
////            ),
////            sleepTimer = 0,
//////            eyesInWaterPlayer = false,
////            livingEntityRecord = livingEntityRecord,
////        )
////
////        val records = listOf(entityRecord, livingEntityRecord, playerEntityRecord)
////
////        records.forEach {
////            // 保存
////            val key = db.put(it)
////            // 確認
////            assert(it == db[key])
////        }
////    }
//}