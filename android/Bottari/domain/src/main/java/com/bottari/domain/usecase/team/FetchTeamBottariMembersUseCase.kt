package com.bottari.domain.usecase.team

import com.bottari.domain.model.exception.BottariResult
import com.bottari.domain.model.team.member.TeamMember
import com.bottari.domain.repository.TeamBottariRepository

class FetchTeamBottariMembersUseCase(
    private val teamBottariRepository: TeamBottariRepository,
) {
    suspend operator fun invoke(teamId: Long): BottariResult<List<TeamMember>> = teamBottariRepository.fetchTeamBottariMembers(teamId)
}
