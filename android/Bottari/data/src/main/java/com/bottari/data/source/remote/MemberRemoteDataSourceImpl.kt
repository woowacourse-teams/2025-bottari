package com.bottari.data.source.remote

import com.bottari.data.common.extension.extractIdFromHeader
import com.bottari.data.common.util.safeApiCall
import com.bottari.data.model.member.MemberRegisterRequest
import com.bottari.data.model.member.MemberResponse
import com.bottari.data.model.member.MemberSaveNicknameRequest
import com.bottari.data.service.MemberService

class MemberRemoteDataSourceImpl(
    private val memberService: MemberService,
) : MemberRemoteDataSource {
    override suspend fun registerMember(request: MemberRegisterRequest): Result<Long?> =
        runCatching {
            val response = memberService.registerMember(request)
            response.extractIdFromHeader(HEADER_MEMBER_ID_PREFIX)
        }

    override suspend fun saveMemberNickname(request: MemberSaveNicknameRequest): Result<Unit> =
        safeApiCall { memberService.saveMemberNickname(request) }

    override suspend fun checkRegisteredMember(): Result<MemberResponse> =
        safeApiCall {
            memberService.checkRegisteredMember()
        }

    companion object {
        private const val HEADER_MEMBER_ID_PREFIX = "/members/"
    }
}
