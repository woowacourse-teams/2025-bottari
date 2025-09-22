package com.bottari.domain.usecase.team

import com.bottari.domain.model.team.bottari.TeamBottariStatus
import com.bottari.domain.repository.TeamBottariItemsRepository

class FetchTeamStatusUseCase(
    private val teamBottariItemsRepository: TeamBottariItemsRepository,
) {
    suspend operator fun invoke(id: Long): Result<TeamBottariStatus> = teamBottariItemsRepository.fetchTeamBottariStatus(id)
}
