package com.bottari.domain.usecase.team

import com.bottari.domain.model.exception.BottariResult
import com.bottari.domain.model.team.bottari.TeamBottariCheckList
import com.bottari.domain.repository.TeamBottariRepository

class FetchTeamChecklistUseCase(
    private val teamBottariRepository: TeamBottariRepository,
) {
    suspend operator fun invoke(id: Long): BottariResult<TeamBottariCheckList> = teamBottariRepository.fetchTeamBottari(id)
}
