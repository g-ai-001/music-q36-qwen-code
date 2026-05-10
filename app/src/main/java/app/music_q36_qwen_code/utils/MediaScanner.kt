package app.music_q36_qwen_code.utils

import android.content.Context
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import app.music_q36_qwen_code.data.model.Song
import app.music_q36_qwen_code.utils.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 本地音乐扫描工具类
 * 从MediaStore扫描音频文件
 */
object MediaScanner {
    private const val TAG = "MediaScanner"

    private val AUDIO_MIME_TYPES = setOf(
        "audio/mpeg",
        "audio/mp3",
        "audio/mp4",
        "audio/x-ms-wma",
        "audio/aac",
        "audio/ogg",
        "audio/flac",
        "audio/wav",
        "audio/x-wav",
        "audio/3gpp",
        "audio/amr",
        "audio/midi",
        "audio/x-midi"
    )

    /**
     * 扫描所有音频文件
     * @param context Context
     * @param minDuration 最小时长（毫秒），用于过滤铃声
     * @return 歌曲列表
     */
    suspend fun scanAudioFiles(context: Context, minDuration: Long = Song.MIN_DURATION): List<Song> =
        withContext(Dispatchers.IO) {
            Logger.i(TAG, "Starting media scan...")
            val songs = mutableListOf<Song>()

            try {
                val projection = arrayOf(
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.ALBUM,
                    MediaStore.Audio.Media.DURATION,
                    MediaStore.Audio.Media.DATA,
                    MediaStore.Audio.Media.ALBUM_ID,
                    MediaStore.Audio.Media.SIZE,
                    MediaStore.Audio.Media.DATE_ADDED,
                    MediaStore.Audio.Media.MIME_TYPE
                )

                val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0 AND ${MediaStore.Audio.Media.DURATION} >= ?"
                val selectionArgs = arrayOf((minDuration / 1000).toString()) // MediaStore uses seconds

                val sortOrder = "${MediaStore.Audio.Media.DATE_ADDED} DESC"

                context.contentResolver.query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    selection,
                    selectionArgs,
                    sortOrder
                )?.use { cursor ->
                    val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                    val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                    val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                    val albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
                    val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
                    val pathColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
                    val albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
                    val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
                    val dateAddedColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED)

                    while (cursor.moveToNext()) {
                        try {
                            val id = cursor.getLong(idColumn)
                            val title = cursor.getString(titleColumn)
                            val artist = cursor.getString(artistColumn) ?: "未知艺术家"
                            val album = cursor.getString(albumColumn) ?: "未知专辑"
                            val duration = cursor.getLong(durationColumn)
                            val path = cursor.getString(pathColumn)
                            val albumId = cursor.getLong(albumIdColumn)
                            val size = cursor.getLong(sizeColumn)
                            val dateAdded = cursor.getLong(dateAddedColumn) * 1000 // Convert to milliseconds

                            val albumArtUri = "content://media/external/audio/albumart/$albumId"

                            songs.add(
                                Song(
                                    id = id,
                                    title = title,
                                    artist = artist,
                                    album = album,
                                    duration = duration,
                                    path = path,
                                    albumArtUri = albumArtUri,
                                    size = size,
                                    dateAdded = dateAdded
                                )
                            )
                        } catch (e: Exception) {
                            Logger.w(TAG, "Failed to parse song metadata at cursor position", e)
                        }
                    }
                }

                Logger.i(TAG, "Media scan completed. Found ${songs.size} songs.")
            } catch (e: Exception) {
                Logger.e(TAG, "Failed to scan media files", e)
            }

            songs
        }

    /**
     * 获取专辑封面URI
     */
    fun getAlbumArtUri(albumId: Long): String {
        return "content://media/external/audio/albumart/$albumId"
    }
}
