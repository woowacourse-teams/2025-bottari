package com.bottari.data.source.remote

import com.bottari.data.model.team.bottari.CreateTeamBottariRequest
import com.bottari.data.model.team.bottari.FetchTeamBottariChecklistResponse
import com.bottari.data.model.team.bottari.FetchTeamBottariDetailResponse
import com.bottari.data.model.team.bottari.FetchTeamBottariResponse
import com.bottari.data.model.team.bottari.FetchTeamBottariStatusResponse
import com.bottari.data.model.team.bottari.JoinTeamBottariRequest
import com.bottari.data.model.team.bottari.item.AssignedItemsCreateRequest
import com.bottari.data.model.team.bottari.item.AssignedItemsResponse
import com.bottari.data.model.team.bottari.item.AssignedItemsUpdateRequest
import com.bottari.data.model.team.bottari.item.PersonalItemsCreateRequest
import com.bottari.data.model.team.bottari.item.PersonalItemsResponse
import com.bottari.data.model.team.bottari.item.SharedItemsCreateRequest
import com.bottari.data.model.team.bottari.item.SharedItemsResponse
import com.bottari.data.model.team.bottari.item.TeamBottariItemCheckUpdateRequest
import com.bottari.data.model.team.bottari.item.TeamBottariItemDeleteRequest
import com.bottari.data.model.team.bottari.item.TeamBottariItemRemindRequest
import com.bottari.data.model.team.bottari.item.TeamBottariItemUnCheckUpdateRequest
import com.bottari.data.model.team.member.FetchTeamBottariMemberResponse
import com.bottari.data.model.team.member.FetchTeamMemberStatusResponse
import com.bottari.data.model.team.member.FetchTeamMembersResponse

interface TeamBottariRemoteDataSource {
    suspend fun createBottari(request: CreateTeamBottariRequest): Result<Long?>

    suspend fun fetchTeamBottari(teamBottariId: Long): Result<FetchTeamBottariChecklistResponse>

    suspend fun uncheckBottariItem(
        bottariItemId: Long,
        request: TeamBottariItemUnCheckUpdateRequest,
    ): Result<Unit>

    suspend fun checkBottariItem(
        bottariItemId: Long,
        request: TeamBottariItemCheckUpdateRequest,
    ): Result<Unit>

    suspend fun fetchTeamBottaries(): Result<List<FetchTeamBottariResponse>>

    suspend fun fetchTeamBottariDetail(teamBottariId: Long): Result<FetchTeamBottariDetailResponse>

    suspend fun fetchTeamBottariStatus(id: Long): Result<FetchTeamBottariStatusResponse>

    suspend fun sendRemindByItem(
        id: Long,
        type: TeamBottariItemRemindRequest,
    ): Result<Unit>

    suspend fun fetchTeamMembers(id: Long): Result<FetchTeamMembersResponse>

    suspend fun fetchTeamMembersStatus(id: Long): Result<List<FetchTeamMemberStatusResponse>>

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

    suspend fun joinTeamBottari(request: JoinTeamBottariRequest): Result<Unit>

    suspend fun fetchTeamBottariMembers(teamBottariId: Long): Result<List<FetchTeamBottariMemberResponse>>

    suspend fun fetchTeamAssignedItems(teamBottariId: Long): Result<List<AssignedItemsResponse>>

    suspend fun fetchTeamSharedItems(teamBottariId: Long): Result<List<SharedItemsResponse>>

    suspend fun fetchTeamPersonalItems(teamBottariId: Long): Result<List<PersonalItemsResponse>>

    suspend fun saveTeamBottariAssignedItem(
        teamBottariId: Long,
        assignedItemId: Long,
        request: AssignedItemsUpdateRequest,
    ): Result<Unit>

    suspend fun exitTeamBottari(teamBottariId: Long): Result<Unit>
}
