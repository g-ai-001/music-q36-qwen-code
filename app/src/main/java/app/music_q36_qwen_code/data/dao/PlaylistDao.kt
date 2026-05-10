package app.music_q36_qwen_code.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import app.music_q36_qwen_code.data.model.Playlist
import app.music_q36_qwen_code.data.model.PlaylistSongMap
import kotlinx.coroutines.flow.Flow

/**
 * 歌单数据访问对象
 */
@Dao
interface PlaylistDao {
    @Query("SELECT * FROM playlists ORDER BY createdAt DESC")
    fun getAllPlaylists(): Flow<List<Playlist>>

    @Query("SELECT * FROM playlists WHERE id = :playlistId")
    suspend fun getPlaylistById(playlistId: Long): Playlist?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: Playlist): Long

    @Update
    suspend fun updatePlaylist(playlist: Playlist)

    @Delete
    suspend fun deletePlaylist(playlist: Playlist)

    @Query("SELECT COUNT(*) FROM playlists")
    fun getPlaylistCount(): Flow<Int>

    // Playlist-Song Map operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSongToPlaylist(map: PlaylistSongMap)

    @Query("SELECT * FROM playlist_song_map WHERE playlistId = :playlistId ORDER BY `order` ASC")
    fun getSongsInPlaylist(playlistId: Long): Flow<List<PlaylistSongMap>>

    @Query("DELETE FROM playlist_song_map WHERE playlistId = :playlistId AND songId = :songId")
    suspend fun removeSongFromPlaylist(playlistId: Long, songId: Long)

    @Query("DELETE FROM playlist_song_map WHERE playlistId = :playlistId")
    suspend fun clearPlaylist(playlistId: Long)

    @Query("UPDATE playlists SET songCount = :count WHERE id = :playlistId")
    suspend fun updatePlaylistSongCount(playlistId: Long, count: Int)
}
