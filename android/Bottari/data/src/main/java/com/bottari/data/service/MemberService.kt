package com.bottari.data.service

import com.bottari.data.model.member.MemberRegisterRequest
import com.bottari.data.model.member.MemberResponse
import com.bottari.data.model.member.MemberSaveNicknameRequest
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
        @Body request: MemberSaveNicknameRequest,
    ): Response<Unit>

    @GET("/members/check")
    suspend fun checkRegisteredMember(): Response<MemberResponse>
}
