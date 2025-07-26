package com.bottari.domain.usecase.bottari

import com.bottari.domain.repository.BottariRepository

class RenameBottariUseCase(
    private val bottariRepository: BottariRepository,
) {
    suspend operator fun invoke(
        id: Long,
        ssaid: String,
        title: String,
    ): Result<Unit> = bottariRepository.renameBottari(id, ssaid, title)
}
