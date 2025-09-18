package com.bottari.domain.repository

import com.bottari.domain.model.bottari.item.BottariItem
import com.bottari.domain.model.exception.BottariResult
import com.bottari.domain.model.team.bottari.TeamBottari
import com.bottari.domain.model.team.bottari.TeamBottariCheckList
import com.bottari.domain.model.team.bottari.TeamBottariDetail
import com.bottari.domain.model.team.bottari.TeamBottariStatus
import com.bottari.domain.model.team.bottari.item.TeamBottariItemType
import com.bottari.domain.model.team.member.TeamMember
import com.bottari.domain.model.team.member.TeamMemberStatus
import com.bottari.domain.model.team.member.TeamStatus

interface TeamBottariRepository {
    suspend fun createTeamBottari(title: String): BottariResult<Long>

    suspend fun fetchTeamBottari(teamBottariId: Long): BottariResult<TeamBottariCheckList>

    suspend fun uncheckBottariItem(
        bottariItemId: Long,
        type: String,
    ): BottariResult<Unit>

    suspend fun checkBottariItem(
        bottariItemId: Long,
        type: String,
    ): BottariResult<Unit>

    suspend fun fetchTeamBottaries(): BottariResult<List<TeamBottari>>

    suspend fun fetchTeamMembers(id: Long): BottariResult<TeamStatus>

    suspend fun fetchTeamBottariDetail(teamBottariId: Long): BottariResult<TeamBottariDetail>

    suspend fun fetchTeamBottariStatus(id: Long): BottariResult<TeamBottariStatus>

    suspend fun sendRemindByItem(
        id: Long,
        type: String,
    ): BottariResult<Unit>

    suspend fun fetchTeamMembersStatus(id: Long): BottariResult<List<TeamMemberStatus>>

    suspend fun createTeamBottariSharedItem(
        id: Long,
        name: String,
    ): BottariResult<Long>

    suspend fun createTeamBottariPersonalItem(
        id: Long,
        name: String,
    ): BottariResult<Long>

    suspend fun createTeamBottariAssignedItem(
        id: Long,
        name: String,
        teamMemberIds: List<Long>,
    ): BottariResult<Long>

    suspend fun deleteTeamBottariItem(
        id: Long,
        type: TeamBottariItemType,
    ): BottariResult<Unit>

    suspend fun sendRemindByMemberMessage(
        teamBottariId: Long,
        memberId: Long,
    ): BottariResult<Unit>

    suspend fun joinTeamBottari(inviteCode: String): BottariResult<Unit>

    suspend fun fetchTeamBottariMembers(teamBottariId: Long): BottariResult<List<TeamMember>>

    suspend fun fetchTeamAssignedItems(teamBottariId: Long): BottariResult<List<BottariItem>>

    suspend fun fetchTeamSharedItems(teamBottariId: Long): BottariResult<List<BottariItem>>

    suspend fun fetchTeamPersonalItems(teamBottariId: Long): BottariResult<List<BottariItem>>

    suspend fun saveTeamBottariAssignedItem(
        teamBottariId: Long,
        assignedItemId: Long,
        name: String,
        assigneeIds: List<Long>,
    ): BottariResult<Unit>

    suspend fun exitTeamBottari(teamBottariId: Long): BottariResult<Unit>
}
