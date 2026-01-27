package com.bottari.domain.usecase.team

import com.bottari.domain.model.team.bottari.item.TeamBottariItemType
import com.bottari.domain.repository.TeamBottariItemsRepository
import javax.inject.Inject

class DeleteTeamBottariItemUseCase @Inject constructor(
    private val teamBottariItemsRepository: TeamBottariItemsRepository,
) {
    suspend operator fun invoke(
        itemId: Long,
        type: TeamBottariItemType,
    ) = teamBottariItemsRepository.deleteTeamBottariItem(itemId, type)
}
