package com.bottari.domain.usecase.team

import com.bottari.domain.model.exception.BottariResult
import com.bottari.domain.repository.TeamBottariRepository

class UncheckTeamBottariItemUseCase(
    private val teamBottariRepository: TeamBottariRepository,
) {
    suspend operator fun invoke(
        id: Long,
        type: String,
    ): BottariResult<Unit> = teamBottariRepository.uncheckBottariItem(id, type)
}
