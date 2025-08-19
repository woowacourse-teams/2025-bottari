package com.bottari.domain.usecase.team

import com.bottari.domain.model.team.TeamBottariDetail
import com.bottari.domain.repository.TeamBottariRepository

class FetchTeamBottariDetailUseCase(
    private val teamBottariRepository: TeamBottariRepository,
) {
    suspend operator fun invoke(teamBottariId: Long): Result<TeamBottariDetail> =
        teamBottariRepository.fetchTeamBottariDetail(teamBottariId)
}
