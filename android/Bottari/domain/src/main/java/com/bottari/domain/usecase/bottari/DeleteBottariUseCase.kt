package com.bottari.domain.usecase.bottari

import com.bottari.domain.repository.BottariRepository

class DeleteBottariUseCase(
    private val bottariRepository: BottariRepository,
) {
    suspend operator fun invoke(
        ssaid: String,
        bottariId: Long,
    ): Result<Unit> = bottariRepository.deleteBottari(bottariId, ssaid)
}
