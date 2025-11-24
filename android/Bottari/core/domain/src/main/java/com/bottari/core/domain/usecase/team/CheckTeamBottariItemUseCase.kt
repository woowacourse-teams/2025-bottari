package com.bottari.core.domain.usecase.team

import com.bottari.core.domain.repository.TeamBottariItemsRepository
import javax.inject.Inject

class CheckTeamBottariItemUseCase @Inject constructor(
    private val teamBottariItemsRepository: TeamBottariItemsRepository,
) {
    suspend operator fun invoke(
        id: Long,
        type: String,
    ): Result<Unit> = teamBottariItemsRepository.checkBottariItem(id, type)
}
