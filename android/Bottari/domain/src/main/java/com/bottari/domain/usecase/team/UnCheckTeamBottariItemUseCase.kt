package com.bottari.domain.usecase.team

import com.bottari.domain.repository.TeamBottariRepository

class UnCheckTeamBottariItemUseCase(
    private val teamBottariRepository: TeamBottariRepository,
) {
    suspend operator fun invoke(
        id: Long,
        category: String,
    ): Result<Unit> = teamBottariRepository.uncheckBottariItem(id, category)
}
