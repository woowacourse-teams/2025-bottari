package com.bottari.core.domain.usecase.team

import com.bottari.core.domain.model.team.member.TeamMemberStatus
import com.bottari.core.domain.repository.TeamMemberRepository
import javax.inject.Inject

class FetchTeamMembersStatusUseCase @Inject constructor(
    private val teamMemberRepository: TeamMemberRepository,
) {
    suspend operator fun invoke(id: Long): Result<List<TeamMemberStatus>> = teamMemberRepository.fetchTeamMembersStatus(id)
}
