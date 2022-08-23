package jp.ac.tsukuba.cs.iplab.catalyst.server.record

import kotlinx.serialization.Serializable

@Serializable
class FoodStatsRecord(
    var foodLevel: Int,
    val foodSaturationLevel: Float,
//    val foodExhaustionLevel: Float,
//    val foodTimer: Int,
): jp.ac.tsukuba.cs.iplab.catalyst.Record()