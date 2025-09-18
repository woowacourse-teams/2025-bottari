package com.bottari.domain.usecase.team

import com.bottari.domain.model.bottari.item.BottariItem
import com.bottari.domain.model.exception.BottariResult
import com.bottari.domain.repository.TeamBottariRepository

class FetchTeamPersonalItemsUseCase(
    private val teamBottariRepository: TeamBottariRepository,
) {
    suspend operator fun invoke(bottariId: Long): BottariResult<List<BottariItem>> = teamBottariRepository.fetchTeamPersonalItems(bottariId)
}
