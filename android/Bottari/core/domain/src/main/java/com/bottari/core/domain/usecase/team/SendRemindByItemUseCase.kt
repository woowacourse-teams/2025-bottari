package com.bottari.core.domain.usecase.team

import com.bottari.core.domain.repository.TeamBottariItemsRepository
import javax.inject.Inject

class SendRemindByItemUseCase @Inject constructor(
    private val teamBottariItemsRepository: TeamBottariItemsRepository,
) {
    suspend operator fun invoke(
        id: Long,
        type: String,
    ): Result<Unit> = teamBottariItemsRepository.sendRemindByItem(id, type)
}
