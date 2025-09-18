package com.bottari.domain.usecase.team

import com.bottari.domain.model.exception.BottariResult
import com.bottari.domain.model.team.bottari.TeamBottari
import com.bottari.domain.repository.TeamBottariRepository

class FetchTeamBottariesUseCase(
    private val teamBottariRepository: TeamBottariRepository,
) {
    suspend operator fun invoke(): BottariResult<List<TeamBottari>> = teamBottariRepository.fetchTeamBottaries()
}
