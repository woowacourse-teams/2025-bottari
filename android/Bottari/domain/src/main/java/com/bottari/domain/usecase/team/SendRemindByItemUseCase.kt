package com.bottari.domain.usecase.team

import com.bottari.domain.repository.TeamBottariItemsRepository
import javax.inject.Inject

class SendRemindByItemUseCase @Inject constructor(
    private val teamBottariItemsRepository: TeamBottariItemsRepository,
) {
    suspend operator fun invoke(
        id: Long,
        type: String,
    ): Result<Unit> = teamBottariItemsRepository.sendRemindByItem(id, type)
}
