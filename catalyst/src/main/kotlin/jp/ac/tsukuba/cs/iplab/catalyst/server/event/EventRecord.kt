package jp.ac.tsukuba.cs.iplab.catalyst.server.event

import jp.ac.tsukuba.cs.iplab.catalyst.Record
import kotlinx.serialization.Serializable

@Serializable
sealed class EventRecord : Record()
