package com.bottari.domain.usecase.team

import com.bottari.domain.model.exception.BottariResult
import com.bottari.domain.repository.TeamBottariRepository

class JoinTeamBottariUseCase(
    private val teamBottariRepository: TeamBottariRepository,
) {
    suspend operator fun invoke(inviteCode: String): BottariResult<Unit> = teamBottariRepository.joinTeamBottari(inviteCode)
}
