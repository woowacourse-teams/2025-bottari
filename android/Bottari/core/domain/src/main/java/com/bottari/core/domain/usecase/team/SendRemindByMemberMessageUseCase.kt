package com.bottari.core.domain.usecase.team

import com.bottari.core.domain.repository.TeamMemberRepository
import javax.inject.Inject

class SendRemindByMemberMessageUseCase @Inject constructor(
    private val teamMemberRepository: TeamMemberRepository,
) {
    suspend operator fun invoke(
        teamBottariId: Long,
        memberId: Long,
    ): Result<Unit> = teamMemberRepository.sendRemindByMemberMessage(teamBottariId, memberId)
}
