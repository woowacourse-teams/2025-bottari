package com.bottari.data.model.remote.sse

import kotlinx.serialization.Serializable

@Serializable
enum class EventResponse {
    CREATE,
    DELETE,
    CHANGE,
    CHECK,
}
