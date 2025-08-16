package com.bottari.domain.usecase.team

import com.bottari.domain.model.bottari.BottariItemType
import com.bottari.domain.repository.TeamBottariRepository

class DeleteTeamBottariItemUseCase(
    private val teamBottariRepository: TeamBottariRepository,
) {
    suspend operator fun invoke(
        itemId: Long,
        type: BottariItemType,
    ) = teamBottariRepository.deleteTeamBottariItem(itemId, type)
}
