package com.bottari.domain.usecase.team

import com.bottari.domain.model.team.member.TeamMember
import com.bottari.domain.repository.TeamMemberRepository
import javax.inject.Inject

class FetchTeamBottariMembersUseCase @Inject constructor(
    private val teamMemberRepository: TeamMemberRepository,
) {
    suspend operator fun invoke(teamId: Long): Result<List<TeamMember>> = teamMemberRepository.fetchTeamBottariMembers(teamId)
}
