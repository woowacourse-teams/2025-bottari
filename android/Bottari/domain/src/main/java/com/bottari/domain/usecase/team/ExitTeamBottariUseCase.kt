package com.bottari.domain.usecase.team

import com.bottari.domain.model.exception.BottariResult
import com.bottari.domain.repository.TeamBottariRepository

class ExitTeamBottariUseCase(
    private val teamBottariRepository: TeamBottariRepository,
) {
    suspend operator fun invoke(teamBottariId: Long): BottariResult<Unit> = teamBottariRepository.exitTeamBottari(teamBottariId)
}
