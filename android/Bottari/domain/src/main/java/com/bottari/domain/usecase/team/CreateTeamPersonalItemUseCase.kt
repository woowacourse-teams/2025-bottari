package com.bottari.domain.usecase.team

import com.bottari.domain.repository.TeamBottariRepository

class CreateTeamPersonalItemUseCase(
    private val teamBottariRepository: TeamBottariRepository,
) {
    suspend operator fun invoke(
        bottariId: Long,
        name: String,
    ) = teamBottariRepository.createTeamBottariPersonalItem(bottariId, name)
}
