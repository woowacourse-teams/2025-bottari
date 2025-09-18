package com.bottari.domain.usecase.team

import com.bottari.domain.model.exception.BottariResult
import com.bottari.domain.model.team.bottari.TeamBottariDetail
import com.bottari.domain.repository.TeamBottariRepository

class FetchTeamBottariDetailUseCase(
    private val teamBottariRepository: TeamBottariRepository,
) {
    suspend operator fun invoke(teamBottariId: Long): BottariResult<TeamBottariDetail> =
        teamBottariRepository.fetchTeamBottariDetail(teamBottariId)
}
