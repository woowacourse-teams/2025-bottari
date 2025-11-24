package com.bottari.core.domain.usecase.team

import com.bottari.core.domain.repository.TeamBottariRepository
import javax.inject.Inject

class ExitTeamBottariUseCase @Inject constructor(
    private val teamBottariRepository: TeamBottariRepository,
) {
    suspend operator fun invoke(teamBottariId: Long): Result<Unit> = teamBottariRepository.exitTeamBottari(teamBottariId)
}
