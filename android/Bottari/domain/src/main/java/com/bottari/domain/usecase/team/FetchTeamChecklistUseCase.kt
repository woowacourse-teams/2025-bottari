package com.bottari.domain.usecase.team

import com.bottari.domain.model.team.TeamBottariCheckList
import com.bottari.domain.repository.TeamBottariRepository

class FetchTeamChecklistUseCase(
    private val teamBottariRepository: TeamBottariRepository,
) {
    suspend operator fun invoke(id: Long): Result<TeamBottariCheckList> = teamBottariRepository.fetchTeamBottari(id)
}
