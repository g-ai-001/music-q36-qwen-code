package app.music_q36_qwen_code

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import app.music_q36_qwen_code.service.MusicService
import app.music_q36_qwen_code.ui.components.MiniPlayer
import app.music_q36_qwen_code.ui.screens.HomeScreen
import app.music_q36_qwen_code.ui.screens.MeScreen
import app.music_q36_qwen_code.ui.screens.PlayerScreen
import app.music_q36_qwen_code.ui.theme.MusicPlayerTheme
import app.music_q36_qwen_code.utils.Logger
import app.music_q36_qwen_code.viewmodel.LibraryViewModel
import app.music_q36_qwen_code.viewmodel.PlayerViewModel

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        if (allGranted) {
            Logger.i(TAG, "All permissions granted")
        } else {
            Logger.w(TAG, "Some permissions denied")
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize logger
        Logger.init(applicationContext)
        Logger.i(TAG, "App starting")

        // Start music service
        startService(Intent(this, MusicService::class.java))

        // Request permissions
        requestPermissions()

        // Set up UI
        setContent {
            MusicPlayerTheme {
                MainScreen()
            }
        }
    }

    private fun requestPermissions() {
        val permissions = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.READ_MEDIA_AUDIO)
        } else {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        if (permissions.isNotEmpty()) {
            requestPermissionLauncher.launch(permissions.toTypedArray())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Logger.i(TAG, "MainActivity destroyed")
        Logger.close()
    }
}

@Composable
fun MainScreen(
    libraryViewModel: LibraryViewModel = viewModel(),
    playerViewModel: PlayerViewModel = viewModel()
) {
    val navController = rememberNavController()
    var selectedIndex by remember { mutableIntStateOf(0) }
    var showPlayerScreen by remember { mutableStateOf(false) }

    val playerState by playerViewModel.playerState.collectAsState()
    val lyrics by playerViewModel.lyrics.collectAsState()
    val isFavorite by playerViewModel.isFavorite.collectAsState()

    if (showPlayerScreen) {
        PlayerScreen(
            currentSong = playerState.currentSong,
            isPlaying = playerState.isPlaying,
            currentPosition = playerState.currentPosition,
            duration = playerState.duration,
            lyrics = lyrics.map { it.time to it.text },
            onPlayPauseClick = { playerViewModel.togglePlayPause() },
            onNextClick = { playerViewModel.playNext() },
            onPreviousClick = { playerViewModel.playPrevious() },
            onSeekTo = { position -> playerViewModel.seekTo(position) },
            onBackClick = { showPlayerScreen = false },
            onFavoriteClick = { playerViewModel.toggleFavorite() },
            isFavorite = isFavorite
        )
        return
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Column {
                // Mini Player
                if (playerState.currentSong != null) {
                    MiniPlayer(
                        currentSong = playerState.currentSong,
                        isPlaying = playerState.isPlaying,
                        onPlayPauseClick = { playerViewModel.togglePlayPause() },
                        onExpandClick = { showPlayerScreen = true }
                    )
                }

                // Navigation Bar
                NavigationBar {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = "首页") },
                        label = { Text("首页") },
                        selected = selectedIndex == 0,
                        onClick = {
                            selectedIndex = 0
                            navController.navigate("home") {
                                popUpTo("home") { inclusive = true }
                            }
                        }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Person, contentDescription = "我的") },
                        label = { Text("我的") },
                        selected = selectedIndex == 1,
                        onClick = {
                            selectedIndex = 1
                            navController.navigate("me") {
                                popUpTo("me") { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(
                navController = navController,
                startDestination = "home"
            ) {
                composable("home") {
                    HomeScreen(
                        libraryViewModel = libraryViewModel,
                        playerViewModel = playerViewModel,
                        onSongClick = { song ->
                            playerViewModel.playSong(song)
                        }
                    )
                }
                composable("me") {
                    MeScreen(
                        libraryViewModel = libraryViewModel,
                        onSongClick = { song ->
                            playerViewModel.playSong(song)
                        }
                    )
                }
            }
        }
    }
}
