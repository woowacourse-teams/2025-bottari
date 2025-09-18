package com.bottari.domain.usecase.bottariDetail

import com.bottari.domain.model.bottari.Bottari
import com.bottari.domain.model.exception.BottariResult
import com.bottari.domain.repository.BottariRepository

class FetchBottariDetailUseCase(
    private val bottariRepository: BottariRepository,
) {
    suspend operator fun invoke(id: Long): BottariResult<Bottari> = bottariRepository.fetchBottariDetail(id)
}
