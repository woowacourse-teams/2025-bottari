package com.bottari.data.source.remote

import com.bottari.data.model.remote.team.bottari.FetchTeamBottariStatusResponse
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
import com.bottari.data.service.TeamBottariService
import com.bottari.domain.model.exception.BottariResult

class TeamBottariRemoteDataSourceImpl(
    private val teamBottariService: TeamBottariService,
) : TeamBottariRemoteDataSource {
    override suspend fun createBottari(request: com.bottari.data.model.remote.team.bottari.TeamBottariCreateRequest): BottariResult<Long> =
        teamBottariService.createTeamBottari(request)

    override suspend fun fetchTeamBottari(teamBottariId: Long): BottariResult<TeamBottariItemChecklistFetchResponse> =
        teamBottariService.fetchTeamBottari(teamBottariId)

    override suspend fun uncheckBottariItem(
        bottariItemId: Long,
        request: TeamBottariItemUnCheckUpdateRequest,
    ): BottariResult<Unit> = teamBottariService.uncheckTeamBottariItem(bottariItemId, request)

    override suspend fun checkBottariItem(
        bottariItemId: Long,
        request: TeamBottariItemCheckUpdateRequest,
    ): BottariResult<Unit> = teamBottariService.checkTeamBottariItem(bottariItemId, request)

    override suspend fun fetchTeamBottaries(): BottariResult<List<TeamBottariFetchResponse>> = teamBottariService.fetchTeamBottaries()

    override suspend fun fetchTeamBottariDetail(teamBottariId: Long): BottariResult<TeamBottariDetailFetchResponse> =
        teamBottariService.fetchTeamBottariDetail(teamBottariId)

    override suspend fun fetchTeamBottariStatus(id: Long): BottariResult<FetchTeamBottariStatusResponse> =
        teamBottariService.fetchTeamBottariStatus(id)

    override suspend fun sendRemindByItem(
        id: Long,
        type: TeamBottariItemRemindRequest,
    ): BottariResult<Unit> = teamBottariService.sendRemindByItem(id, type)

    override suspend fun fetchTeamMembers(id: Long): BottariResult<TeamMemberFetchResponse> = teamBottariService.fetchTeamMembers(id)

    override suspend fun fetchTeamMembersStatus(id: Long): BottariResult<List<TeamMemberStatusFetchResponse>> =
        teamBottariService.fetchTeamMembersStatus(id)

    override suspend fun createTeamBottariSharedItem(
        id: Long,
        request: SharedItemsCreateRequest,
    ): BottariResult<Long> = teamBottariService.createTeamBottariSharedItem(id, request)

    override suspend fun createTeamBottariPersonalItem(
        id: Long,
        request: PersonalItemsCreateRequest,
    ): BottariResult<Long> = teamBottariService.createTeamBottariPersonalItem(id, request)

    override suspend fun createTeamBottariAssignedItem(
        id: Long,
        request: AssignedItemsCreateRequest,
    ): BottariResult<Long> = teamBottariService.createTeamBottariAssignedItem(id, request)

    override suspend fun deleteTeamBottariItem(
        id: Long,
        type: TeamBottariItemDeleteRequest,
    ): BottariResult<Unit> = teamBottariService.deleteTeamBottariItem(id, type)

    override suspend fun sendRemindByMemberMessage(
        teamBottariId: Long,
        memberId: Long,
    ): BottariResult<Unit> = teamBottariService.sendRemindByMemberMessage(teamBottariId, memberId)

    override suspend fun joinTeamBottari(request: TeamBottariJoinRequest): BottariResult<Unit> = teamBottariService.joinTeamBottari(request)

    override suspend fun fetchTeamBottariMembers(teamBottariId: Long): BottariResult<List<TeamMemberNameFetchResponse>> =
        teamBottariService.fetchTeamBottariMembers(teamBottariId)

    override suspend fun fetchTeamAssignedItems(teamBottariId: Long): BottariResult<List<AssignedItemsFetchResponse>> =
        teamBottariService.fetchTeamAssignedItems(teamBottariId)

    override suspend fun fetchTeamSharedItems(teamBottariId: Long): BottariResult<List<SharedItemsFetchResponse>> =
        teamBottariService.fetchTeamSharedItems(teamBottariId)

    override suspend fun fetchTeamPersonalItems(teamBottariId: Long): BottariResult<List<PersonalItemsFetchResponse>> =
        teamBottariService.fetchTeamPersonalItems(teamBottariId)

    override suspend fun saveTeamBottariAssignedItem(
        teamBottariId: Long,
        assignedItemId: Long,
        request: AssignedItemsUpdateRequest,
    ): BottariResult<Unit> = teamBottariService.saveTeamAssignedItem(teamBottariId, assignedItemId, request)

    override suspend fun exitTeamBottari(teamBottariId: Long): BottariResult<Unit> = teamBottariService.exitTeamBottari(teamBottariId)
}
