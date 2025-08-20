package com.bottari.data.source.remote

import com.bottari.data.model.sse.SSEEventState
import kotlinx.coroutines.flow.Flow

interface SSERemoteDataSource {
    suspend fun connectEvent(teamBottariId: Long): Flow<SSEEventState>
}
