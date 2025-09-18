package com.bottari.domain.usecase.team

import com.bottari.domain.model.exception.BottariResult
import com.bottari.domain.model.team.member.TeamStatus
import com.bottari.domain.repository.TeamBottariRepository

class FetchTeamMembersUseCase(
    private val repository: TeamBottariRepository,
) {
    suspend operator fun invoke(id: Long): BottariResult<TeamStatus> = repository.fetchTeamMembers(id)
}
