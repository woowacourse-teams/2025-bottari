package com.bottari.domain.usecase.team

import com.bottari.domain.repository.TeamBottariItemsRepository

class CreateTeamAssignedItemUseCase(
    private val teamBottariItemsRepository: TeamBottariItemsRepository,
) {
    suspend operator fun invoke(
        bottariId: Long,
        name: String,
        teamMemberIds: List<Long>,
    ): Result<Unit> = teamBottariItemsRepository.createTeamBottariAssignedItem(bottariId, name, teamMemberIds)
}
