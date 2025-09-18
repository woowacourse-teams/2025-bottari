package com.bottari.domain.usecase.team

import com.bottari.domain.repository.TeamBottariRepository

class CreateTeamAssignedItemUseCase(
    private val teamBottariRepository: TeamBottariRepository,
) {
    suspend operator fun invoke(
        bottariId: Long,
        name: String,
        teamMemberIds: List<Long>,
    ) = teamBottariRepository.createTeamBottariAssignedItem(bottariId, name, teamMemberIds)
}
