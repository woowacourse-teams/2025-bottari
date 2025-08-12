package com.bottari.domain.usecase.team

import com.bottari.domain.model.team.TeamMembers
import com.bottari.domain.repository.TeamBottariRepository

class FetchTeamMembersUseCase(
    private val repository: TeamBottariRepository,
) {
    suspend operator fun invoke(id: Long): Result<TeamMembers> = repository.fetchTeamMembers(id)
}
