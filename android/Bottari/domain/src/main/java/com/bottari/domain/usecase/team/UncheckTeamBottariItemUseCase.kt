package com.bottari.domain.usecase.team

import com.bottari.domain.repository.TeamBottariItemsRepository

class UncheckTeamBottariItemUseCase(
    private val teamBottariItemsRepository: TeamBottariItemsRepository,
) {
    suspend operator fun invoke(
        id: Long,
        type: String,
    ): Result<Unit> = teamBottariItemsRepository.uncheckBottariItem(id, type)
}
