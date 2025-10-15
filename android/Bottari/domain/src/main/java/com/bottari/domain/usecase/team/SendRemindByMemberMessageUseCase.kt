package com.bottari.domain.usecase.team

import com.bottari.domain.repository.TeamMemberRepository
import javax.inject.Inject

class SendRemindByMemberMessageUseCase @Inject constructor(
    private val teamMemberRepository: TeamMemberRepository,
) {
    suspend operator fun invoke(
        teamBottariId: Long,
        memberId: Long,
    ): Result<Unit> = teamMemberRepository.sendRemindByMemberMessage(teamBottariId, memberId)
}
