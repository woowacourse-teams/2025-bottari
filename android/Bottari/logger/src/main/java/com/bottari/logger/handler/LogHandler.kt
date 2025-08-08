package com.bottari.logger.handler

import com.bottari.logger.model.CallerInfo
import com.bottari.logger.model.LogLevel

interface LogHandler {
    fun handleLog(
        level: LogLevel,
        message: String,
        callerInfo: CallerInfo,
        timestamp: String,
        throwable: Throwable?,
    )
}
