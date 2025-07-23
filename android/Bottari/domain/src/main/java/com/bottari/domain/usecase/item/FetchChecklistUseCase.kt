package com.bottari.domain.usecase.item

import com.bottari.domain.model.bottari.BottariItem
import com.bottari.domain.repository.BottariItemRepository

class FetchChecklistUseCase(
    private val bottariItemRepository: BottariItemRepository,
) {
    suspend operator fun invoke(
        ssaid: String,
        bottariId: Long,
    ): Result<List<BottariItem>> = bottariItemRepository.fetchChecklist(ssaid, bottariId)
}
