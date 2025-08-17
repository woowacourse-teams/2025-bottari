package com.bottari.domain.repository

import com.bottari.domain.model.bottari.BottariItemType
import com.bottari.domain.model.bottari.TeamBottari
import com.bottari.domain.model.team.TeamBottariCheckList
import com.bottari.domain.model.team.TeamBottariDetail
import com.bottari.domain.model.team.TeamBottariStatus
import com.bottari.domain.model.team.TeamMember
import com.bottari.domain.model.team.TeamMemberStatus
import com.bottari.domain.model.team.TeamMembers

interface TeamBottariRepository {
    suspend fun createTeamBottari(title: String): Result<Long?>

    suspend fun fetchTeamBottari(teamBottariId: Long): Result<TeamBottariCheckList>

    suspend fun uncheckBottariItem(
        bottariItemId: Long,
        type: String,
    ): Result<Unit>

    suspend fun checkBottariItem(
        bottariItemId: Long,
        type: String,
    ): Result<Unit>

    suspend fun fetchTeamBottaries(): Result<List<TeamBottari>>

    suspend fun fetchTeamMembers(id: Long): Result<TeamMembers>

    suspend fun fetchTeamBottariDetail(teamBottariId: Long): Result<TeamBottariDetail>

    suspend fun fetchTeamBottariStatus(id: Long): Result<TeamBottariStatus>

    suspend fun sendRemindByItem(
        id: Long,
        type: String,
    ): Result<Unit>

    suspend fun fetchTeamMembersStatus(id: Long): Result<List<TeamMemberStatus>>

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
        type: BottariItemType,
    ): Result<Unit>

    suspend fun sendRemindByMemberMessage(
        teamBottariId: Long,
        memberId: Long,
    ): Result<Unit>

    suspend fun joinTeamBottari(inviteCode: String): Result<Unit>

    suspend fun fetchTeamBottariMembers(teamBottariId: Long): Result<List<TeamMember>>
}
