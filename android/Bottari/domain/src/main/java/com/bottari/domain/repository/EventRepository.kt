package com.bottari.domain.repository

import com.bottari.domain.model.event.EventState
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    suspend fun connectEvent(teamBottariId: Long): Flow<EventState>
}
