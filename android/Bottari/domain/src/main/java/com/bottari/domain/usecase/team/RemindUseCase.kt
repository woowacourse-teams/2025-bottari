package com.bottari.domain.usecase.team

import com.bottari.domain.repository.TeamBottariRepository

class RemindUseCase(
    private val teamBottariRepository: TeamBottariRepository,
) {
    suspend operator fun invoke(
        id: Long,
        type: String,
    ): Result<Unit> = teamBottariRepository.remind(id, type)
}
