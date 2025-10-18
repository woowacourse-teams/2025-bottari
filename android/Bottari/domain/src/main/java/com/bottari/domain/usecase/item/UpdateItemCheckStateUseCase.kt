package com.bottari.domain.usecase.item

import com.bottari.domain.repository.BottariItemRepository
import javax.inject.Inject

class UpdateItemCheckStateUseCase @Inject constructor(
    private val bottariItemRepository: BottariItemRepository,
) {
    suspend operator fun invoke(
        id: Long,
        isChecked: Boolean,
    ): Result<Unit> = bottariItemRepository.updateCheckState(id, isChecked)
}
