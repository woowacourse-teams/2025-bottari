package com.bottari.data.source.remote

import com.bottari.data.common.extension.extractIdFromHeader
import com.bottari.data.common.util.safeApiCall
import com.bottari.data.model.member.CheckRegisteredMemberResponse
import com.bottari.data.model.member.RegisterMemberRequest
import com.bottari.data.model.member.SaveMemberNicknameRequest
import com.bottari.data.service.MemberService

class MemberRemoteDataSourceImpl(
    private val memberService: MemberService,
) : MemberRemoteDataSource {
    override suspend fun registerMember(request: RegisterMemberRequest): Result<Long?> =
        runCatching {
            val response = memberService.registerMember(request)
            response.extractIdFromHeader(HEADER_MEMBER_ID_PREFIX)
        }

    override suspend fun saveMemberNickname(request: SaveMemberNicknameRequest): Result<Unit> =
        safeApiCall { memberService.saveMemberNickname(request) }

    override suspend fun checkRegisteredMember(): Result<CheckRegisteredMemberResponse> =
        safeApiCall {
            memberService.checkRegisteredMember()
        }

    companion object {
        private const val HEADER_MEMBER_ID_PREFIX = "/members/"
    }
}
