package app.music_q36_qwen_code.utils

import android.content.Context
import android.util.Log
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 日志系统 - 将日志保存到外部存储目录
 * 日志文件保存在 context.getExternalFilesDir(null) 目录下
 */
object Logger {
    private const val TAG = "LocalMusicLogger"
    private const val LOG_FILE_PREFIX = "music_player_"
    private const val LOG_FILE_EXTENSION = ".log"
    private const val MAX_LOG_FILE_SIZE = 5 * 1024 * 1024 // 5MB
    private const val MAX_LOG_FILES = 3

    private var logFile: File? = null
    private var logWriter: PrintWriter? = null
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())
    private val fileDateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())

    private var isInitialized = false

    enum class Level {
        DEBUG, INFO, WARN, ERROR
    }

    fun init(context: Context) {
        if (isInitialized) return

        val logDir = File(context.getExternalFilesDir(null), "logs")
        if (!logDir.exists()) {
            logDir.mkdirs()
        }

        cleanupOldLogFiles(logDir)

        val fileName = LOG_FILE_PREFIX + fileDateFormat.format(Date()) + LOG_FILE_EXTENSION
        logFile = File(logDir, fileName)

        try {
            logWriter = PrintWriter(FileWriter(logFile, true), true)
            isInitialized = true
            log(Level.INFO, TAG, "Logger initialized: ${logFile?.absolutePath}")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize logger", e)
            isInitialized = false
        }
    }

    private fun cleanupOldLogFiles(logDir: File) {
        try {
            val logFiles = logDir.listFiles { file ->
                file.name.startsWith(LOG_FILE_PREFIX) && file.name.endsWith(LOG_FILE_EXTENSION)
            }?.sortedBy { it.lastModified() } ?: return

            while (logFiles.size > MAX_LOG_FILES) {
                logFiles.removeAt(0).delete()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to cleanup old log files", e)
        }
    }

    private fun checkLogFileRotation() {
        val currentFile = logFile ?: return
        if (currentFile.exists() && currentFile.length() > MAX_LOG_FILE_SIZE) {
            try {
                logWriter?.close()
                cleanupOldLogFiles(currentFile.parentFile ?: return)

                val newFileName = LOG_FILE_PREFIX + fileDateFormat.format(Date()) + "_${System.currentTimeMillis()}" + LOG_FILE_EXTENSION
                logFile = File(currentFile.parentFile, newFileName)
                logWriter = PrintWriter(FileWriter(logFile, true), true)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to rotate log file", e)
            }
        }
    }

    private fun log(level: Level, tag: String, message: String, throwable: Throwable? = null) {
        val timestamp = dateFormat.format(Date())
        val logMessage = "[$timestamp] [${level.name}] [$tag] $message"

        when (level) {
            Level.DEBUG -> Log.d(tag, message, throwable)
            Level.INFO -> Log.i(tag, message, throwable)
            Level.WARN -> Log.w(tag, message, throwable)
            Level.ERROR -> Log.e(tag, message, throwable)
        }

        if (isInitialized) {
            checkLogFileRotation()
            logWriter?.println(logMessage)
            throwable?.let {
                it.printStackTrace(logWriter)
                logWriter?.flush()
            }
        }
    }

    fun d(tag: String, message: String) = log(Level.DEBUG, tag, message)
    fun i(tag: String, message: String) = log(Level.INFO, tag, message)
    fun w(tag: String, message: String) = log(Level.WARN, tag, message)
    fun w(tag: String, message: String, throwable: Throwable) = log(Level.WARN, tag, message, throwable)
    fun e(tag: String, message: String) = log(Level.ERROR, tag, message)
    fun e(tag: String, message: String, throwable: Throwable) = log(Level.ERROR, tag, message, throwable)

    fun getLogFiles(context: Context): List<File> {
        val logDir = File(context.getExternalFilesDir(null), "logs")
        return logDir.listFiles { file ->
            file.name.startsWith(LOG_FILE_PREFIX) && file.name.endsWith(LOG_FILE_EXTENSION)
        }?.toList() ?: emptyList()
    }

    fun close() {
        logWriter?.close()
        logWriter = null
        isInitialized = false
    }
}
