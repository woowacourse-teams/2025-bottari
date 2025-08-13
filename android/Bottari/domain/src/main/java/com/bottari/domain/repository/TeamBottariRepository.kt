package com.bottari.domain.repository

import com.bottari.domain.model.bottari.TeamBottari
import com.bottari.domain.model.team.TeamBottariCheckList
import com.bottari.domain.model.team.TeamMembers

interface TeamBottariRepository {
    suspend fun createTeamBottari(title: String): Result<Long?>

    suspend fun fetchTeamBottari(teamBottariId: Long): Result<TeamBottariCheckList>

    suspend fun uncheckBottariItem(
        bottariItemId: Long,
        category: String,
    ): Result<Unit>

    suspend fun checkBottariItem(
        bottariItemId: Long,
        category: String,
    ): Result<Unit>

    suspend fun fetchTeamBottaries(): Result<List<TeamBottari>>

    suspend fun fetchTeamMembers(id: Long): Result<TeamMembers>
}
