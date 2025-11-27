package com.bottari.core.domain.usecase.team

import com.bottari.core.domain.model.team.member.TeamStatus
import com.bottari.core.domain.repository.TeamMemberRepository
import javax.inject.Inject

class FetchTeamMembersUseCase @Inject constructor(
    private val teamMemberRepository: TeamMemberRepository,
) {
    suspend operator fun invoke(id: Long): Result<TeamStatus> = teamMemberRepository.fetchTeamMembers(id)
}
