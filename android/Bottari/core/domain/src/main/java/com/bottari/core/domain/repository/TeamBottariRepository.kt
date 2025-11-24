package com.bottari.core.domain.repository

import com.bottari.core.domain.model.team.bottari.TeamBottari
import com.bottari.core.domain.model.team.bottari.TeamBottariDetail

interface TeamBottariRepository {
    suspend fun createTeamBottari(title: String): Result<Long?>

    suspend fun fetchTeamBottaries(): Result<List<TeamBottari>>

    suspend fun fetchTeamBottariDetail(teamBottariId: Long): Result<TeamBottariDetail>

    suspend fun exitTeamBottari(teamBottariId: Long): Result<Unit>
}
