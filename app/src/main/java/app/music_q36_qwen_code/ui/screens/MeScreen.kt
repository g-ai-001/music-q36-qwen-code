package app.music_q36_qwen_code.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.music_q36_qwen_code.data.model.Song
import app.music_q36_qwen_code.ui.components.SongItem
import app.music_q36_qwen_code.viewmodel.LibraryViewModel

/**
 * 我的页面屏幕 - 完整版
 * 包含：Header卡片、统计网格、最近播放、自建歌单
 */
@Composable
fun MeScreen(
    libraryViewModel: LibraryViewModel = viewModel(),
    onSongClick: (Song) -> Unit
) {
    val songs by libraryViewModel.allSongs.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A2E))
    ) {
        // Header 卡片
        item {
            HeaderCard()
            Spacer(modifier = Modifier.height(16.dp))
        }

        // 统计网格
        item {
            StatsGrid(
                songCount = songs.size
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // 快捷操作
        item {
            QuickActions()
            Spacer(modifier = Modifier.height(16.dp))
        }

        // 最近播放
        item {
            SectionHeader("最近播放", "查看全部")
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(songs.take(10)) { song ->
                    RecentlyPlayedCard(
                        song = song,
                        onClick = { onSongClick(song) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // 自建歌单
        item {
            SectionHeader("自建歌单", "新建歌单")
        }

        items(listOf("我的最爱", "运动音乐", "工作放松", "睡眠轻音乐")) { playlistName ->
            PlaylistItemCard(
                name = playlistName,
                count = (0..20).random(),
                onClick = { /* TODO: 打开歌单 */ }
            )
        }

        // 底部留白
        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun HeaderCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFFA8E6CF),
                        Color(0xFF88D8A8),
                        Color(0xFF4CAF50)
                    )
                )
            )
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 用户头像
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // 用户信息
            Column {
                Text(
                    text = "本地用户",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF2E7D32))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "VIP",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // 右侧按钮
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                HeaderButton("签到", Icons.Default.CheckCircle)
                HeaderButton("积分", Icons.Default.Star)
            }
        }
    }
}

@Composable
fun HeaderButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.2f))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = text,
            color = Color.White,
            fontSize = 12.sp
        )
    }
}

@Composable
fun StatsGrid(songCount: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatItem(Icons.Default.Favorite, "收藏", "0")
        StatItem(Icons.Default.MusicNote, "本地", songCount.toString())
        StatItem(Icons.Default.Mic, "有声", "0")
        StatItem(Icons.Default.ShoppingBag, "已购", "0")
    }
}

@Composable
fun StatItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    count: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = count,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 12.sp
        )
    }
}

@Composable
fun QuickActions() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        QuickActionItem(Icons.Default.AutoAwesome, "会员中心")
        QuickActionItem(Icons.Default.Brush, "装扮")
        QuickActionItem(Icons.Default.FormatQuote, "日签")
        QuickActionItem(Icons.Default.GroupAdd, "关注")
    }
}

@Composable
fun QuickActionItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { /* TODO */ }
            .padding(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF81C784),
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = Color.White,
            fontSize = 11.sp
        )
    }
}

@Composable
fun SectionHeader(
    title: String,
    actionText: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = actionText,
            color = Color.Gray,
            fontSize = 12.sp,
            modifier = Modifier.clickable { /* TODO */ }
        )
    }
}

@Composable
fun RecentlyPlayedCard(
    song: Song,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(100.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF667eea),
                            Color(0xFF764ba2)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.MusicNote,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = song.title,
            color = Color.White,
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun PlaylistItemCard(
    name: String,
    count: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 歌单封面
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF434343),
                            Color(0xFF000000)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.QueueMusic,
                contentDescription = null,
                tint = Color(0xFF81C784),
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // 歌单信息
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$count 首",
                color = Color.Gray,
                fontSize = 12.sp
            )
        }

        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )
    }
}
