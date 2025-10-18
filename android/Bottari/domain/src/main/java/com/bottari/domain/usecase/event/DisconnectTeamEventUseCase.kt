package com.bottari.domain.usecase.event

import com.bottari.domain.repository.EventRepository
import javax.inject.Inject

class DisconnectTeamEventUseCase @Inject constructor(
    private val eventRepository: EventRepository,
) {
    suspend operator fun invoke() = eventRepository.disconnectEvent()
}
