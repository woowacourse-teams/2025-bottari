package com.bottari.domain.usecase.team

import com.bottari.domain.repository.TeamBottariItemsRepository
import javax.inject.Inject

class SaveTeamBottariAssignedItemUseCase @Inject constructor(
    private val teamBottariItemsRepository: TeamBottariItemsRepository,
) {
    suspend operator fun invoke(
        teamBottariId: Long,
        assignedItemId: Long,
        name: String,
        assigneeIds: List<Long>,
    ): Result<Unit> =
        teamBottariItemsRepository.saveTeamBottariAssignedItem(
            teamBottariId,
            assignedItemId,
            name,
            assigneeIds,
        )
}
