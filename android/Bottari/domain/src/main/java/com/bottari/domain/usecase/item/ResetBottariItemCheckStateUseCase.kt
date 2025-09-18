package com.bottari.domain.usecase.item

import com.bottari.domain.model.exception.BottariResult
import com.bottari.domain.repository.BottariItemRepository

class ResetBottariItemCheckStateUseCase(
    private val bottariItemRepository: BottariItemRepository,
) {
    suspend operator fun invoke(bottariId: Long): BottariResult<Unit> = bottariItemRepository.resetBottariItemCheckState(bottariId)
}
