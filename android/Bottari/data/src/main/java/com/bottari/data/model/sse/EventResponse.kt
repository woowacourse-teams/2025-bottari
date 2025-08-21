package com.bottari.data.model.sse

import kotlinx.serialization.Serializable

@Serializable
enum class EventResponse {
    CREATE,
    CHANGE,
    DELETE,
}
