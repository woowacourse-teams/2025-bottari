package com.bottari.logger.policy

import com.bottari.logger.model.LogLevel

class DefaultLogPolicy(
    private val isDebug: Boolean,
) : LogPolicy {
    override fun shouldLogToConsole(level: LogLevel): Boolean = isDebug

    override fun shouldLogToAnalytics(level: LogLevel): Boolean = !isDebug && level.sendToAnalytics

    override fun shouldLogToCrashlytics(level: LogLevel): Boolean = !isDebug && level.sendToCrashlytics
}
