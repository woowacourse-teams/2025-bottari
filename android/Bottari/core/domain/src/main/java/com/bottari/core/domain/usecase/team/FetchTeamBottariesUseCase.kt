package com.bottari.core.domain.usecase.team

import com.bottari.core.domain.model.team.bottari.TeamBottari
import com.bottari.core.domain.repository.TeamBottariRepository
import javax.inject.Inject

class FetchTeamBottariesUseCase @Inject constructor(
    private val teamBottariRepository: TeamBottariRepository,
) {
    suspend operator fun invoke(): Result<List<TeamBottari>> = teamBottariRepository.fetchTeamBottaries()
}
