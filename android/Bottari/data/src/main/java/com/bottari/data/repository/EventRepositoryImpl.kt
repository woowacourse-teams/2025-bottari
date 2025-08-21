package com.bottari.data.repository

import com.bottari.data.mapper.EventMapper.toDomain
import com.bottari.data.source.remote.EventRemoteDataSource
import com.bottari.domain.model.event.EventState
import com.bottari.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class EventRepositoryImpl(
    private val eventRemoteDataSource: EventRemoteDataSource,
) : EventRepository {
    override suspend fun connectEvent(teamBottariId: Long): Flow<EventState> =
        eventRemoteDataSource.connectEvent(teamBottariId).map { eventStateResponse ->
            eventStateResponse.toDomain()
        }

    override suspend fun disconnectEvent() = eventRemoteDataSource.disconnectEvent()
}
