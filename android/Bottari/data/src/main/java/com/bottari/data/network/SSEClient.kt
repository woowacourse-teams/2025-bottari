package com.bottari.data.network

import com.bottari.data.model.sse.SSEEventState
import kotlinx.coroutines.flow.Flow

interface SSEClient {
    suspend fun connect(teamBottariId: Long): Flow<SSEEventState>

    suspend fun disconnect()
}
