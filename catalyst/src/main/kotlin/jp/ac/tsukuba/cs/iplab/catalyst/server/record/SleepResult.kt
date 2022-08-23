package jp.ac.tsukuba.cs.iplab.catalyst.server.record

import kotlinx.serialization.Serializable

@Serializable
enum class SleepResult {
    NOT_POSSIBLE_HERE,
    NOT_POSSIBLE_NOW,
    TOO_FAR_AWAY,
    OBSTRUCTED,
    OTHER_PROBLEM,
    NOT_SAFE;
}