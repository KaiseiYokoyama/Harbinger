package jp.ac.tsukuba.cs.iplab.catalyst.server.record

import kotlinx.serialization.Serializable

/**
 * PlayerEntityクラスに対応するレコード用クラス
 */
@Serializable
class PlayerEntity(
    /**
     * PlayerEntity.getCachedUniqueIdString()
     * PlayerEntityそのものを保存する代わりに、識別子を保存しておく
     */
    val playerEntityUUID: String,
    /**
     * PlayerEntity.getName().getText()
     * アバターに割り当てられていたユーザネーム displayName（表示名）ではない
     */
    val playerName: String,
    /**
     * インベントリ
     */
    val inventory: PlayerInventoryRecord,
    val cameraYaw: Float,
    /**
     * 空腹度など
     */
    val foodStats: FoodStatsRecord,
    val sleepTimer: Int,
    /**
     * レベル
     */
    var experienceLevel: Int,
    /**
     * 合計経験値
     */
    var experienceTotal: Int,
    /**
     * 次のレベルへの進捗度
     */
    var experience: Float,
//    val eyesInWaterPlayer: Boolean,
    val livingEntity: LivingEntity,
) : LivingEntity(livingEntity)