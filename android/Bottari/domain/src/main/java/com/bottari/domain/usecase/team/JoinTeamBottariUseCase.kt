package com.bottari.domain.usecase.team

import com.bottari.domain.repository.TeamBottariRepository

class JoinTeamBottariUseCase(
    private val teamBottariRepository: TeamBottariRepository,
) {
    suspend operator fun invoke(inviteCode: String): Result<Unit> = teamBottariRepository.joinTeamBottari(inviteCode)
}
