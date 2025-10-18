package com.bottari.domain.usecase.team

import com.bottari.domain.repository.TeamMemberRepository
import javax.inject.Inject

class JoinTeamBottariUseCase @Inject constructor(
    private val teamMemberRepository: TeamMemberRepository,
) {
    suspend operator fun invoke(inviteCode: String): Result<Unit> = teamMemberRepository.joinTeamBottari(inviteCode)
}
