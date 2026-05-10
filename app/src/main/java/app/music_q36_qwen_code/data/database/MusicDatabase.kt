package app.music_q36_qwen_code.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import app.music_q36_qwen_code.data.dao.FavoriteDao
import app.music_q36_qwen_code.data.dao.PlaylistDao
import app.music_q36_qwen_code.data.dao.SongDao
import app.music_q36_qwen_code.data.model.Favorite
import app.music_q36_qwen_code.data.model.Playlist
import app.music_q36_qwen_code.data.model.PlaylistSongMap
import app.music_q36_qwen_code.data.model.Song

/**
 * Room数据库
 */
@Database(
    entities = [Song::class, Playlist::class, PlaylistSongMap::class, Favorite::class],
    version = 1,
    exportSchema = false
)
abstract class MusicDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        private const val DATABASE_NAME = "music_player_db"

        @Volatile
        private var INSTANCE: MusicDatabase? = null

        fun getInstance(context: Context): MusicDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    MusicDatabase::class.java,
                    DATABASE_NAME
                ).build().also { INSTANCE = it }
            }
        }
    }
}
