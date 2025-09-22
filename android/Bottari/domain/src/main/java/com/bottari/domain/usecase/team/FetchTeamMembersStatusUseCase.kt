package com.bottari.domain.usecase.team

import com.bottari.domain.model.team.member.TeamMemberStatus
import com.bottari.domain.repository.TeamMemberRepository

class FetchTeamMembersStatusUseCase(
    private val teamMemberRepository: TeamMemberRepository,
) {
    suspend operator fun invoke(id: Long): Result<List<TeamMemberStatus>> = teamMemberRepository.fetchTeamMembersStatus(id)
}
