package com.bottari.domain.usecase.bottariDetail

import com.bottari.domain.model.bottari.BottariDetail
import com.bottari.domain.repository.BottariDetailRepository

class FindBottariDetailUseCase(
    val bottariDetailRepository: BottariDetailRepository,
) {
    suspend operator fun invoke(
        id: Long,
        ssaid: String,
    ): Result<BottariDetail> = bottariDetailRepository.findBottariDetail(id, ssaid)
}
