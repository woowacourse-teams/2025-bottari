package com.bottari.domain.usecase.team

import com.bottari.domain.model.team.bottari.item.TeamBottariItemType
import com.bottari.domain.repository.TeamBottariRepository

class DeleteTeamBottariItemUseCase(
    private val teamBottariRepository: TeamBottariRepository,
) {
    suspend operator fun invoke(
        itemId: Long,
        type: TeamBottariItemType,
    ) = teamBottariRepository.deleteTeamBottariItem(itemId, type)
}
