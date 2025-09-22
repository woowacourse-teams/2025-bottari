package com.bottari.domain.usecase.team

import com.bottari.domain.model.team.bottari.TeamBottariCheckList
import com.bottari.domain.repository.TeamBottariItemsRepository

class FetchTeamChecklistUseCase(
    private val teamBottariItemsRepository: TeamBottariItemsRepository,
) {
    suspend operator fun invoke(id: Long): Result<TeamBottariCheckList> = teamBottariItemsRepository.fetchTeamBottari(id)
}
