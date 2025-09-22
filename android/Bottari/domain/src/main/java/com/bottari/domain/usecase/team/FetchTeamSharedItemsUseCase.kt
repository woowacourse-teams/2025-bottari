package com.bottari.domain.usecase.team

import com.bottari.domain.model.bottari.item.BottariItem
import com.bottari.domain.repository.TeamBottariItemsRepository

class FetchTeamSharedItemsUseCase(
    private val teamBottariItemsRepository: TeamBottariItemsRepository,
) {
    suspend operator fun invoke(bottariId: Long): Result<List<BottariItem>> = teamBottariItemsRepository.fetchTeamSharedItems(bottariId)
}
