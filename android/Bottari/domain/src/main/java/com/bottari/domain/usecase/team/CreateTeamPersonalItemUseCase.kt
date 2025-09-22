package com.bottari.domain.usecase.team

import com.bottari.domain.repository.TeamBottariItemsRepository

class CreateTeamPersonalItemUseCase(
    private val teamBottariItemsRepository: TeamBottariItemsRepository,
) {
    suspend operator fun invoke(
        bottariId: Long,
        name: String,
    ): Result<Unit> = teamBottariItemsRepository.createTeamBottariPersonalItem(bottariId, name)
}
