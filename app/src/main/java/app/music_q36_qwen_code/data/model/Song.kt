package app.music_q36_qwen_code.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 歌曲数据模型
 */
@Entity(
    tableName = "songs",
    indices = [
        Index(value = ["path"], unique = true),
        Index(value = ["artist"]),
        Index(value = ["album"])
    ]
)
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
    val lastModified: Long = 0, // 文件最后修改时间
    val lastPlayedTime: Long = 0,
    val playCount: Int = 0
) {
    companion object {
        const val MIN_DURATION = 30000L // 30秒，过滤铃声
    }
}

/**
 * 扫描进度信息
 */
data class ScanProgress(
    val current: Int = 0,
    val total: Int = 0,
    val isNew: Boolean = true, // true=扫描中, false=已完成
    val message: String = ""
) {
    val percentage: Int
        get() = if (total > 0) (current * 100 / total) else 0
}
