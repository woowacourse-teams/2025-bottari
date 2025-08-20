package com.bottari.data.mapper

import com.bottari.data.model.sse.EventStateResponse
import com.bottari.domain.model.event.EventState

object EventMapper {
    fun EventStateResponse.toDomain(): EventState =
        when (this) {
            EventStateResponse.Empty -> EventState.Empty
            EventStateResponse.OnClosed -> EventState.OnClosed
            EventStateResponse.OnOpen -> EventState.OnOpen
            is EventStateResponse.OnFailure -> EventState.OnFailure(exception)
            is EventStateResponse.OnEventResponse ->
                EventState.OnEvent(
                    data.toDomain(),
                    publishedAt,
                )
        }
}
