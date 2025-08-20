package com.bottari.data.network

import com.bottari.data.model.sse.EventStateResponse
import kotlinx.coroutines.flow.Flow

interface SSEClient {
    suspend fun connect(teamBottariId: Long): Flow<EventStateResponse>

    suspend fun disconnect()
}
