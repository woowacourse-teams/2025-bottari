package com.bottari.domain.usecase.team

import com.bottari.domain.model.bottari.TeamBottari
import com.bottari.domain.repository.TeamBottariRepository

class FetchTeamBottariesUseCase(
    private val teamBottariRepository: TeamBottariRepository,
) {
    suspend operator fun invoke(): Result<List<TeamBottari>> = teamBottariRepository.fetchTeamBottaries()
}
