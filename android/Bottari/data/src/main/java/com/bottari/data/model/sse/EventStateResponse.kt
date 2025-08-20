package com.bottari.data.model.sse

import java.time.LocalDateTime

sealed interface EventStateResponse {
    data object Empty : EventStateResponse

    data object OnClosed : EventStateResponse

    data object OnOpen : EventStateResponse

    data class OnEventResponse(
        val resource: ResourceResponse,
        val event: EventResponse,
        val data: EventDataResponse,
        val publishedAt: LocalDateTime,
    ) : EventStateResponse

    data class OnFailure(
        val exception: Throwable?,
    ) : EventStateResponse
}
