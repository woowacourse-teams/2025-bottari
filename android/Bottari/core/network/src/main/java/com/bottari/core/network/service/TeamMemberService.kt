package com.bottari.core.network.service

import com.bottari.core.network.dto.team.bottari.TeamBottariJoinRequest
import com.bottari.core.network.dto.team.member.TeamMemberFetchResponse
import com.bottari.core.network.dto.team.member.TeamMemberNameFetchResponse
import com.bottari.core.network.dto.team.member.TeamMemberStatusFetchResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TeamMemberService {
    @GET("/team-bottaries/{teamBottariId}/members/status")
    suspend fun fetchTeamMembersStatus(
        @Path("teamBottariId") id: Long,
    ): Response<List<TeamMemberStatusFetchResponse>>

    @GET("/team-bottaries/{teamBottariId}/members")
    suspend fun fetchTeamMembers(
        @Path("teamBottariId") id: Long,
    ): Response<TeamMemberFetchResponse>

    @POST("/team-bottaries/{teamBottariId}/members/{memberId}/remind")
    suspend fun sendRemindByMemberMessage(
        @Path("teamBottariId") teamBottariId: Long,
        @Path("memberId") memberId: Long,
    ): Response<Unit>

    @POST("/team-bottaries/join")
    suspend fun joinTeamBottari(
        @Body request: TeamBottariJoinRequest,
    ): Response<Unit>

    @GET("/team-bottaries/{teamBottariId}/members/name")
    suspend fun fetchTeamBottariMembers(
        @Path("teamBottariId") teamBottariId: Long,
    ): Response<List<TeamMemberNameFetchResponse>>
}
