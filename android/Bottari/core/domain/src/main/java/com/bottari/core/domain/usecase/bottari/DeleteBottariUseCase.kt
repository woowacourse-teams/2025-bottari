package com.bottari.core.domain.usecase.bottari

import com.bottari.core.domain.repository.BottariRepository
import javax.inject.Inject

class DeleteBottariUseCase @Inject constructor(
    private val bottariRepository: BottariRepository,
) {
    suspend operator fun invoke(bottariId: Long): Result<Unit> = bottariRepository.deleteBottari(bottariId)
}
