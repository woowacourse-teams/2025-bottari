package com.bottari.domain.usecase.team

import com.bottari.domain.model.team.TeamBottariStatus
import com.bottari.domain.repository.TeamBottariRepository

class FetchTeamStatusUseCase(
    private val teamBottariRepository: TeamBottariRepository,
) {
    suspend operator fun invoke(id: Long): Result<TeamBottariStatus> = teamBottariRepository.fetchTeamBottariStatus(id)
}
