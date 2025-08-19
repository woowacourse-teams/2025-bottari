package com.bottari.domain.usecase.item

import com.bottari.domain.repository.BottariItemRepository

class ResetBottariItemCheckStateUseCase(
    private val bottariItemRepository: BottariItemRepository,
) {
    suspend operator fun invoke(bottariId: Long): Result<Unit> = bottariItemRepository.resetBottariItemCheckState(bottariId)
}
