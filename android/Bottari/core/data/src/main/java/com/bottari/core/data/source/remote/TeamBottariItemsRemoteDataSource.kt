package com.bottari.core.data.source.remote

import com.bottari.core.network.dto.team.bottari.FetchTeamBottariStatusResponse
import com.bottari.core.network.dto.team.bottari.item.AssignedItemsCreateRequest
import com.bottari.core.network.dto.team.bottari.item.AssignedItemsFetchResponse
import com.bottari.core.network.dto.team.bottari.item.AssignedItemsUpdateRequest
import com.bottari.core.network.dto.team.bottari.item.PersonalItemsCreateRequest
import com.bottari.core.network.dto.team.bottari.item.PersonalItemsFetchResponse
import com.bottari.core.network.dto.team.bottari.item.SharedItemsCreateRequest
import com.bottari.core.network.dto.team.bottari.item.SharedItemsFetchResponse
import com.bottari.core.network.dto.team.bottari.item.TeamBottariItemCheckUpdateRequest
import com.bottari.core.network.dto.team.bottari.item.TeamBottariItemChecklistFetchResponse
import com.bottari.core.network.dto.team.bottari.item.TeamBottariItemDeleteRequest
import com.bottari.core.network.dto.team.bottari.item.TeamBottariItemRemindRequest
import com.bottari.core.network.dto.team.bottari.item.TeamBottariItemUnCheckUpdateRequest

interface TeamBottariItemsRemoteDataSource {
    suspend fun fetchTeamBottari(teamBottariId: Long): Result<TeamBottariItemChecklistFetchResponse>

    suspend fun uncheckBottariItem(
        bottariItemId: Long,
        request: TeamBottariItemUnCheckUpdateRequest,
    ): Result<Unit>

    suspend fun checkBottariItem(
        bottariItemId: Long,
        request: TeamBottariItemCheckUpdateRequest,
    ): Result<Unit>

    suspend fun fetchTeamBottariStatus(id: Long): Result<FetchTeamBottariStatusResponse>

    suspend fun sendRemindByItem(
        id: Long,
        type: TeamBottariItemRemindRequest,
    ): Result<Unit>

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

    suspend fun fetchTeamAssignedItems(teamBottariId: Long): Result<List<AssignedItemsFetchResponse>>

    suspend fun fetchTeamSharedItems(teamBottariId: Long): Result<List<SharedItemsFetchResponse>>

    suspend fun fetchTeamPersonalItems(teamBottariId: Long): Result<List<PersonalItemsFetchResponse>>

    suspend fun saveTeamBottariAssignedItem(
        teamBottariId: Long,
        assignedItemId: Long,
        request: AssignedItemsUpdateRequest,
    ): Result<Unit>
}
