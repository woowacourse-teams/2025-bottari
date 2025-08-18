package com.bottari.domain.usecase.team

import com.bottari.domain.model.team.TeamMember
import com.bottari.domain.repository.TeamBottariRepository

class FetchTeamBottariMembersUseCase(
    private val teamBottariRepository: TeamBottariRepository,
) {
    suspend operator fun invoke(teamId: Long): Result<List<TeamMember>> = teamBottariRepository.fetchTeamBottariMembers(teamId)
}
