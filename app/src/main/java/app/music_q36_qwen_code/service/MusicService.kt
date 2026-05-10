package app.music_q36_qwen_code.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import app.music_q36_qwen_code.MainActivity
import app.music_q36_qwen_code.R
import app.music_q36_qwen_code.data.model.Song
import app.music_q36_qwen_code.utils.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 音乐播放服务 - 前台服务
 * 使用Media3 MediaSessionService实现锁屏控制和通知栏控制
 */
class MusicService : MediaSessionService() {
    private var mediaSession: MediaSession? = null
    private val serviceScope = CoroutineScope(Dispatchers.Main)

    companion object {
        private const val TAG = "MusicService"
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "music_playback_channel"
    }

    override fun onCreate() {
        super.onCreate()
        Logger.i(TAG, "MusicService created")

        // Create notification channel
        createNotificationChannel()

        // Initialize ExoPlayer
        val player = ExoPlayer.Builder(this).build()
        mediaSession = MediaSession.Builder(this, player).build()

        // Setup player listener
        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_ENDED -> {
                        Logger.i(TAG, "Playback ended")
                        PlayerManager.playNext()
                    }
                    Player.STATE_READY -> {
                        Logger.i(TAG, "Player ready")
                    }
                }
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                Logger.d(TAG, "Playing state changed: $isPlaying")
                PlayerManager.play()
                updateNotification()
            }
        })
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Logger.i(TAG, "MusicService started")
        return START_STICKY
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "音乐播放控制",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "显示当前播放的音乐信息和控制按钮"
            setShowBadge(false)
        }

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    private fun updateNotification() {
        val currentState = PlayerManager.playerState.value
        val song = currentState.currentSong ?: return

        val notification = createNotification(song, currentState.isPlaying)
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun createNotification(song: Song, isPlaying: Boolean): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val playPauseActionIcon = if (isPlaying) {
            android.R.drawable.ic_media_pause
        } else {
            android.R.drawable.ic_media_play
        }

        val builder = Notification.Builder(this, CHANNEL_ID)
            .setContentTitle(song.title)
            .setContentText(song.artist)
            .setSmallIcon(R.drawable.ic_music_note)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .addAction(
                android.R.drawable.ic_media_previous,
                "上一首",
                createPendingIntent(PlayerAction.PREVIOUS)
            )
            .addAction(
                playPauseActionIcon,
                if (isPlaying) "暂停" else "播放",
                createPendingIntent(PlayerAction.PLAY_PAUSE)
            )
            .addAction(
                android.R.drawable.ic_media_next,
                "下一首",
                createPendingIntent(PlayerAction.NEXT)
            )
            .setStyle(
                Notification.MediaStyle()
                    .setMediaSession(mediaSession?.sessionCompatToken)
                    .setShowActionsInCompactView(0, 1, 2)
            )

        return builder.build()
    }

    private fun createPendingIntent(action: PlayerAction): PendingIntent {
        val intent = Intent(this, MusicService::class.java).apply {
            putExtra("action", action.name)
        }
        return PendingIntent.getService(
            this,
            action.ordinal,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    enum class PlayerAction {
        PLAY_PAUSE, PREVIOUS, NEXT
    }

    override fun onDestroy() {
        Logger.i(TAG, "MusicService destroyed")
        mediaSession?.run {
            player.release()
            release()
        }
        mediaSession = null
        super.onDestroy()
    }
}
