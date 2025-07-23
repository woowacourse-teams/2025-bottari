package com.bottari.domain.usecase.bottari

import com.bottari.domain.model.bottari.Bottari
import com.bottari.domain.repository.BottariRepository

class FetchBottariesUseCase(
    private val bottariRepository: BottariRepository,
) {
    suspend operator fun invoke(ssaid: String): Result<List<Bottari>> =
        bottariRepository.fetchBottaries(ssaid)
}
