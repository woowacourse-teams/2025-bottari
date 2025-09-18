package com.bottari.domain.usecase.team

import com.bottari.domain.model.exception.BottariResult
import com.bottari.domain.model.team.bottari.TeamBottariStatus
import com.bottari.domain.repository.TeamBottariRepository

class FetchTeamStatusUseCase(
    private val teamBottariRepository: TeamBottariRepository,
) {
    suspend operator fun invoke(id: Long): BottariResult<TeamBottariStatus> = teamBottariRepository.fetchTeamBottariStatus(id)
}
