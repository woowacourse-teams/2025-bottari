package com.bottari.logger

import com.bottari.logger.handler.LogHandler
import com.bottari.logger.model.CallerInfo
import com.bottari.logger.model.LogLevel
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BottariTree(
    private val handlers: List<LogHandler>,
) : Timber.Tree() {
    override fun log(
        priority: Int,
        tag: String?,
        message: String,
        t: Throwable?,
    ) {
        val level = LogLevel.fromTag(tag)
        if (level == LogLevel.UNKNOWN) return

        val callerInfo = CallerInfo.extract()
        val timestamp = currentTimestamp()

        handlers.forEach { handler ->
            handler.handleLog(level, message, callerInfo, timestamp, t)
        }
    }

    private fun currentTimestamp(): String = SimpleDateFormat(TIME_STAMP_FORMAT_PATTERN, Locale.KOREA).format(Date())

    companion object {
        private const val TIME_STAMP_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS"
    }
}
