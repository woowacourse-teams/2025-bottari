package com.bottari.data.source.remote

import com.bottari.data.model.remote.sse.EventStateResponse
import com.bottari.data.network.SSEClient
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EventRemoteDataSourceImpl @Inject constructor(
    private val client: SSEClient,
) : EventRemoteDataSource {
    override suspend fun connectEvent(memberId: Long): Flow<EventStateResponse> = client.connect(memberId)

    override suspend fun disconnectEvent() = client.disconnect()
}
