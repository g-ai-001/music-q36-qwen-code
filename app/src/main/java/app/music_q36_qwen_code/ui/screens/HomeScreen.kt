package app.music_q36_qwen_code.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.music_q36_qwen_code.data.model.Song
import app.music_q36_qwen_code.ui.components.SongItem
import app.music_q36_qwen_code.viewmodel.LibraryViewModel
import app.music_q36_qwen_code.viewmodel.PlayerViewModel

/**
 * 首页屏幕
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    libraryViewModel: LibraryViewModel = viewModel(),
    playerViewModel: PlayerViewModel = viewModel(),
    onSongClick: (Song) -> Unit
) {
    val songs by libraryViewModel.allSongs.collectAsState()
    val isScanning by libraryViewModel.isScanning.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1A1A2E),
                        Color(0xFF2D2D44)
                    )
                )
            )
    ) {
        // 顶部栏
        TopAppBar(
            title = { Text("本地音乐", color = Color.White) },
            actions = {
                IconButton(onClick = { libraryViewModel.scanMedia() }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "扫描音乐",
                        tint = Color.White
                    )
                }
                IconButton(onClick = { /* TODO: 设置 */ }) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "设置",
                        tint = Color.White
                    )
                }
            }
        )

        // 搜索栏
        TextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                libraryViewModel.searchSongs(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            placeholder = { Text("搜索本地音乐...", color = Color.Gray) },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray)
            }
        )

        // 内容区域
        Box(modifier = Modifier.fillMaxSize()) {
            if (isScanning) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White
                )
            } else if (songs.isEmpty()) {
                Text(
                    text = "暂无音乐，点击右上角扫描添加音乐",
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(32.dp)
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 8.dp)
                ) {
                    items(songs) { song ->
                        SongItem(
                            song = song,
                            onClick = { onSongClick(song) }
                        )
                    }
                }
            }
        }
    }
}
