package com.bottari.core.domain.usecase.team

import com.bottari.core.domain.model.team.bottari.TeamBottariStatus
import com.bottari.core.domain.repository.TeamBottariItemsRepository
import javax.inject.Inject

class FetchTeamStatusUseCase @Inject constructor(
    private val teamBottariItemsRepository: TeamBottariItemsRepository,
) {
    suspend operator fun invoke(id: Long): Result<TeamBottariStatus> = teamBottariItemsRepository.fetchTeamBottariStatus(id)
}
