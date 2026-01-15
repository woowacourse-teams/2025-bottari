package com.bottari.logger.util

import com.bottari.logger.model.LogEventData
import com.google.firebase.analytics.FirebaseAnalytics

fun FirebaseAnalytics.logEvent(
    event: LogEventData?,
    baseParams: Map<String, Any>,
) {
    val bundle =
        baseParams
            .toMutableMap()
            .apply { event?.params?.let { putAll(it) } }
            .toBundle()

    logEvent(event?.eventName ?: "Unknown", bundle)
}
