package com.bottari.core.network.service

import com.bottari.core.network.dto.member.MemberNicknameSaveRequest
import com.bottari.core.network.dto.member.MemberRegisterCheckResponse
import com.bottari.core.network.dto.member.MemberRegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface MemberService {
    @POST("/members")
    suspend fun registerMember(
        @Body request: MemberRegisterRequest,
    ): Response<Unit>

    @PATCH("/members/me")
    suspend fun saveMemberNickname(
        @Body request: MemberNicknameSaveRequest,
    ): Response<Unit>

    @GET("/members/check")
    suspend fun checkRegisteredMember(): Response<MemberRegisterCheckResponse>
}
