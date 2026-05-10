package app.music_q36_qwen_code.service

import app.music_q36_qwen_code.data.model.Song
import app.music_q36_qwen_code.utils.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 播放器状态
 */
data class PlayerState(
    val currentSong: Song? = null,
    val isPlaying: Boolean = false,
    val currentPosition: Long = 0,
    val duration: Long = 0,
    val playlist: List<Song> = emptyList(),
    val currentIndex: Int = 0,
    val isShuffle: Boolean = false,
    val repeatMode: RepeatMode = RepeatMode.NONE
)

enum class RepeatMode {
    NONE,       // 不重复
    ONE,        // 单曲循环
    ALL         // 列表循环
}

/**
 * 播放器管理器 - 单例模式管理ExoPlayer实例
 */
object PlayerManager {
    private const val TAG = "PlayerManager"

    private val _playerState = MutableStateFlow(PlayerState())
    val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()

    // 实际的ExoPlayer实例将在服务中创建
    private var exoPlayer: androidx.media3.exoplayer.ExoPlayer? = null

    fun initialize() {
        Logger.i(TAG, "PlayerManager initialized")
    }

    fun prepare(song: Song) {
        Logger.i(TAG, "Preparing song: ${song.title}")
        val currentState = _playerState.value
        _playerState.value = currentState.copy(
            currentSong = song,
            currentPosition = 0,
            duration = song.duration
        )
    }

    fun play() {
        Logger.i(TAG, "Play")
        _playerState.value = _playerState.value.copy(isPlaying = true)
    }

    fun pause() {
        Logger.i(TAG, "Pause")
        _playerState.value = _playerState.value.copy(isPlaying = false)
    }

    fun togglePlayPause() {
        if (_playerState.value.isPlaying) {
            pause()
        } else {
            play()
        }
    }

    fun seekTo(position: Long) {
        Logger.d(TAG, "Seek to: $position")
        _playerState.value = _playerState.value.copy(currentPosition = position)
    }

    fun setPlaylist(playlist: List<Song>, startIndex: Int = 0) {
        Logger.i(TAG, "Setting playlist with ${playlist.size} songs, start index: $startIndex")
        if (playlist.isEmpty()) return

        val song = playlist[startIndex]
        _playerState.value = PlayerState(
            currentSong = song,
            isPlaying = true,
            currentPosition = 0,
            duration = song.duration,
            playlist = playlist,
            currentIndex = startIndex
        )
    }

    fun playNext() {
        val currentState = _playerState.value
        val playlist = currentState.playlist
        if (playlist.isEmpty()) return

        val nextIndex = when {
            currentState.isShuffle -> (0 until playlist.size).random()
            currentState.currentIndex < playlist.size - 1 -> currentState.currentIndex + 1
            currentState.repeatMode == RepeatMode.ALL -> 0
            else -> currentState.currentIndex
        }

        if (nextIndex != currentState.currentIndex) {
            val nextSong = playlist[nextIndex]
            _playerState.value = currentState.copy(
                currentSong = nextSong,
                currentIndex = nextIndex,
                currentPosition = 0,
                duration = nextSong.duration,
                isPlaying = true
            )
            Logger.i(TAG, "Playing next: ${nextSong.title}")
        }
    }

    fun playPrevious() {
        val currentState = _playerState.value
        val playlist = currentState.playlist
        if (playlist.isEmpty()) return

        // If more than 3 seconds in, restart current song
        if (currentState.currentPosition > 3000) {
            seekTo(0)
            return
        }

        val prevIndex = when {
            currentState.isShuffle -> (0 until playlist.size).random()
            currentState.currentIndex > 0 -> currentState.currentIndex - 1
            currentState.repeatMode == RepeatMode.ALL -> playlist.size - 1
            else -> 0
        }

        val prevSong = playlist[prevIndex]
        _playerState.value = currentState.copy(
            currentSong = prevSong,
            currentIndex = prevIndex,
            currentPosition = 0,
            duration = prevSong.duration,
            isPlaying = true
        )
        Logger.i(TAG, "Playing previous: ${prevSong.title}")
    }

    fun setShuffle(shuffle: Boolean) {
        _playerState.value = _playerState.value.copy(isShuffle = shuffle)
    }

    fun setRepeatMode(mode: RepeatMode) {
        _playerState.value = _playerState.value.copy(repeatMode = mode)
    }

    fun updatePosition(position: Long) {
        if (_playerState.value.currentSong != null) {
            _playerState.value = _playerState.value.copy(currentPosition = position)
        }
    }

    fun release() {
        Logger.i(TAG, "PlayerManager released")
        exoPlayer?.release()
        exoPlayer = null
    }
}
