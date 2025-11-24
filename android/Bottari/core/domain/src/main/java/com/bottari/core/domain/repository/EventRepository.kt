package com.bottari.core.domain.repository

import com.bottari.core.domain.model.event.EventState
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    suspend fun connectEvent(memberId: Long): Flow<EventState>

    suspend fun disconnectEvent()
}
