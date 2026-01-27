package com.bottari.domain.usecase.team

import com.bottari.domain.model.team.member.TeamMemberStatus
import com.bottari.domain.repository.TeamMemberRepository
import javax.inject.Inject

class FetchTeamMembersStatusUseCase @Inject constructor(
    private val teamMemberRepository: TeamMemberRepository,
) {
    suspend operator fun invoke(id: Long): Result<List<TeamMemberStatus>> = teamMemberRepository.fetchTeamMembersStatus(id)
}
