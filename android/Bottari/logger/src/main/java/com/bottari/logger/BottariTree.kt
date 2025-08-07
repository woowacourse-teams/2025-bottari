package com.bottari.logger

import android.util.Log
import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BottariTree(
    userId: String,
    private val analytics: FirebaseAnalytics,
    private val crashlytics: FirebaseCrashlytics,
    private val minLogPriority: Int = if (BuildConfig.DEBUG) Log.VERBOSE else Log.INFO,
) : Timber.Tree() {
    init {
        crashlytics.isCrashlyticsCollectionEnabled = !BuildConfig.DEBUG
        crashlytics.setUserId(userId)
        analytics.setUserId(userId)
    }

    override fun isLoggable(
        tag: String?,
        priority: Int,
    ): Boolean = priority >= minLogPriority

    override fun log(
        priority: Int,
        tag: String?,
        message: String,
        t: Throwable?,
    ) {
        if (priority < minLogPriority) return

        val logLevel = LogLevel.fromTag(tag)
        if (logLevel == LogLevel.UNKNOWN) {
            Log.println(priority, tag, message)
            return
        }

        val timestamp = dateFormat.format(Date())
        val threadName = Thread.currentThread().name
        val callerInfo = CallerInfo.extract()

        val fullLog = buildLogMessage(logLevel, message, threadName, timestamp, callerInfo)

        logToConsole(priority, fullLog)

        if (logLevel.sendToCrashlytics) {
            logToCrashlytics(fullLog, t)
        }

        if (logLevel.sendToAnalytics) {
            logToAnalytics(logLevel.label, message, callerInfo, timestamp)
        }
    }

    private fun buildLogMessage(
        logLevel: LogLevel,
        message: String,
        threadName: String,
        timestamp: String,
        callerInfo: CallerInfo,
    ): String =
        when (logLevel) {
            LogLevel.NETWORK ->
                buildNetworkLog(
                    logLevel,
                    message,
                    threadName,
                    timestamp,
                    callerInfo,
                )

            LogLevel.LIFECYCLE -> buildLifecycleLog(logLevel, message, callerInfo)
            else -> buildDefaultLog(logLevel, message, threadName, timestamp, callerInfo)
        }

    private fun buildNetworkLog(
        logLevel: LogLevel,
        message: String,
        threadName: String,
        timestamp: String,
        callerInfo: CallerInfo,
    ): String =
        buildString {
            appendLine(SHORT_LOG_SEPARATOR)
            appendLine(convertNetworkKeywords(message))
            appendLine(SHORT_LOG_SEPARATOR)
            appendLine(formatMetaInfo(logLevel.label, threadName, timestamp, callerInfo))
            append(SHORT_LOG_SEPARATOR)
        }

    private fun buildLifecycleLog(
        logLevel: LogLevel,
        message: String,
        callerInfo: CallerInfo,
    ): String =
        buildString {
            appendLine(SHORT_LOG_SEPARATOR)
            appendLine("[${logLevel.label}] $message | ${callerInfo.methodName}")
            appendLine(SHORT_LOG_SEPARATOR)
        }

    private fun buildDefaultLog(
        logLevel: LogLevel,
        message: String,
        threadName: String,
        timestamp: String,
        callerInfo: CallerInfo,
    ): String =
        buildString {
            appendLine(LOG_SEPARATOR)
            appendLine(message)
            appendLine(LOG_SEPARATOR)
            appendLine(formatMetaInfo(logLevel.label, threadName, timestamp, callerInfo))
            append(LOG_SEPARATOR)
        }

    private fun formatMetaInfo(
        tag: String,
        threadName: String,
        timestamp: String,
        callerInfo: CallerInfo,
    ): String =
        buildString {
            append(" Type: $tag")
            if (tag != LogLevel.NETWORK.label) {
                append("\t|\tThread: $threadName")
                append("\t|\tMethod: ${callerInfo.methodName}()")
                append("\t|\t${callerInfo.display()}")
            }
            append("\t|\t$timestamp")
        }

    private fun convertNetworkKeywords(message: String): String =
        message
            .replace(NETWORK_REQUEST_PREFIX, NETWORK_REQUEST_REPLACE_PREFIX)
            .replace(NETWORK_RESPONSE_PREFIX, NETWORK_RESPONSE_REPLACE_PREFIX)

    private fun logToConsole(
        priority: Int,
        log: String,
    ) {
        if (log.length <= MAX_LOG_LENGTH) {
            Log.println(priority, LOG_NAME, log)
            return
        }

        log.chunked(MAX_LOG_LENGTH).forEach { Log.println(priority, LOG_NAME, it) }
    }

    private fun logToCrashlytics(
        log: String,
        t: Throwable?,
    ) {
        crashlytics.log(log)
        t?.let { crashlytics.recordException(it) }
    }

    private fun logToAnalytics(
        tag: String,
        message: String,
        callerInfo: CallerInfo,
        timestamp: String,
    ) {
        analytics.logEvent(
            EVENT_CUSTOM_LOG,
            bundleOf(
                PARAM_TAG to tag,
                PARAM_MESSAGE to message,
                PARAM_LOCATION to callerInfo.display(),
                PARAM_TIMESTAMP to timestamp,
            ),
        )
    }

    companion object {
        private const val LOG_NAME = "[BOTTARI_LOG]"
        private const val MAX_LOG_LENGTH = 4000

        private const val SHORT_LOG_SEPARATOR =
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
        private const val LOG_SEPARATOR =
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

        private const val EVENT_CUSTOM_LOG = "BOTTARI_EVENT_LOG"
        private const val PARAM_TAG = "LOG_LEVEL"
        private const val PARAM_MESSAGE = "LOG_MESSAGE"
        private const val PARAM_LOCATION = "LOG_LOCATION"
        private const val PARAM_TIMESTAMP = "LOG_TIMESTAMP"

        private const val NETWORK_REQUEST_PREFIX = "-->"
        private const val NETWORK_RESPONSE_PREFIX = "<--"
        private const val NETWORK_REQUEST_REPLACE_PREFIX = "[REQUEST]"
        private const val NETWORK_RESPONSE_REPLACE_PREFIX = "[RESPONSE]"

        private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.KOREA)
    }
}
