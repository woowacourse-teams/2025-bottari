package com.bottari.domain.usecase.team

import com.bottari.domain.model.team.bottari.TeamBottari
import com.bottari.domain.repository.TeamBottariRepository
import javax.inject.Inject

class FetchTeamBottariesUseCase @Inject constructor(
    private val teamBottariRepository: TeamBottariRepository,
) {
    suspend operator fun invoke(): Result<List<TeamBottari>> = teamBottariRepository.fetchTeamBottaries()
}
