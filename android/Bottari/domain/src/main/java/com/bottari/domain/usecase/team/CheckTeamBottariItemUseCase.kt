package com.bottari.domain.usecase.team

import com.bottari.domain.repository.TeamBottariItemsRepository

class CheckTeamBottariItemUseCase(
    private val teamBottariItemsRepository: TeamBottariItemsRepository,
) {
    suspend operator fun invoke(
        id: Long,
        type: String,
    ): Result<Unit> = teamBottariItemsRepository.checkBottariItem(id, type)
}
