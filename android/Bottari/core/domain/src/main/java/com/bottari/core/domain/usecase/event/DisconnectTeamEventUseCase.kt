package com.bottari.core.domain.usecase.event

import com.bottari.core.domain.repository.EventRepository
import javax.inject.Inject

class DisconnectTeamEventUseCase @Inject constructor(
    private val eventRepository: EventRepository,
) {
    suspend operator fun invoke() = eventRepository.disconnectEvent()
}
