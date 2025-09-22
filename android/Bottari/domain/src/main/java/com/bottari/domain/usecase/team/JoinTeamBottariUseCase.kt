package com.bottari.domain.usecase.team

import com.bottari.domain.repository.TeamMemberRepository

class JoinTeamBottariUseCase(
    private val teamMemberRepository: TeamMemberRepository,
) {
    suspend operator fun invoke(inviteCode: String): Result<Unit> = teamMemberRepository.joinTeamBottari(inviteCode)
}
