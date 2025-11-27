package com.bottari.core.domain.usecase.event

import com.bottari.core.domain.model.event.EventState
import com.bottari.core.domain.repository.EventRepository
import com.bottari.core.domain.repository.MemberRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ConnectTeamEventUseCase @Inject constructor(
    private val memberRepository: MemberRepository,
    private val eventRepository: EventRepository,
) {
    suspend operator fun invoke(): Flow<EventState> =
        memberRepository.getMemberId().fold(
            onSuccess = { memberId -> eventRepository.connectEvent(memberId) },
            onFailure = { exception -> flowOf(EventState.OnFailure(exception)) },
        )
}
