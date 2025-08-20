package com.bottari.data.source.remote

import com.bottari.data.model.sse.EventStateResponse
import com.bottari.data.network.SSEClient
import kotlinx.coroutines.flow.Flow

class SSERemoteDataSourceImpl(
    private val client: SSEClient,
) : SSERemoteDataSource {
    override suspend fun connectEvent(teamBottariId: Long): Flow<EventStateResponse> = client.connect(teamBottariId)
}
