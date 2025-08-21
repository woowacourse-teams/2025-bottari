package com.bottari.domain.usecase.event

import com.bottari.domain.repository.EventRepository

class DisconnectEventUseCase(
    private val eventRepository: EventRepository,
) {
    suspend operator fun invoke() = eventRepository.disconnectEvent()
}
