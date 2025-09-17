package com.bottari.data.service

import com.bottari.data.model.remote.member.MemberNicknameSaveRequest
import com.bottari.data.model.remote.member.MemberRegisterCheckResponse
import com.bottari.data.model.remote.member.MemberRegisterRequest
import com.bottari.domain.model.exception.BottariResult
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface MemberService {
    @POST("/members")
    suspend fun registerMember(
        @Body request: MemberRegisterRequest,
    ): BottariResult<Long>

    @PATCH("/members/me")
    suspend fun saveMemberNickname(
        @Body request: MemberNicknameSaveRequest,
    ): BottariResult<Unit>

    @GET("/members/check")
    suspend fun checkRegisteredMember(): BottariResult<MemberRegisterCheckResponse>
}
