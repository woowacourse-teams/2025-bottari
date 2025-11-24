package com.bottari.core.network.dto.sse

import kotlinx.serialization.Serializable

@Serializable
enum class EventResponse {
    CREATE,
    DELETE,
    CHANGE,
    CHECK,
}
