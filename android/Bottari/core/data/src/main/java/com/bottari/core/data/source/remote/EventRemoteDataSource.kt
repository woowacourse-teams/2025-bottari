package com.bottari.core.data.source.remote

import com.bottari.core.network.dto.sse.EventStateResponse
import kotlinx.coroutines.flow.Flow

interface EventRemoteDataSource {
    suspend fun connectEvent(memberId: Long): Flow<EventStateResponse>

    suspend fun disconnectEvent()
}
