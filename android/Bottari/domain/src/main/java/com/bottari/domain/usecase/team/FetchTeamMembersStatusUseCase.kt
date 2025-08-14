package com.bottari.domain.usecase.team

import com.bottari.domain.model.team.TeamMemberStatus
import com.bottari.domain.repository.TeamBottariRepository

class FetchTeamMembersStatusUseCase(
    private val repository: TeamBottariRepository,
) {
    suspend operator fun invoke(id: Long): Result<List<TeamMemberStatus>> = repository.fetchTeamMembersStatus(id)
}
