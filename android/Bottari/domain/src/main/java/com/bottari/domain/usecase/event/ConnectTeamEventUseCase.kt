package com.bottari.domain.usecase.event

import com.bottari.domain.model.event.EventState
import com.bottari.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow

class ConnectTeamEventUseCase(
    private val eventRepository: EventRepository,
) {
    suspend operator fun invoke(teamBottariId: Long): Flow<EventState> = eventRepository.connectEvent(teamBottariId)
}
