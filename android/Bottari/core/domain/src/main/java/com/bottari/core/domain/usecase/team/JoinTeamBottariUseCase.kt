package com.bottari.core.domain.usecase.team

import com.bottari.core.domain.repository.TeamMemberRepository
import javax.inject.Inject

class JoinTeamBottariUseCase @Inject constructor(
    private val teamMemberRepository: TeamMemberRepository,
) {
    suspend operator fun invoke(inviteCode: String): Result<Unit> = teamMemberRepository.joinTeamBottari(inviteCode)
}
