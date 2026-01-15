package com.bottari.logger.handler

import com.bottari.logger.model.CallerInfo
import com.bottari.logger.model.LogLevel
import com.bottari.logger.policy.LogPolicy
import com.google.firebase.crashlytics.FirebaseCrashlytics

class CrashlyticsLogHandler(
    private val crashlytics: FirebaseCrashlytics,
    private val policy: LogPolicy,
) : LogHandler {
    override fun handleLog(
        level: LogLevel,
        message: String,
        callerInfo: CallerInfo,
        timestamp: String,
        throwable: Throwable?,
    ) {
        if (!policy.shouldLogToCrashlytics(level)) return
        crashlytics.log("[${level.label}] $message @ ${callerInfo.display()} | $timestamp")
        throwable?.let { crashlytics.recordException(it) }
    }
}
