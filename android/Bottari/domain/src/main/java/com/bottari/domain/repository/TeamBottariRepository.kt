package com.bottari.domain.repository

import com.bottari.domain.model.bottari.TeamBottari
import com.bottari.domain.model.team.TeamMembers

interface TeamBottariRepository {
    suspend fun createTeamBottari(title: String): Result<Long?>

    suspend fun fetchTeamBottaries(): Result<List<TeamBottari>>

    suspend fun fetchTeamMembers(id: Long): Result<TeamMembers>
}
