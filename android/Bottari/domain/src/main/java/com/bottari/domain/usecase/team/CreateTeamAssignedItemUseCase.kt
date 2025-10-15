package com.bottari.domain.usecase.team

import com.bottari.domain.repository.TeamBottariItemsRepository
import javax.inject.Inject

class CreateTeamAssignedItemUseCase @Inject constructor(
    private val teamBottariItemsRepository: TeamBottariItemsRepository,
) {
    suspend operator fun invoke(
        bottariId: Long,
        name: String,
        teamMemberIds: List<Long>,
    ): Result<Unit> = teamBottariItemsRepository.createTeamBottariAssignedItem(bottariId, name, teamMemberIds)
}
