package com.bottari.core.domain.usecase.team

import com.bottari.core.domain.model.team.bottari.TeamBottariDetail
import com.bottari.core.domain.repository.TeamBottariRepository
import javax.inject.Inject

class FetchTeamBottariDetailUseCase @Inject constructor(
    private val teamBottariRepository: TeamBottariRepository,
) {
    suspend operator fun invoke(teamBottariId: Long): Result<TeamBottariDetail> =
        teamBottariRepository.fetchTeamBottariDetail(teamBottariId)
}
