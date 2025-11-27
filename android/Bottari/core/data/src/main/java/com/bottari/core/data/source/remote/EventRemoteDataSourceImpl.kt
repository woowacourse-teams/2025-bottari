package com.bottari.core.data.source.remote

import com.bottari.core.network.client.SSEClient
import com.bottari.core.network.dto.sse.EventStateResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EventRemoteDataSourceImpl @Inject constructor(
    private val client: SSEClient,
) : EventRemoteDataSource {
    override suspend fun connectEvent(memberId: Long): Flow<EventStateResponse> = client.connect(memberId)

    override suspend fun disconnectEvent() = client.disconnect()
}
