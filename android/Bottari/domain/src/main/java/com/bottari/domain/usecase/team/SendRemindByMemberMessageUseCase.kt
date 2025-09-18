package com.bottari.domain.usecase.team

import com.bottari.domain.model.exception.BottariResult
import com.bottari.domain.repository.TeamBottariRepository

class SendRemindByMemberMessageUseCase(
    private val teamBottariRepository: TeamBottariRepository,
) {
    suspend operator fun invoke(
        teamBottariId: Long,
        memberId: Long,
    ): BottariResult<Unit> = teamBottariRepository.sendRemindByMemberMessage(teamBottariId, memberId)
}
