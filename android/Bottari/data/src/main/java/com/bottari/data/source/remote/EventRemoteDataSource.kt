package com.bottari.data.source.remote

import com.bottari.data.model.remote.sse.EventStateResponse
import kotlinx.coroutines.flow.Flow

interface EventRemoteDataSource {
    suspend fun connectEvent(memberId: Long): Flow<EventStateResponse>

    suspend fun disconnectEvent()
}
