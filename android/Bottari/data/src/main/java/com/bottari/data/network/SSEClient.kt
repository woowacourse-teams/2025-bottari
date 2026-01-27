package com.bottari.data.network

import com.bottari.data.model.remote.sse.EventStateResponse
import kotlinx.coroutines.flow.Flow

interface SSEClient {
    fun connect(memberId: Long): Flow<EventStateResponse>

    fun disconnect()
}
