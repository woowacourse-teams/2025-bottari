package com.bottari.core.data.repository

import com.bottari.core.data.source.remote.EventRemoteDataSource
import com.bottari.core.domain.model.event.EventState
import com.bottari.core.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val eventRemoteDataSource: EventRemoteDataSource,
) : EventRepository {
    override suspend fun connectEvent(memberId: Long): Flow<EventState> =
        eventRemoteDataSource.connectEvent(memberId).map { eventStateResponse ->
            eventStateResponse.toDomain()
        }

    override suspend fun disconnectEvent() = eventRemoteDataSource.disconnectEvent()
}
