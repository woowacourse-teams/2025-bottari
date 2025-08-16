package com.bottari.domain.usecase.team

import com.bottari.domain.repository.TeamBottariRepository

class CreateTeamAssignedItemUseCase(
    private val teamBottariRepository: TeamBottariRepository,
) {
    suspend operator fun invoke(
        bottariId: Long,
        name: String,
        teamMemberNames: List<String>,
    ): Result<Unit> = teamBottariRepository.createTeamBottariAssignedItem(bottariId, name, teamMemberNames)
}
