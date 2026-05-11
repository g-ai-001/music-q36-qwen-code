package app.music_q36_qwen_code.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.music_q36_qwen_code.data.model.Song
import app.music_q36_qwen_code.ui.components.SectionHeader
import app.music_q36_qwen_code.ui.components.SongItem
import app.music_q36_qwen_code.ui.theme.*
import app.music_q36_qwen_code.viewmodel.LibraryViewModel
import app.music_q36_qwen_code.viewmodel.PlayerViewModel

/**
 * 首页屏幕 - 完整版
 * 包含：搜索栏、Tab布局、最近播放、本地歌单、歌曲列表
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
    val scanProgress by libraryViewModel.scanProgress.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedTab by remember { mutableIntStateOf(0) }
    var showSortMenu by remember { mutableStateOf(false) }

    val tabs = listOf("推荐", "歌单", "歌手", "专辑")
    val sortOptions = listOf("添加时间", "歌曲名", "艺术家", "时长")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        DarkOverlay,
                        CardBackground
                    )
                )
            )
    ) {
        // 顶部栏
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "本地音乐",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Row {
                IconButton(onClick = { libraryViewModel.scanMediaWithProgress() }) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "扫描音乐",
                        tint = Color.White
                    )
                }
                IconButton(onClick = { showSortMenu = true }) {
                    Icon(
                        imageVector = Icons.Default.Sort,
                        contentDescription = "排序",
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
        }

        // 排序进度提示
        if (isScanning && scanProgress != null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                LinearProgressIndicator(
                    progress = { scanProgress!!.percentage / 100f },
                    modifier = Modifier.fillMaxWidth(),
                    color = ButtonGreen
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = scanProgress!!.message,
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }

        // 搜索栏
        TextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                libraryViewModel.searchSongs(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(24.dp)),
            placeholder = { Text("搜索本地音乐...", color = Color.Gray) },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray)
            },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = CardBackground,
                focusedContainerColor = CardBackground,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(24.dp)
        )

        // Tab 布局
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            modifier = Modifier.fillMaxWidth(),
            edgePadding = 16.dp,
            containerColor = Color.Transparent,
            contentColor = ButtonGreen,
            indicator = { tabPositions ->
                if (selectedTab < tabPositions.size) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(x = tabPositions[selectedTab].left)
                            .width(tabPositions[selectedTab].width)
                            .height(3.dp)
                            .background(
                                color = ButtonGreen,
                                shape = RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp)
                            )
                    )
                }
            },
            divider = {}
        ) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = tab,
                            color = if (selectedTab == index) Color.White else Color.Gray,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }

        // 内容区域
        Box(modifier = Modifier.fillMaxSize().weight(1f)) {
            if (isScanning) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xFF81C784)
                )
            } else if (songs.isEmpty()) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.MusicOff,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "暂无音乐",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "点击右上角扫描添加音乐",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 8.dp)
                ) {
                    // 最近播放
                    item {
                        SectionHeader("最近播放", "查看更多")
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(songs.take(10)) { song ->
                                app.music_q36_qwen_code.ui.components.RecentlyPlayedItem(
                                    song = song,
                                    onClick = { onSongClick(song) }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // 本地歌单
                    item {
                        SectionHeader("本地歌单", "查看更多")
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                listOf(
                                    PlaylistItem("所有歌曲", songs.size.toString(), Icons.Default.MusicNote),
                                    PlaylistItem("我的收藏", "0", Icons.Default.Favorite),
                                    PlaylistItem("新建歌单", "+", Icons.Default.Add)
                                )
                            ) { playlist ->
                                PlaylistCard(
                                    playlist = playlist,
                                    onClick = { /* TODO: 打开歌单 */ }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // 歌曲列表
                    item {
                        SectionHeader("歌曲列表", "查看更多")
                        
                        // 排序菜单
                        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                            DropdownMenu(
                                expanded = showSortMenu,
                                onDismissRequest = { showSortMenu = false }
                            ) {
                                sortOptions.forEachIndexed { index, option ->
                                    DropdownMenuItem(
                                        text = { Text(option) },
                                        onClick = {
                                            val field = when (index) {
                                                0 -> "dateAdded"
                                                1 -> "title"
                                                2 -> "artist"
                                                3 -> "duration"
                                                else -> "dateAdded"
                                            }
                                            libraryViewModel.setSortField(field)
                                            showSortMenu = false
                                        }
                                    )
                                }
                            }
                        }
                    }

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

data class PlaylistItem(
    val name: String,
    val count: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun PlaylistCard(
    playlist: PlaylistItem,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(120.dp)
            .height(80.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        CardGradientStart,
                        CardGradientEnd
                    )
                )
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = playlist.icon,
                contentDescription = null,
                tint = ButtonGreen,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = playlist.name,
                color = Color.White,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${playlist.count}首",
                color = Color.Gray,
                fontSize = 10.sp
            )
        }
    }
}
