package com.bottari.data.source.remote

import com.bottari.data.model.remote.sse.EventStateResponse
import com.bottari.data.network.SSEClient
import kotlinx.coroutines.flow.Flow

class EventRemoteDataSourceImpl(
    private val client: SSEClient,
) : EventRemoteDataSource {
    override suspend fun connectEvent(teamBottariId: Long): Flow<EventStateResponse> = client.connect(teamBottariId)

    override suspend fun disconnectEvent() = client.disconnect()
}
