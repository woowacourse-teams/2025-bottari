package com.bottari.domain.repository

import com.bottari.domain.model.bottari.TeamBottari

interface TeamBottariRepository {
    suspend fun createTeamBottari(title: String): Result<Long?>

    suspend fun fetchTeamBottaries(): Result<List<TeamBottari>>
}
