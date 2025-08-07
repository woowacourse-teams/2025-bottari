package com.bottari.logger.handler

import com.bottari.logger.model.CallerInfo
import com.bottari.logger.model.LogEventData
import com.bottari.logger.model.LogLevel
import com.bottari.logger.policy.LogPolicy
import com.bottari.logger.util.logEvent
import com.google.firebase.analytics.FirebaseAnalytics

class AnalyticsLogHandler(
    private val analytics: FirebaseAnalytics,
    private val policy: LogPolicy,
) : LogHandler {
    override fun handleLog(
        level: LogLevel,
        message: String,
        callerInfo: CallerInfo,
        timestamp: String,
        throwable: Throwable?,
    ) {
        if (!policy.shouldLogToAnalytics(level)) return

        val logEventData = LogEventData.deserialize(message)

        val baseParams =
            mapOf(
                PARAM_LOG_LEVEL to level.label,
                PARAM_MESSAGE to message,
                PARAM_LOCATION to callerInfo.display(),
                PARAM_TIMESTAMP to timestamp,
            )

        analytics.logEvent(logEventData, baseParams)
    }

    companion object {
        private const val PARAM_LOG_LEVEL = "LOG_LEVEL"
        private const val PARAM_MESSAGE = "LOG_MESSAGE"
        private const val PARAM_LOCATION = "LOG_LOCATION"
        private const val PARAM_TIMESTAMP = "LOG_TIMESTAMP"
    }
}
