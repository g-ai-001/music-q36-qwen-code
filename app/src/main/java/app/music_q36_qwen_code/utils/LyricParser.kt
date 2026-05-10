package app.music_q36_qwen_code.utils

import app.music_q36_qwen_code.utils.Logger
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader

/**
 * 歌词解析器
 * 解析LRC格式歌词文件
 */
data class LyricLine(
    val time: Long, // 毫秒
    val text: String
)

object LyricParser {
    private const val TAG = "LyricParser"

    /**
     * 解析LRC歌词文件
     */
    fun parseLrcFile(filePath: String): List<LyricLine> {
        val lines = mutableListOf<LyricLine>()
        val file = File(filePath)

        if (!file.exists()) {
            return lines
        }

        try {
            FileInputStream(file).use { fis ->
                InputStreamReader(fis, "UTF-8").use { reader ->
                    reader.readLines().forEach { line ->
                        parseLrcLine(line.trim())?.let { lines.add(it) }
                    }
                }
            }
        } catch (e: Exception) {
            Logger.e(TAG, "Failed to parse LRC file: $filePath", e)
        }

        return lines.sortedBy { it.time }
    }

    /**
     * 解析单行LRC歌词
     * 格式: [mm:ss.xx]Lyric text
     */
    private fun parseLrcLine(line: String): LyricLine? {
        if (line.isEmpty() || !line.startsWith("[")) {
            return null
        }

        try {
            val timeEndIndex = line.indexOf(']')
            if (timeEndIndex < 0) return null

            val timeStr = line.substring(1, timeEndIndex)
            val text = line.substring(timeEndIndex + 1).trim()

            val timeParts = timeStr.split(":")
            if (timeParts.size != 2) return null

            val minutes = timeParts[0].toLong()
            val secondsAndMillis = timeParts[1].split(".")
            val seconds = secondsAndMillis[0].toLong()
            val millis = if (secondsAndMillis.size > 1) {
                secondsAndMillis[1].toLong() * 10
            } else {
                0L
            }

            val totalTime = minutes * 60 * 1000 + seconds * 1000 + millis

            return LyricLine(totalTime, text)
        } catch (e: Exception) {
            Logger.w(TAG, "Failed to parse LRC line: $line", e)
            return null
        }
    }

    /**
     * 查找与音频文件同名的LRC歌词文件
     */
    fun findLrcFileForAudio(audioPath: String): String? {
        val basePath = audioPath.substringBeforeLast(".")
        val possiblePaths = listOf(
            "$basePath.lrc",
            "$basePath.LRC",
            "${File(audioPath).parent}/${File(audioPath).nameWithoutExtension}.lrc"
        )

        return possiblePaths.firstOrNull { File(it).exists() }
    }

    /**
     * 根据当前播放时间获取应该显示的歌词行
     */
    fun getLyricLineAtTime(lyrics: List<LyricLine>, currentTime: Long): LyricLine? {
        if (lyrics.isEmpty()) return null

        var currentLine: LyricLine? = null
        for (line in lyrics) {
            if (line.time <= currentTime) {
                currentLine = line
            } else {
                break
            }
        }
        return currentLine
    }

    /**
     * 获取当前歌词行及其前后行（用于显示）
     */
    fun getLyricLinesAroundTime(
        lyrics: List<LyricLine>,
        currentTime: Long,
        range: Int = 2
    ): Triple<List<LyricLine>, LyricLine?, List<LyricLine>> {
        val currentIndex = lyrics.indexOfLast { it.time <= currentTime }

        if (currentIndex < 0) {
            val before = if (lyrics.size > range) lyrics.take(range) else lyrics
            return Triple(emptyList(), lyrics.firstOrNull(), emptyList())
        }

        val currentLine = lyrics.getOrNull(currentIndex)
        val beforeStart = maxOf(0, currentIndex - range)
        val afterEnd = minOf(lyrics.size, currentIndex + range + 1)

        val before = lyrics.subList(beforeStart, currentIndex)
        val after = if (afterEnd <= lyrics.size) lyrics.subList(currentIndex + 1, afterEnd) else emptyList()

        return Triple(before, currentLine, after)
    }
}
