package app.music_q36_qwen_code.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import app.music_q36_qwen_code.data.model.Song
import kotlinx.coroutines.flow.Flow

/**
 * 歌曲数据访问对象
 */
@Dao
interface SongDao {
    @Query("SELECT * FROM songs ORDER BY dateAdded DESC")
    fun getAllSongs(): Flow<List<Song>>

    @Query("SELECT * FROM songs ORDER BY title ASC")
    fun getSongsSortedByTitleAsc(): Flow<List<Song>>

    @Query("SELECT * FROM songs ORDER BY title DESC")
    fun getSongsSortedByTitleDesc(): Flow<List<Song>>

    @Query("SELECT * FROM songs ORDER BY artist ASC, title ASC")
    fun getSongsSortedByArtistAsc(): Flow<List<Song>>

    @Query("SELECT * FROM songs ORDER BY artist DESC, title ASC")
    fun getSongsSortedByArtistDesc(): Flow<List<Song>>

    @Query("SELECT * FROM songs ORDER BY duration ASC")
    fun getSongsSortedByDurationAsc(): Flow<List<Song>>

    @Query("SELECT * FROM songs ORDER BY duration DESC")
    fun getSongsSortedByDurationDesc(): Flow<List<Song>>

    @Query("SELECT * FROM songs ORDER BY dateAdded ASC")
    fun getSongsSortedByDateAddedAsc(): Flow<List<Song>>

    @Query("SELECT * FROM songs ORDER BY lastPlayedTime DESC LIMIT 20")
    fun getRecentlyPlayed(): Flow<List<Song>>

    @Query("SELECT * FROM songs WHERE id = :songId")
    suspend fun getSongById(songId: Long): Song?

    @Query("SELECT * FROM songs WHERE path = :path")
    suspend fun getSongByPath(path: String): Song?

    @Query("SELECT * FROM songs WHERE title LIKE '%' || :query || '%' OR artist LIKE '%' || :query || '%'")
    fun searchSongs(query: String): Flow<List<Song>>

    @Query("SELECT DISTINCT artist FROM songs ORDER BY artist")
    fun getAllArtists(): Flow<List<String>>

    @Query("SELECT DISTINCT album FROM songs ORDER BY album")
    fun getAllAlbums(): Flow<List<String>>

    @Query("SELECT * FROM songs WHERE artist = :artist ORDER BY dateAdded DESC")
    fun getSongsByArtist(artist: String): Flow<List<Song>>

    @Query("SELECT * FROM songs WHERE album = :album ORDER BY dateAdded DESC")
    fun getSongsByAlbum(album: String): Flow<List<Song>>

    @Query("UPDATE songs SET lastPlayedTime = :time, playCount = playCount + 1 WHERE id = :songId")
    suspend fun updatePlayHistory(songId: Long, time: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSongs(songs: List<Song>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNewSongs(songs: List<Song>)

    @Update
    suspend fun updateSongs(songs: List<Song>)

    @Delete
    suspend fun deleteSong(song: Song)

    @Query("DELETE FROM songs WHERE path NOT IN (:paths)")
    suspend fun deleteSongsNotInPaths(paths: List<String>)

    @Query("DELETE FROM songs")
    suspend fun deleteAllSongs()

    @Query("SELECT COUNT(*) FROM songs")
    fun getSongCount(): Flow<Int>

    @Query("SELECT path, lastModified FROM songs")
    suspend fun getAllSongPathsAndModifiedTimes(): List<PathAndModifiedTime>
}

/**
 * 歌曲路径和最后修改时间数据类
 */
data class PathAndModifiedTime(
    val path: String,
    val lastModified: Long
)
