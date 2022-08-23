package jp.ac.tsukuba.cs.iplab.harbinger.prelude

import jp.ac.tsukuba.cs.iplab.catalyst.db.DBWriter
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.io.path.isRegularFile
import kotlin.streams.toList
import jp.ac.tsukuba.cs.iplab.catalyst.server.World
import kotlin.io.path.deleteIfExists

class WorldSaver(
    val dbWriter: DBWriter,
) {
    val zipFileName = "${dbWriter.expConfig.expID}.zip"

    companion object {
        val worldDir = Paths.get("world")
    }

    fun save() {
        deletePrevZip()
        zip()
        upload()
    }

    private fun worldFiles(): List<Path> = Files.walk(worldDir).filter { it.isRegularFile() }.toList()

    private fun deletePrevZip() = Paths.get(zipFileName).deleteIfExists()

    private fun zip() {
        ZipOutputStream(FileOutputStream(zipFileName)).use { zipper ->
            worldFiles().forEach { inputPath ->
                FileInputStream(inputPath.toFile()).use { input ->
                    val entry = ZipEntry(inputPath.toString())

                    // zip内でのファイルの配置を決定
                    zipper.putNextEntry(entry)
                    // zipに書き込み
                    zipper.write(input.readBytes())
                    // 1ファイルの書き込み終了
                    zipper.closeEntry()
                }
            }
        }
    }

    private fun upload() {
        val world = World(
            dbWriter.expConfig.expID,
            FileInputStream(zipFileName).readBytes(),
        )

        dbWriter.recordWorld(world)
    }
}