package com.bottari.data.source.remote

import com.bottari.data.model.team.CreateTeamBottariRequest
import com.bottari.data.model.team.FetchTeamBottariChecklistResponse
import com.bottari.data.model.team.FetchTeamBottariDetailResponse
import com.bottari.data.model.team.FetchTeamBottariResponse
import com.bottari.data.model.team.FetchTeamBottariStatusResponse
import com.bottari.data.model.team.FetchTeamMemberStatusResponse
import com.bottari.data.model.team.FetchTeamMembersResponse
import com.bottari.data.model.team.ItemTypeRequest

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

    suspend fun remind(
        id: Long,
        type: ItemTypeRequest,
    ): Result<Unit>

    suspend fun fetchTeamMembers(id: Long): Result<FetchTeamMembersResponse>

    suspend fun fetchTeamMembersStatus(id: Long): Result<List<FetchTeamMemberStatusResponse>>
}
