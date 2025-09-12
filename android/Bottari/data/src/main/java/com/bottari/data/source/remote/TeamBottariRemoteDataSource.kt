package com.bottari.data.source.remote

import com.bottari.data.model.team.bottari.FetchTeamBottariStatusResponse
import com.bottari.data.model.team.bottari.TeamBottariCreateRequest
import com.bottari.data.model.team.bottari.TeamBottariFetchDetailResponse
import com.bottari.data.model.team.bottari.TeamBottariFetchResponse
import com.bottari.data.model.team.bottari.TeamBottariJoinRequest
import com.bottari.data.model.team.bottari.item.AssignedItemsCreateRequest
import com.bottari.data.model.team.bottari.item.AssignedItemsFetchResponse
import com.bottari.data.model.team.bottari.item.AssignedItemsUpdateRequest
import com.bottari.data.model.team.bottari.item.PersonalItemsCreateRequest
import com.bottari.data.model.team.bottari.item.PersonalItemsFetchResponse
import com.bottari.data.model.team.bottari.item.SharedItemsCreateRequest
import com.bottari.data.model.team.bottari.item.SharedItemsFetchResponse
import com.bottari.data.model.team.bottari.item.TeamBottariItemCheckUpdateRequest
import com.bottari.data.model.team.bottari.item.TeamBottariItemChecklistFetchResponse
import com.bottari.data.model.team.bottari.item.TeamBottariItemDeleteRequest
import com.bottari.data.model.team.bottari.item.TeamBottariItemRemindRequest
import com.bottari.data.model.team.bottari.item.TeamBottariItemUnCheckUpdateRequest
import com.bottari.data.model.team.member.TeamMemberFetchResponse
import com.bottari.data.model.team.member.TeamMemberNameFetchResponse
import com.bottari.data.model.team.member.TeamMemberStatusFetchResponse

interface TeamBottariRemoteDataSource {
    suspend fun createBottari(request: TeamBottariCreateRequest): Result<Long?>

    suspend fun fetchTeamBottari(teamBottariId: Long): Result<TeamBottariItemChecklistFetchResponse>

    suspend fun uncheckBottariItem(
        bottariItemId: Long,
        request: TeamBottariItemUnCheckUpdateRequest,
    ): Result<Unit>

    suspend fun checkBottariItem(
        bottariItemId: Long,
        request: TeamBottariItemCheckUpdateRequest,
    ): Result<Unit>

    suspend fun fetchTeamBottaries(): Result<List<TeamBottariFetchResponse>>

    suspend fun fetchTeamBottariDetail(teamBottariId: Long): Result<TeamBottariFetchDetailResponse>

    suspend fun fetchTeamBottariStatus(id: Long): Result<FetchTeamBottariStatusResponse>

    suspend fun sendRemindByItem(
        id: Long,
        type: TeamBottariItemRemindRequest,
    ): Result<Unit>

    suspend fun fetchTeamMembers(id: Long): Result<TeamMemberFetchResponse>

    suspend fun fetchTeamMembersStatus(id: Long): Result<List<TeamMemberStatusFetchResponse>>

    suspend fun createTeamBottariSharedItem(
        id: Long,
        request: SharedItemsCreateRequest,
    ): Result<Unit>

    suspend fun createTeamBottariPersonalItem(
        id: Long,
        request: PersonalItemsCreateRequest,
    ): Result<Unit>

    suspend fun createTeamBottariAssignedItem(
        id: Long,
        request: AssignedItemsCreateRequest,
    ): Result<Unit>

    suspend fun deleteTeamBottariItem(
        id: Long,
        type: TeamBottariItemDeleteRequest,
    ): Result<Unit>

    suspend fun sendRemindByMemberMessage(
        teamBottariId: Long,
        memberId: Long,
    ): Result<Unit>

    suspend fun joinTeamBottari(request: TeamBottariJoinRequest): Result<Unit>

    suspend fun fetchTeamBottariMembers(teamBottariId: Long): Result<List<TeamMemberNameFetchResponse>>

    suspend fun fetchTeamAssignedItems(teamBottariId: Long): Result<List<AssignedItemsFetchResponse>>

    suspend fun fetchTeamSharedItems(teamBottariId: Long): Result<List<SharedItemsFetchResponse>>

    suspend fun fetchTeamPersonalItems(teamBottariId: Long): Result<List<PersonalItemsFetchResponse>>

    suspend fun saveTeamBottariAssignedItem(
        teamBottariId: Long,
        assignedItemId: Long,
        request: AssignedItemsUpdateRequest,
    ): Result<Unit>

    suspend fun exitTeamBottari(teamBottariId: Long): Result<Unit>
}
