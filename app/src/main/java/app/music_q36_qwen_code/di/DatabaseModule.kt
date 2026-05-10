package app.music_q36_qwen_code.di

import android.content.Context
import app.music_q36_qwen_code.data.dao.FavoriteDao
import app.music_q36_qwen_code.data.dao.PlaylistDao
import app.music_q36_qwen_code.data.dao.SongDao
import app.music_q36_qwen_code.data.database.MusicDatabase

/**
 * 数据库依赖提供模块
 * 提供数据库和DAO的简单访问方法
 */
object DatabaseModule {
    fun provideDatabase(context: Context): MusicDatabase {
        return MusicDatabase.getInstance(context)
    }

    fun provideSongDao(context: Context): SongDao {
        return provideDatabase(context).songDao()
    }

    fun providePlaylistDao(context: Context): PlaylistDao {
        return provideDatabase(context).playlistDao()
    }

    fun provideFavoriteDao(context: Context): FavoriteDao {
        return provideDatabase(context).favoriteDao()
    }
}
