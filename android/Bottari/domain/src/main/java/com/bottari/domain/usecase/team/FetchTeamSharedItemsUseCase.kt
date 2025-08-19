package com.bottari.domain.usecase.team

import com.bottari.domain.model.bottari.BottariItem
import com.bottari.domain.repository.TeamBottariRepository

class FetchTeamSharedItemsUseCase(
    private val teamBottariRepository: TeamBottariRepository,
) {
    suspend operator fun invoke(bottariId: Long): Result<List<BottariItem>> = teamBottariRepository.fetchTeamSharedItems(bottariId)
}
