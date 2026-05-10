package app.music_q36_qwen_code.data.dao

import androidx.room.*
import app.music_q36_qwen_code.data.model.Favorite
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorites ORDER BY addedAt DESC")
    fun getAllFavorites(): Flow<List<Favorite>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE songId = :songId)")
    suspend fun isFavorite(songId: Long): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: Favorite)

    @Query("DELETE FROM favorites WHERE songId = :songId")
    suspend fun removeFavoriteBySongId(songId: Long)

    @Query("SELECT * FROM favorites ORDER BY addedAt DESC")
    suspend fun getFavoriteList(): List<Favorite>
}
