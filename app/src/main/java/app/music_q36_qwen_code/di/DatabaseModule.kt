package app.music_q36_qwen_code.di

import android.content.Context
import app.music_q36_qwen_code.data.dao.FavoriteDao
import app.music_q36_qwen_code.data.dao.PlaylistDao
import app.music_q36_qwen_code.data.dao.SongDao
import app.music_q36_qwen_code.data.database.MusicDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger Hilt依赖注入模块
 * 由于0.1.0版本暂不引入Hilt，使用简单的依赖提供方式
 */
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MusicDatabase {
        return MusicDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideSongDao(database: MusicDatabase): SongDao {
        return database.songDao()
    }

    @Provides
    @Singleton
    fun providePlaylistDao(database: MusicDatabase): PlaylistDao {
        return database.playlistDao()
    }

    @Provides
    @Singleton
    fun provideFavoriteDao(database: MusicDatabase): FavoriteDao {
        return database.favoriteDao()
    }
}
