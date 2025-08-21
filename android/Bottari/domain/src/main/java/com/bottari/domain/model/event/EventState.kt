package com.bottari.domain.model.event

import java.time.LocalDateTime

sealed interface EventState {
    data object Empty : EventState

    data object OnClosed : EventState

    data object OnOpen : EventState

    data class OnEvent(
        val data: EventData,
        val publishedAt: LocalDateTime,
    ) : EventState

    data class OnFailure(
        val exception: Throwable?,
    ) : EventState
}
