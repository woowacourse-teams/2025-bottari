package com.bottari.data.source.remote

import com.bottari.data.model.team.bottari.CreateTeamBottariRequest
import com.bottari.data.model.team.bottari.FetchTeamBottariChecklistResponse
import com.bottari.data.model.team.bottari.FetchTeamBottariDetailResponse
import com.bottari.data.model.team.bottari.FetchTeamBottariResponse
import com.bottari.data.model.team.bottari.FetchTeamBottariStatusResponse
import com.bottari.data.model.team.bottari.JoinTeamBottariRequest
import com.bottari.data.model.team.item.CreateTeamBottariAssignedItemRequest
import com.bottari.data.model.team.item.CreateTeamBottariPersonalItemRequest
import com.bottari.data.model.team.item.CreateTeamBottariSharedItemRequest
import com.bottari.data.model.team.item.DeleteTeamBottariItemRequest
import com.bottari.data.model.team.item.FetchTeamAssignedItemResponse
import com.bottari.data.model.team.item.FetchTeamPersonalItemResponse
import com.bottari.data.model.team.item.FetchTeamSharedItemResponse
import com.bottari.data.model.team.item.SaveTeamAssignedItemRequest
import com.bottari.data.model.team.item.SendRemindByItemRequest
import com.bottari.data.model.team.item.UpdateTeamItemCheckRequest
import com.bottari.data.model.team.member.FetchTeamBottariMemberResponse
import com.bottari.data.model.team.member.FetchTeamMemberStatusResponse
import com.bottari.data.model.team.member.FetchTeamMembersResponse

interface TeamBottariRemoteDataSource {
    suspend fun createBottari(request: CreateTeamBottariRequest): Result<Long?>

    suspend fun fetchTeamBottari(teamBottariId: Long): Result<FetchTeamBottariChecklistResponse>

    suspend fun uncheckBottariItem(
        bottariItemId: Long,
        request: UpdateTeamItemCheckRequest,
    ): Result<Unit>

    suspend fun checkBottariItem(
        bottariItemId: Long,
        request: UpdateTeamItemCheckRequest,
    ): Result<Unit>

    suspend fun fetchTeamBottaries(): Result<List<FetchTeamBottariResponse>>

    suspend fun fetchTeamBottariDetail(teamBottariId: Long): Result<FetchTeamBottariDetailResponse>

    suspend fun fetchTeamBottariStatus(id: Long): Result<FetchTeamBottariStatusResponse>

    suspend fun sendRemindByItem(
        id: Long,
        type: SendRemindByItemRequest,
    ): Result<Unit>

    suspend fun fetchTeamMembers(id: Long): Result<FetchTeamMembersResponse>

    suspend fun fetchTeamMembersStatus(id: Long): Result<List<FetchTeamMemberStatusResponse>>

    suspend fun createTeamBottariSharedItem(
        id: Long,
        request: CreateTeamBottariSharedItemRequest,
    ): Result<Unit>

    suspend fun createTeamBottariPersonalItem(
        id: Long,
        request: CreateTeamBottariPersonalItemRequest,
    ): Result<Unit>

    suspend fun createTeamBottariAssignedItem(
        id: Long,
        request: CreateTeamBottariAssignedItemRequest,
    ): Result<Unit>

    suspend fun deleteTeamBottariItem(
        id: Long,
        type: DeleteTeamBottariItemRequest,
    ): Result<Unit>

    suspend fun sendRemindByMemberMessage(
        teamBottariId: Long,
        memberId: Long,
    ): Result<Unit>

    suspend fun joinTeamBottari(request: JoinTeamBottariRequest): Result<Unit>

    suspend fun fetchTeamBottariMembers(teamBottariId: Long): Result<List<FetchTeamBottariMemberResponse>>

    suspend fun fetchTeamAssignedItems(teamBottariId: Long): Result<List<FetchTeamAssignedItemResponse>>

    suspend fun fetchTeamSharedItems(teamBottariId: Long): Result<List<FetchTeamSharedItemResponse>>

    suspend fun fetchTeamPersonalItems(teamBottariId: Long): Result<List<FetchTeamPersonalItemResponse>>

    suspend fun saveTeamBottariAssignedItem(
        teamBottariId: Long,
        assignedItemId: Long,
        request: SaveTeamAssignedItemRequest,
    ): Result<Unit>

    suspend fun exitTeamBottari(teamBottariId: Long): Result<Unit>
}
