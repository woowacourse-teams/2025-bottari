package com.bottari.data.source.remote

import com.bottari.data.model.sse.EventStateResponse
import kotlinx.coroutines.flow.Flow

interface SSERemoteDataSource {
    suspend fun connectEvent(teamBottariId: Long): Flow<EventStateResponse>
}
