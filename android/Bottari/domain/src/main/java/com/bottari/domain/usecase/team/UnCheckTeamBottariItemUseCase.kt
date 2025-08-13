package com.bottari.domain.usecase.team

import com.bottari.domain.repository.TeamBottariRepository

class UnCheckTeamBottariItemUseCase(
    private val teamBottariRepository: TeamBottariRepository,
) {
    suspend operator fun invoke(title: String): Result<Long?> = teamBottariRepository.createTeamBottari(title)
}
