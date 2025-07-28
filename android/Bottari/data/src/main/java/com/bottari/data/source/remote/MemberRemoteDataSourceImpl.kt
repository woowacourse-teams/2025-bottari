package com.bottari.data.source.remote

import com.bottari.data.model.member.CheckRegisteredMemberResponse
import com.bottari.data.model.member.RegisterMemberRequest
import com.bottari.data.model.member.UpdateMemberNicknameRequest
import com.bottari.data.service.MemberService
import com.bottari.data.util.safeApiCall

class MemberRemoteDataSourceImpl(
    private val memberService: MemberService,
) : MemberRemoteDataSource {
    override suspend fun registerMember(request: RegisterMemberRequest): Result<Unit> =
        safeApiCall {
            memberService.registerMember(request)
        }

    override suspend fun updateMemberNickname(
        ssaid: String,
        request: UpdateMemberNicknameRequest,
    ): Result<Unit> = safeApiCall { memberService.updateMemberNickname(ssaid, request) }

    override suspend fun checkRegisteredMember(ssaid: String): Result<CheckRegisteredMemberResponse> =
        safeApiCall {
            memberService.checkRegisteredMember(ssaid)
        }
}
