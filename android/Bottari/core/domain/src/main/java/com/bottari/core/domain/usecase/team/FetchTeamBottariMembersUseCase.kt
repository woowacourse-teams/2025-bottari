package com.bottari.core.domain.usecase.team

import com.bottari.core.domain.model.team.member.TeamMember
import com.bottari.core.domain.repository.TeamMemberRepository
import javax.inject.Inject

class FetchTeamBottariMembersUseCase @Inject constructor(
    private val teamMemberRepository: TeamMemberRepository,
) {
    suspend operator fun invoke(teamId: Long): Result<List<TeamMember>> = teamMemberRepository.fetchTeamBottariMembers(teamId)
}
