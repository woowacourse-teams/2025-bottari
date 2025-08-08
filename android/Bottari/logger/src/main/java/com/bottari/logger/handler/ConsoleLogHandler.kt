package com.bottari.logger.handler

import android.util.Log
import com.bottari.logger.model.CallerInfo
import com.bottari.logger.model.LogLevel
import com.bottari.logger.policy.LogPolicy

class ConsoleLogHandler(
    private val policy: LogPolicy,
) : LogHandler {
    override fun handleLog(
        level: LogLevel,
        message: String,
        callerInfo: CallerInfo,
        timestamp: String,
        throwable: Throwable?,
    ) {
        if (!policy.shouldLogToConsole(level)) return
        val fullLog =
            formatLogMessage(level, message, Thread.currentThread().name, timestamp, callerInfo)

        logToConsole(level, fullLog)
    }

    private fun logToConsole(
        level: LogLevel,
        fullLog: String,
    ) {
        if (!policy.shouldLogToConsole(level)) return
        val lines = fullLog.lines()

        lines.forEach { line ->
            if (line.length > MAX_LOG_LENGTH) {
                logLineFromChunked(level, line)
                return@forEach
            }

            Log.println(level.priority, LOG_NAME, line)
        }
    }

    private fun logLineFromChunked(
        level: LogLevel,
        line: String,
    ) {
        line.chunked(MAX_LOG_LENGTH).forEach { chunk ->
            Log.println(level.priority, LOG_NAME, chunk)
        }
    }

    private fun formatLogMessage(
        logLevel: LogLevel,
        message: String,
        threadName: String,
        timestamp: String,
        callerInfo: CallerInfo,
    ) = when (logLevel) {
        LogLevel.NETWORK ->
            buildNetworkLog(
                logLevel.label,
                message,
                threadName,
                timestamp,
                callerInfo,
            )

        LogLevel.LIFECYCLE -> buildLifecycleLog(logLevel.label, message, callerInfo)
        else -> buildDefaultLog(logLevel.label, message, threadName, timestamp, callerInfo)
    }

    private fun buildDefaultLog(
        tag: String,
        message: String,
        threadName: String,
        timestamp: String,
        callerInfo: CallerInfo,
    ): String =
        buildString {
            appendLine(LOG_SEPARATOR)
            appendLine(message)
            appendLine(LOG_SEPARATOR)
            appendLine(formatMetaInfo(tag, threadName, timestamp, callerInfo))
            append(LOG_SEPARATOR)
        }

    private fun buildNetworkLog(
        tag: String,
        message: String,
        threadName: String,
        timestamp: String,
        callerInfo: CallerInfo,
    ) = buildString {
        appendLine(SHORT_LOG_SEPARATOR)
        appendLine(
            message
                .replace(NETWORK_REQUEST_PREFIX, NETWORK_REQUEST_REPLACE_PREFIX)
                .replace(NETWORK_RESPONSE_PREFIX, NETWORK_RESPONSE_REPLACE_PREFIX),
        )
        appendLine(SHORT_LOG_SEPARATOR)
        appendLine(formatMetaInfo(tag, threadName, timestamp, callerInfo))
        append(SHORT_LOG_SEPARATOR)
    }

    private fun buildLifecycleLog(
        tag: String,
        message: String,
        callerInfo: CallerInfo,
    ): String =
        buildString {
            appendLine(SHORT_LOG_SEPARATOR)
            appendLine("[$tag] $message | ${callerInfo.methodName}")
            append(SHORT_LOG_SEPARATOR)
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

    companion object {
        private const val LOG_NAME = "[BOTTARI_LOG]"
        private const val MAX_LOG_LENGTH = 4000

        private const val SHORT_LOG_SEPARATOR =
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
        private const val LOG_SEPARATOR =
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

        private const val NETWORK_REQUEST_PREFIX = "-->"
        private const val NETWORK_RESPONSE_PREFIX = "<--"
        private const val NETWORK_REQUEST_REPLACE_PREFIX = "[REQUEST]"
        private const val NETWORK_RESPONSE_REPLACE_PREFIX = "[RESPONSE]"
    }
}
