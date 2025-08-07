package com.bottari.logger

import android.util.Log

sealed class LogLevel(
    val label: String,
    val priority: Int,
    val sendToAnalytics: Boolean = false,
    val sendToCrashlytics: Boolean = false,
) {
    data object UI : LogLevel("UI", Log.DEBUG, sendToAnalytics = true)

    data object DATA : LogLevel("DATA", Log.DEBUG)

    data object NETWORK : LogLevel("NETWORK", Log.DEBUG)

    data object GLOBAL : LogLevel("GLOBAL", Log.INFO, sendToCrashlytics = true)

    data object ERROR :
        LogLevel("ERROR", Log.ERROR, sendToCrashlytics = true)

    data object DEBUG : LogLevel("DEBUG", Log.VERBOSE)

    data object LIFECYCLE : LogLevel("LIFECYCLE", Log.INFO)

    data object UNKNOWN : LogLevel("UNKNOWN", Log.VERBOSE)

    companion object {
        fun fromTag(tag: String?): LogLevel =
            when (tag) {
                UI.label -> UI
                DATA.label -> DATA
                NETWORK.label -> NETWORK
                GLOBAL.label -> GLOBAL
                ERROR.label -> ERROR
                DEBUG.label -> DEBUG
                LIFECYCLE.label -> LIFECYCLE
                else -> UNKNOWN
            }
    }
}
