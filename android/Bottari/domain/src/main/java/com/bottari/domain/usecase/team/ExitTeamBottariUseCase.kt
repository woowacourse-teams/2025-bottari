package com.bottari.domain.usecase.team

import com.bottari.domain.repository.TeamBottariRepository

class ExitTeamBottariUseCase(
    private val teamBottariRepository: TeamBottariRepository,
) {
    suspend operator fun invoke(teamBottariId: Long): Result<Unit> = teamBottariRepository.exitTeamBottari(teamBottariId)
}
