package com.bottari.domain.usecase.team

import com.bottari.domain.model.team.member.TeamStatus
import com.bottari.domain.repository.TeamMemberRepository
import javax.inject.Inject

class FetchTeamMembersUseCase @Inject constructor(
    private val teamMemberRepository: TeamMemberRepository,
) {
    suspend operator fun invoke(id: Long): Result<TeamStatus> = teamMemberRepository.fetchTeamMembers(id)
}
