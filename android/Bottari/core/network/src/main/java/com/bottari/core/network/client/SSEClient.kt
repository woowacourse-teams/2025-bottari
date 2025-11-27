package com.bottari.core.network.client

import com.bottari.core.network.dto.sse.EventStateResponse
import kotlinx.coroutines.flow.Flow

interface SSEClient {
    fun connect(memberId: Long): Flow<EventStateResponse>

    fun disconnect()
}
