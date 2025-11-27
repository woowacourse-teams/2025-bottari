package com.bottari.core.network.dto.sse

import com.bottari.core.domain.model.event.EventState
import java.time.LocalDateTime

sealed interface EventStateResponse {
    fun toDomain(): EventState

    data object Empty : EventStateResponse {
        override fun toDomain(): EventState = EventState.Empty
    }

    data object OnClosed : EventStateResponse {
        override fun toDomain(): EventState = EventState.OnClosed
    }

    data object OnOpen : EventStateResponse {
        override fun toDomain(): EventState = EventState.OnOpen
    }

    data class OnEventResponse(
        val resource: ResourceResponse,
        val event: EventResponse,
        val data: EventDataResponse,
        val publishedAt: LocalDateTime,
    ) : EventStateResponse {
        override fun toDomain(): EventState =
            EventState.OnEvent(
                data.toDomain(),
                publishedAt,
            )
    }

    data class OnFailure(
        val exception: Throwable?,
    ) : EventStateResponse {
        override fun toDomain(): EventState = EventState.OnFailure(exception)
    }
}
