package com.bottari.data.service

import com.bottari.data.model.member.RegisterMemberRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface MemberService {
    @POST("/members")
    suspend fun registerMember(
        @Body request: RegisterMemberRequest,
    ): Response<Unit>
}
