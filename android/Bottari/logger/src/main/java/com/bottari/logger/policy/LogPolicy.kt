package com.bottari.logger.policy

import com.bottari.logger.model.LogLevel

interface LogPolicy {
    fun shouldLogToConsole(level: LogLevel): Boolean

    fun shouldLogToAnalytics(level: LogLevel): Boolean

    fun shouldLogToCrashlytics(level: LogLevel): Boolean
}
