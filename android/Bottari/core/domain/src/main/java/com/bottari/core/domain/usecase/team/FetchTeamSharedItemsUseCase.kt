package com.bottari.core.domain.usecase.team

import com.bottari.core.domain.model.bottari.item.BottariItem
import com.bottari.core.domain.repository.TeamBottariItemsRepository
import javax.inject.Inject

class FetchTeamSharedItemsUseCase @Inject constructor(
    private val teamBottariItemsRepository: TeamBottariItemsRepository,
) {
    suspend operator fun invoke(bottariId: Long): Result<List<BottariItem>> = teamBottariItemsRepository.fetchTeamSharedItems(bottariId)
}
