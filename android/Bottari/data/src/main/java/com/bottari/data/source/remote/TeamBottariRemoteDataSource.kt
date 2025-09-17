package com.bottari.data.source.remote

import com.bottari.data.model.remote.team.bottari.FetchTeamBottariStatusResponse
import com.bottari.data.model.remote.team.bottari.TeamBottariCreateRequest
import com.bottari.data.model.remote.team.bottari.TeamBottariDetailFetchResponse
import com.bottari.data.model.remote.team.bottari.TeamBottariFetchResponse
import com.bottari.data.model.remote.team.bottari.TeamBottariJoinRequest
import com.bottari.data.model.remote.team.bottari.item.request.AssignedItemsCreateRequest
import com.bottari.data.model.remote.team.bottari.item.request.AssignedItemsUpdateRequest
import com.bottari.data.model.remote.team.bottari.item.request.PersonalItemsCreateRequest
import com.bottari.data.model.remote.team.bottari.item.request.SharedItemsCreateRequest
import com.bottari.data.model.remote.team.bottari.item.request.TeamBottariItemCheckUpdateRequest
import com.bottari.data.model.remote.team.bottari.item.request.TeamBottariItemDeleteRequest
import com.bottari.data.model.remote.team.bottari.item.request.TeamBottariItemRemindRequest
import com.bottari.data.model.remote.team.bottari.item.request.TeamBottariItemUnCheckUpdateRequest
import com.bottari.data.model.remote.team.bottari.item.response.AssignedItemsFetchResponse
import com.bottari.data.model.remote.team.bottari.item.response.PersonalItemsFetchResponse
import com.bottari.data.model.remote.team.bottari.item.response.SharedItemsFetchResponse
import com.bottari.data.model.remote.team.bottari.item.response.TeamBottariItemChecklistFetchResponse
import com.bottari.data.model.remote.team.member.TeamMemberFetchResponse
import com.bottari.data.model.remote.team.member.TeamMemberNameFetchResponse
import com.bottari.data.model.remote.team.member.TeamMemberStatusFetchResponse
import com.bottari.domain.model.exception.BottariResult

interface TeamBottariRemoteDataSource {
    suspend fun createBottari(request: TeamBottariCreateRequest): BottariResult<Long>

    suspend fun fetchTeamBottari(teamBottariId: Long): BottariResult<TeamBottariItemChecklistFetchResponse>

    suspend fun uncheckBottariItem(
        bottariItemId: Long,
        request: TeamBottariItemUnCheckUpdateRequest,
    ): BottariResult<Unit>

    suspend fun checkBottariItem(
        bottariItemId: Long,
        request: TeamBottariItemCheckUpdateRequest,
    ): BottariResult<Unit>

    suspend fun fetchTeamBottaries(): BottariResult<List<TeamBottariFetchResponse>>

    suspend fun fetchTeamBottariDetail(teamBottariId: Long): BottariResult<TeamBottariDetailFetchResponse>

    suspend fun fetchTeamBottariStatus(id: Long): BottariResult<FetchTeamBottariStatusResponse>

    suspend fun sendRemindByItem(
        id: Long,
        type: TeamBottariItemRemindRequest,
    ): BottariResult<Unit>

    suspend fun fetchTeamMembers(id: Long): BottariResult<TeamMemberFetchResponse>

    suspend fun fetchTeamMembersStatus(id: Long): BottariResult<List<TeamMemberStatusFetchResponse>>

    suspend fun createTeamBottariSharedItem(
        id: Long,
        request: SharedItemsCreateRequest,
    ): BottariResult<Long>

    suspend fun createTeamBottariPersonalItem(
        id: Long,
        request: PersonalItemsCreateRequest,
    ): BottariResult<Long>

    suspend fun createTeamBottariAssignedItem(
        id: Long,
        request: AssignedItemsCreateRequest,
    ): BottariResult<Long>

    suspend fun deleteTeamBottariItem(
        id: Long,
        type: TeamBottariItemDeleteRequest,
    ): BottariResult<Unit>

    suspend fun sendRemindByMemberMessage(
        teamBottariId: Long,
        memberId: Long,
    ): BottariResult<Unit>

    suspend fun joinTeamBottari(request: TeamBottariJoinRequest): BottariResult<Unit>

    suspend fun fetchTeamBottariMembers(teamBottariId: Long): BottariResult<List<TeamMemberNameFetchResponse>>

    suspend fun fetchTeamAssignedItems(teamBottariId: Long): BottariResult<List<AssignedItemsFetchResponse>>

    suspend fun fetchTeamSharedItems(teamBottariId: Long): BottariResult<List<SharedItemsFetchResponse>>

    suspend fun fetchTeamPersonalItems(teamBottariId: Long): BottariResult<List<PersonalItemsFetchResponse>>

    suspend fun saveTeamBottariAssignedItem(
        teamBottariId: Long,
        assignedItemId: Long,
        request: AssignedItemsUpdateRequest,
    ): BottariResult<Unit>

    suspend fun exitTeamBottari(teamBottariId: Long): BottariResult<Unit>
}
