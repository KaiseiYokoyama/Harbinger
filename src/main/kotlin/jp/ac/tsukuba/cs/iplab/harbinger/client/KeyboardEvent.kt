package jp.ac.tsukuba.cs.iplab.harbinger.client

import jp.ac.tsukuba.cs.iplab.catalyst.client.KeyboardRecord
import net.minecraftforge.eventbus.api.Event

/**
 * キーボードの操作に関するイベント
 * クライアントでしか発生しないイベントのサンプル
 */
class KeyboardEvent<R: KeyboardRecord>(val record: R) : Event()