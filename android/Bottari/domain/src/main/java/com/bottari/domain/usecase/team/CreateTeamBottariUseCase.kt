package com.bottari.domain.usecase.team

import com.bottari.domain.model.exception.BottariResult
import com.bottari.domain.repository.TeamBottariRepository

class CreateTeamBottariUseCase(
    private val teamBottariRepository: TeamBottariRepository,
) {
    suspend operator fun invoke(title: String): BottariResult<Long> = teamBottariRepository.createTeamBottari(title)
}
