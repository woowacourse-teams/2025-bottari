package com.bottari.domain.usecase.team

import com.bottari.domain.repository.TeamBottariItemsRepository
import javax.inject.Inject

class CreateTeamSharedItemUseCase @Inject constructor(
    private val teamBottariItemsRepository: TeamBottariItemsRepository,
) {
    suspend operator fun invoke(
        bottariId: Long,
        name: String,
    ): Result<Unit> = teamBottariItemsRepository.createTeamBottariSharedItem(bottariId, name)
}
