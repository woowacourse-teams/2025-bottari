package com.bottari.domain.usecase.bottari

import com.bottari.domain.repository.BottariRepository

class CreateBottariUseCase(
    private val bottariRepository: BottariRepository,
) {
    suspend operator fun invoke(ssaid: String, title: String): Result<Long> =
        bottariRepository.createBottari(ssaid, title)
}
