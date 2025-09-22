package com.bottari.domain.repository

import com.bottari.domain.model.bottari.item.BottariItem
import com.bottari.domain.model.team.bottari.TeamBottariCheckList
import com.bottari.domain.model.team.bottari.TeamBottariStatus
import com.bottari.domain.model.team.bottari.item.TeamBottariItemType

interface TeamBottariItemsRepository {
    suspend fun fetchTeamBottari(teamBottariId: Long): Result<TeamBottariCheckList>

    suspend fun uncheckBottariItem(
        bottariItemId: Long,
        type: String,
    ): Result<Unit>

    suspend fun checkBottariItem(
        bottariItemId: Long,
        type: String,
    ): Result<Unit>

    suspend fun fetchTeamBottariStatus(id: Long): Result<TeamBottariStatus>

    suspend fun createTeamBottariSharedItem(
        id: Long,
        name: String,
    ): Result<Unit>

    suspend fun createTeamBottariPersonalItem(
        id: Long,
        name: String,
    ): Result<Unit>

    suspend fun createTeamBottariAssignedItem(
        id: Long,
        name: String,
        teamMemberIds: List<Long>,
    ): Result<Unit>

    suspend fun deleteTeamBottariItem(
        id: Long,
        type: TeamBottariItemType,
    ): Result<Unit>

    suspend fun fetchTeamAssignedItems(teamBottariId: Long): Result<List<BottariItem>>

    suspend fun fetchTeamSharedItems(teamBottariId: Long): Result<List<BottariItem>>

    suspend fun fetchTeamPersonalItems(teamBottariId: Long): Result<List<BottariItem>>

    suspend fun saveTeamBottariAssignedItem(
        teamBottariId: Long,
        assignedItemId: Long,
        name: String,
        assigneeIds: List<Long>,
    ): Result<Unit>

    suspend fun sendRemindByItem(
        id: Long,
        type: String,
    ): Result<Unit>
}
