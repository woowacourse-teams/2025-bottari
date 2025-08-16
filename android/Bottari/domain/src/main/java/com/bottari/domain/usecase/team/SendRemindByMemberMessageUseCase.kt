package com.bottari.domain.usecase.team

import com.bottari.domain.repository.TeamBottariRepository

class SendRemindByMemberMessageUseCase(
    private val teamBottariRepository: TeamBottariRepository,
) {
    suspend operator fun invoke(
        teamBottariId: Long,
        memberId: Long,
    ): Result<Unit> = teamBottariRepository.sendRemindByMemberMessage(teamBottariId, memberId)
}
