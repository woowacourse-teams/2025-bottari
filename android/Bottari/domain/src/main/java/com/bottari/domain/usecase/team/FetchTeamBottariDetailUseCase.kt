package com.bottari.domain.usecase.team

import com.bottari.domain.model.team.bottari.TeamBottariDetail
import com.bottari.domain.repository.TeamBottariRepository
import javax.inject.Inject

class FetchTeamBottariDetailUseCase @Inject constructor(
    private val teamBottariRepository: TeamBottariRepository,
) {
    suspend operator fun invoke(teamBottariId: Long): Result<TeamBottariDetail> =
        teamBottariRepository.fetchTeamBottariDetail(teamBottariId)
}
