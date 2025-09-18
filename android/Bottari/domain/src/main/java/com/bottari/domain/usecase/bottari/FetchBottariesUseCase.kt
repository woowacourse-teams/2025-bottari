package com.bottari.domain.usecase.bottari

import com.bottari.domain.model.bottari.BottariState
import com.bottari.domain.model.exception.BottariResult
import com.bottari.domain.repository.BottariRepository

class FetchBottariesUseCase(
    private val bottariRepository: BottariRepository,
) {
    suspend operator fun invoke(): BottariResult<List<BottariState>> = bottariRepository.fetchBottaries()
}
