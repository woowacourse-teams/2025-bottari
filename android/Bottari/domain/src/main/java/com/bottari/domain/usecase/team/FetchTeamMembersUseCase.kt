package com.bottari.domain.usecase.team

import com.bottari.domain.model.team.member.TeamStatus
import com.bottari.domain.repository.TeamMemberRepository

class FetchTeamMembersUseCase(
    private val teamMemberRepository: TeamMemberRepository,
) {
    suspend operator fun invoke(id: Long): Result<TeamStatus> = teamMemberRepository.fetchTeamMembers(id)
}
