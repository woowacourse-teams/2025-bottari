package com.bottari.data.model.sse

import java.time.LocalDateTime

sealed interface SSEEventState {
    data object Empty : SSEEventState

    data object OnClosed : SSEEventState

    data object OnOpen : SSEEventState

    data class OnEvent(
        val resource: ResourceResponse,
        val event: EventResponse,
        val data: SSEDataResponse,
        val publishedAt: LocalDateTime,
    ) : SSEEventState

    data class OnFailure(
        val exception: Throwable?,
    ) : SSEEventState
}
