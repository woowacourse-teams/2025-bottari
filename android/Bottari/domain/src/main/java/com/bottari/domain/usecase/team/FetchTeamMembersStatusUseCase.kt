package com.bottari.domain.usecase.team

import com.bottari.domain.model.exception.BottariResult
import com.bottari.domain.model.team.member.TeamMemberStatus
import com.bottari.domain.repository.TeamBottariRepository

class FetchTeamMembersStatusUseCase(
    private val repository: TeamBottariRepository,
) {
    suspend operator fun invoke(id: Long): BottariResult<List<TeamMemberStatus>> = repository.fetchTeamMembersStatus(id)
}
