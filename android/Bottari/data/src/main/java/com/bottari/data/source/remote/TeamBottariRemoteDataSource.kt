package com.bottari.data.source.remote

import com.bottari.data.model.team.CreateTeamBottariAssignedItemRequest
import com.bottari.data.model.team.CreateTeamBottariPersonalItemRequest
import com.bottari.data.model.team.CreateTeamBottariRequest
import com.bottari.data.model.team.CreateTeamBottariSharedItemRequest
import com.bottari.data.model.team.DeleteTeamBottariItemRequest
import com.bottari.data.model.team.FetchTeamBottariChecklistResponse
import com.bottari.data.model.team.FetchTeamBottariDetailResponse
import com.bottari.data.model.team.FetchTeamBottariMemberResponse
import com.bottari.data.model.team.FetchTeamBottariResponse
import com.bottari.data.model.team.FetchTeamBottariStatusResponse
import com.bottari.data.model.team.FetchTeamMemberStatusResponse
import com.bottari.data.model.team.FetchTeamMembersResponse
import com.bottari.data.model.team.ItemTypeRequest
import com.bottari.data.model.team.JoinTeamBottariRequest
import com.bottari.data.model.team.SaveTeamBottariAssignedItemRequest
import com.bottari.data.model.teamItem.FetchTeamAssignedItemResponse
import com.bottari.data.model.teamItem.FetchTeamPersonalItemResponse
import com.bottari.data.model.teamItem.FetchTeamSharedItemResponse

interface TeamBottariRemoteDataSource {
    suspend fun createBottari(request: CreateTeamBottariRequest): Result<Long?>

    suspend fun fetchTeamBottari(teamBottariId: Long): Result<FetchTeamBottariChecklistResponse>

    suspend fun uncheckBottariItem(
        bottariItemId: Long,
        request: ItemTypeRequest,
    ): Result<Unit>

    suspend fun checkBottariItem(
        bottariItemId: Long,
        request: ItemTypeRequest,
    ): Result<Unit>

    suspend fun fetchTeamBottaries(): Result<List<FetchTeamBottariResponse>>

    suspend fun fetchTeamBottariDetail(teamBottariId: Long): Result<FetchTeamBottariDetailResponse>

    suspend fun fetchTeamBottariStatus(id: Long): Result<FetchTeamBottariStatusResponse>

    suspend fun sendRemindByItem(
        id: Long,
        type: ItemTypeRequest,
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
        request: SaveTeamBottariAssignedItemRequest,
    ): Result<Unit>
}
