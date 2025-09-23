package com.bottari.domain.usecase.item

import com.bottari.domain.repository.BottariItemRepository

class UpdateItemCheckStateUseCase(
    private val bottariItemRepository: BottariItemRepository,
) {
    suspend operator fun invoke(
        id: Long,
        isChecked: Boolean,
    ): Result<Unit> = bottariItemRepository.updateCheckState(id, isChecked)
}
