package com.bottari.data.service

import com.bottari.data.model.member.CheckRegisteredMemberResponse
import com.bottari.data.model.member.RegisterMemberRequest
import com.bottari.data.model.member.SaveMemberNicknameRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface MemberService {
    @POST("/members")
    suspend fun registerMember(
        @Body request: RegisterMemberRequest,
    ): Response<Unit>

    @PATCH("/members/me")
    suspend fun saveMemberNickname(
        @Body request: SaveMemberNicknameRequest,
    ): Response<Unit>

    @GET("/members/check")
    suspend fun checkRegisteredMember(): Response<CheckRegisteredMemberResponse>
}
