package app.music_q36_qwen_code.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import app.music_q36_qwen_code.data.dao.FavoriteDao
import app.music_q36_qwen_code.data.dao.SongDao
import app.music_q36_qwen_code.data.database.MusicDatabase
import app.music_q36_qwen_code.data.model.Song
import app.music_q36_qwen_code.utils.Logger
import app.music_q36_qwen_code.utils.MediaScanner
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * 音乐库ViewModel
 * 管理歌曲列表、扫描、搜索等功能
 */
class LibraryViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "LibraryViewModel"

    private val songDao: SongDao
    private val favoriteDao: FavoriteDao

    private val _allSongs = MutableStateFlow<List<Song>>(emptyList())
    val allSongs: StateFlow<List<Song>> = _allSongs.asStateFlow()

    private val _recentlyPlayed = MutableStateFlow<List<Song>>(emptyList())
    val recentlyPlayed: StateFlow<List<Song>> = _recentlyPlayed.asStateFlow()

    private val _favorites = MutableStateFlow<List<Song>>(emptyList())
    val favorites: StateFlow<List<Song>> = _favorites.asStateFlow()

    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Song>>(emptyList())
    val searchResults: StateFlow<List<Song>> = _searchResults.asStateFlow()

    init {
        val db = MusicDatabase.getInstance(application)
        songDao = db.songDao()
        favoriteDao = db.favoriteDao()

        // Observe all songs
        songDao.getAllSongs().onEach { songs ->
            _allSongs.value = songs
            Logger.d(TAG, "Loaded ${songs.size} songs from database")
        }.launchIn(viewModelScope)

        // Observe recently played
        songDao.getRecentlyPlayed().onEach { songs ->
            _recentlyPlayed.value = songs
        }.launchIn(viewModelScope)
    }

    fun scanMedia() {
        viewModelScope.launch {
            _isScanning.value = true
            Logger.i(TAG, "Starting media scan...")

            try {
                val songs = MediaScanner.scanAudioFiles(getApplication())
                if (songs.isNotEmpty()) {
                    songDao.insertSongs(songs)
                    Logger.i(TAG, "Inserted ${songs.size} songs into database")
                }
            } catch (e: Exception) {
                Logger.e(TAG, "Failed to scan media", e)
            } finally {
                _isScanning.value = false
            }
        }
    }

    fun searchSongs(query: String) {
        _searchQuery.value = query
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }

        viewModelScope.launch {
            songDao.searchSongs(query).onEach { songs ->
                _searchResults.value = songs
            }.launchIn(viewModelScope)
        }
    }

    fun getSongCount(): Int {
        return _allSongs.value.size
    }

    fun getArtists(): List<String> {
        return _allSongs.value.map { it.artist }.distinct().sorted()
    }

    fun getAlbums(): List<String> {
        return _allSongs.value.map { it.album }.distinct().sorted()
    }

    fun getSongsByArtist(artist: String): List<Song> {
        return _allSongs.value.filter { it.artist == artist }
    }

    fun getSongsByAlbum(album: String): List<Song> {
        return _allSongs.value.filter { it.album == album }
    }
}
