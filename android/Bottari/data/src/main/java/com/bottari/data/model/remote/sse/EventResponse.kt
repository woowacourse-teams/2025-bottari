package com.bottari.data.model.remote.sse

import kotlinx.serialization.Serializable

@Serializable
enum class EventResponse {
    CREATE,
    CHANGE,
    CHECK,
    DELETE,
}
