package com.bottari.domain.usecase.bottari

import com.bottari.domain.repository.BottariRepository
import javax.inject.Inject

class CreateBottariUseCase @Inject constructor(
    private val bottariRepository: BottariRepository,
) {
    suspend operator fun invoke(title: String): Result<Long> = bottariRepository.createBottari(title)
}
