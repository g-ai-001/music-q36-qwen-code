package app.music_q36_qwen_code.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import app.music_q36_qwen_code.data.dao.FavoriteDao
import app.music_q36_qwen_code.data.dao.SongDao
import app.music_q36_qwen_code.data.database.MusicDatabase
import app.music_q36_qwen_code.data.model.Song
import app.music_q36_qwen_code.service.PlayerManager
import app.music_q36_qwen_code.service.PlayerState
import app.music_q36_qwen_code.service.RepeatMode
import app.music_q36_qwen_code.utils.Logger
import app.music_q36_qwen_code.utils.LyricLine
import app.music_q36_qwen_code.utils.LyricParser
import app.music_q36_qwen_code.utils.MediaScanner
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * 播放器ViewModel
 * 管理播放状态和歌词
 */
class PlayerViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "PlayerViewModel"

    private val songDao: SongDao
    private val favoriteDao: FavoriteDao

    private val _playerState = MutableStateFlow(PlayerState())
    val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()

    private val _lyrics = MutableStateFlow<List<LyricLine>>(emptyList())
    val lyrics: StateFlow<List<LyricLine>> = _lyrics.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    init {
        val db = MusicDatabase.getInstance(application)
        songDao = db.songDao()
        favoriteDao = db.favoriteDao()

        // Observe player state changes
        PlayerManager.playerState.onEach { state ->
            _playerState.value = state
            loadLyricsForSong(state.currentSong)
            state.currentSong?.let { song ->
                viewModelScope.launch {
                    _isFavorite.value = favoriteDao.isFavorite(song.id)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun playSong(song: Song) {
        PlayerManager.prepare(song)
        PlayerManager.play()
    }

    fun togglePlayPause() {
        PlayerManager.togglePlayPause()
    }

    fun playNext() {
        PlayerManager.playNext()
    }

    fun playPrevious() {
        PlayerManager.playPrevious()
    }

    fun seekTo(position: Long) {
        PlayerManager.seekTo(position)
    }

    fun setShuffle(shuffle: Boolean) {
        PlayerManager.setShuffle(shuffle)
    }

    fun setRepeatMode(mode: RepeatMode) {
        PlayerManager.setRepeatMode(mode)
    }

    fun setPlaylist(playlist: List<Song>, startIndex: Int = 0) {
        PlayerManager.setPlaylist(playlist, startIndex)
    }

    fun toggleFavorite() {
        val song = _playerState.value.currentSong ?: return
        viewModelScope.launch {
            if (_isFavorite.value) {
                favoriteDao.removeFavoriteBySongId(song.id)
                Logger.i(TAG, "Removed from favorites: ${song.title}")
            } else {
                favoriteDao.addFavorite(
                    app.music_q36_qwen_code.data.model.Favorite(
                        songId = song.id,
                        addedAt = System.currentTimeMillis()
                    )
                )
                Logger.i(TAG, "Added to favorites: ${song.title}")
            }
            _isFavorite.value = !_isFavorite.value
        }
    }

    private fun loadLyricsForSong(song: Song?) {
        if (song == null) {
            _lyrics.value = emptyList()
            return
        }

        viewModelScope.launch {
            val lrcPath = LyricParser.findLrcFileForAudio(song.path)
            if (lrcPath != null) {
                _lyrics.value = LyricParser.parseLrcFile(lrcPath)
                Logger.i(TAG, "Loaded lyrics for: ${song.title}")
            } else {
                _lyrics.value = emptyList()
                Logger.d(TAG, "No lyrics found for: ${song.title}")
            }
        }
    }

    fun getCurrentLyricIndex(): Int {
        val currentTime = _playerState.value.currentPosition
        val lyrics = _lyrics.value
        return lyrics.indexOfLast { it.time <= currentTime }
    }

    override fun onCleared() {
        super.onCleared()
        Logger.i(TAG, "PlayerViewModel cleared")
    }
}
