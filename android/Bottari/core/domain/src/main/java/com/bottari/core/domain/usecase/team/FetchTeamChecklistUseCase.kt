package com.bottari.core.domain.usecase.team

import com.bottari.core.domain.model.team.bottari.TeamBottariCheckList
import com.bottari.core.domain.repository.TeamBottariItemsRepository
import javax.inject.Inject

class FetchTeamChecklistUseCase @Inject constructor(
    private val teamBottariItemsRepository: TeamBottariItemsRepository,
) {
    suspend operator fun invoke(id: Long): Result<TeamBottariCheckList> = teamBottariItemsRepository.fetchTeamBottari(id)
}
