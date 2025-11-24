package com.bottari.core.domain.usecase.item

import com.bottari.core.domain.repository.BottariItemRepository
import javax.inject.Inject

class UpdateItemCheckStateUseCase @Inject constructor(
    private val bottariItemRepository: BottariItemRepository,
) {
    suspend operator fun invoke(
        id: Long,
        isChecked: Boolean,
    ): Result<Unit> = bottariItemRepository.updateCheckState(id, isChecked)
}
