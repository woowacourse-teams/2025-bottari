package com.bottari.domain.usecase.bottariDetail

import com.bottari.domain.model.bottari.BottariDetail
import com.bottari.domain.repository.BottariRepository

class FindBottariDetailUseCase(
    val bottariRepository: BottariRepository,
) {
    suspend operator fun invoke(
        id: Long,
        ssaid: String,
    ): Result<BottariDetail> = bottariRepository.findBottari(id, ssaid)
}
