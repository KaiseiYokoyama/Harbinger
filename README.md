# Harbinger
Harbingerは，Minecraftのプレイヤーの行動の記録の取得を目的とした実験用ソフトウェア（MOD）です。
プレイヤーの行動およびステータスの記録を行います。

| Minecraft | Minectaft Forge |
|:----------|:----------------|
| 1.16.3    | 34.1            |

## セットアップ
### クライアント
0. Minecraftをインストール
1. Minecraft 1.16.3を一度起動する（1.16.3のリソースをダウンロードするため）
2. Minecraft Forge（クライアント版）を導入
3. 本MODの`.jar`ファイルを、`mods`フォルダに追加

### サーバ
**MongoDB v5** or newerをセットアップしておく。

0. Minecraftをインストール
1. Minecraft 1.16.3を一度起動する（1.16.3のリソースをダウンロードするため）
2. Minecraft Forge（サーバ版）を導入
3. 本MODの`.jar`ファイルを、`mods`フォルダに追加
4. Minecraftを起動。起動に失敗し、`harbinger.json`が生成される。
5. `harbinger.json`にMongoDBの接続情報を書き込む

## Javaのバージョンについて
- Java 8 かつ Java8 321**未満**

## Developers
開発環境は*IntelliJ IDEA*をオススメします。[Minecraft Development](https://plugins.jetbrains.com/plugin/8327-minecraft-development) というイケてるプラグインが便利なので入れておきましょう。
JDKのインストールもお忘れなく。

### Gradleによるビルド・`.jar`ファイルの出力
- Minecraftを起動する: `runClient`
- `accesstransformer.cfg`の変更を反映する: （IntelliJの場合）`Reload All Gradle Projects`ボタンを押す
   - shiftキー2回で呼び出せるメニューで`Reload All Gradle Projects`を検索すると、actionsタブに出てくる
- 配布用の `.jar` ファイルを出力する：`shadowJar`
   - `jar` や `build` ではないので注意
   - 出力された成果物は`build/libs`に入っています
   - クライアント・サーバ兼用です

### Mixinを使うとき
メソッドやフィールドを`@Shadow` `@Inject`するときは、 **SRG名を使って** ください
ex. `NewChatGuiMixin.kt`

### よくあるエラー
#### ビルドエラー：`org.jetbrains.kotlin.backend.common.BackendException: Backend Internal error: Exception during IR lowering`
- `.gradle`
- `.idea`
- `build`
- `run`

プロジェクトのディレクトリから以上4フォルダを削除し、もう一度ビルドする。

#### 実行エラー：`java.lang.NoSuchMethodError: sun.security.util.ManifestEntryVerifier`
Java8 **321未満** を使用する。

#### サーバが終了してくれない
1. サーバのプロセスIDを探す `netstat -apn | grep :27020`
2. `kill [サーバのプロセスのID]`

## 過去の研究
- [JoyFlick: フリック入力に基づくゲームパッド向けかな文字入力手法](https://www.wiss.org/WISS2020Proceedings/data/17.pdf)
- [JoyFlick: Japanese Text Entry Using Dual Joysticks for Flick Input Users](https://link.springer.com/chapter/10.1007%2F978-3-030-85613-7_8)
