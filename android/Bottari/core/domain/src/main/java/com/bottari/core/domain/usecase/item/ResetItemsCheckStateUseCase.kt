package com.bottari.core.domain.usecase.item

import com.bottari.core.domain.repository.BottariItemRepository
import javax.inject.Inject

class ResetItemsCheckStateUseCase @Inject constructor(
    private val bottariItemRepository: BottariItemRepository,
) {
    suspend operator fun invoke(bottariId: Long): Result<Unit> = bottariItemRepository.resetCheckState(bottariId)
}
