package com.bottari.domain.usecase.bottari

import com.bottari.domain.model.exception.BottariResult
import com.bottari.domain.repository.BottariRepository

class CreateBottariUseCase(
    private val bottariRepository: BottariRepository,
) {
    suspend operator fun invoke(title: String): BottariResult<Long> = bottariRepository.createBottari(title)
}
