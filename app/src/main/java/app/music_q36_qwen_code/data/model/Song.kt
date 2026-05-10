package app.music_q36_qwen_code.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 歌曲数据模型
 */
@Entity(tableName = "songs")
data class Song(
    @PrimaryKey
    val id: Long,
    val title: String,
    val artist: String,
    val album: String,
    val duration: Long, // 毫秒
    val path: String,
    val albumArtUri: String,
    val size: Long, // 文件大小（字节）
    val dateAdded: Long,
    val lastPlayedTime: Long = 0,
    val playCount: Int = 0
) {
    companion object {
        const val MIN_DURATION = 30000L // 30秒，过滤铃声
    }
}
