package com.bottari.domain.usecase.team

import com.bottari.domain.repository.TeamBottariRepository

class SaveTeamBottariAssignedItemUseCase(
    private val teamBottariRepository: TeamBottariRepository,
) {
    suspend operator fun invoke(
        teamBottariId: Long,
        assignedItemId: Long,
        name: String,
        assigneeIds: List<Long>,
    ): Result<Unit> =
        teamBottariRepository.saveTeamBottariAssignedItem(
            teamBottariId,
            assignedItemId,
            name,
            assigneeIds,
        )
}
