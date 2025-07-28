package com.bottari.data.service

import com.bottari.data.model.member.CheckRegisteredMemberResponse
import com.bottari.data.model.member.RegisterMemberRequest
import com.bottari.data.model.member.UpdateMemberNicknameRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST

interface MemberService {
    @POST("/members")
    suspend fun registerMember(
        @Body request: RegisterMemberRequest,
    ): Response<Unit>

    @PATCH("/members/me")
    suspend fun updateMemberNickname(
        @Header("ssaid") ssaid: String,
        @Body request: UpdateMemberNicknameRequest,
    ): Response<Unit>

    @GET("/members/check")
    suspend fun checkRegisteredMember(
        @Header("ssaid") ssaid: String,
    ): Response<CheckRegisteredMemberResponse>
}
