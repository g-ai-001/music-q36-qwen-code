package app.music_q36_qwen_code.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 收藏歌曲表
 */
@Entity(
    tableName = "favorites",
    foreignKeys = [
        ForeignKey(
            entity = Song::class,
            parentColumns = ["id"],
            childColumns = ["songId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["songId"], unique = true)]
)
data class Favorite(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val songId: Long,
    val addedAt: Long
)
