package com.bottari.domain.usecase.bottari

import com.bottari.domain.repository.BottariRepository
import javax.inject.Inject

class SaveBottariTitleUseCase @Inject constructor(
    private val bottariRepository: BottariRepository,
) {
    suspend operator fun invoke(
        id: Long,
        title: String,
    ): Result<Unit> = bottariRepository.saveBottariTitle(id, title)
}
