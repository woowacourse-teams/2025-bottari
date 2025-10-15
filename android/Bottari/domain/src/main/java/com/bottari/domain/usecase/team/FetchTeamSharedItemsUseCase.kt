package com.bottari.domain.usecase.team

import com.bottari.domain.model.bottari.item.BottariItem
import com.bottari.domain.repository.TeamBottariItemsRepository
import javax.inject.Inject

class FetchTeamSharedItemsUseCase @Inject constructor(
    private val teamBottariItemsRepository: TeamBottariItemsRepository,
) {
    suspend operator fun invoke(bottariId: Long): Result<List<BottariItem>> = teamBottariItemsRepository.fetchTeamSharedItems(bottariId)
}
