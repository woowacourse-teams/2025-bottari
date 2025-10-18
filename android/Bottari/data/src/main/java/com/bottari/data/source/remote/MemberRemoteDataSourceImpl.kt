package com.bottari.data.source.remote

import com.bottari.data.common.extension.extractIdFromHeader
import com.bottari.data.common.util.safeApiCall
import com.bottari.data.model.remote.member.MemberNicknameSaveRequest
import com.bottari.data.model.remote.member.MemberRegisterCheckResponse
import com.bottari.data.model.remote.member.MemberRegisterRequest
import com.bottari.data.service.MemberService
import javax.inject.Inject

class MemberRemoteDataSourceImpl @Inject constructor(
    private val memberService: MemberService,
) : MemberRemoteDataSource {
    override suspend fun registerMember(request: MemberRegisterRequest): Result<Long?> =
        runCatching {
            val response = memberService.registerMember(request)
            response.extractIdFromHeader(HEADER_MEMBER_ID_PREFIX)
        }

    override suspend fun saveMemberNickname(request: MemberNicknameSaveRequest): Result<Unit> =
        safeApiCall { memberService.saveMemberNickname(request) }

    override suspend fun checkRegisteredMember(): Result<MemberRegisterCheckResponse> =
        safeApiCall {
            memberService.checkRegisteredMember()
        }

    companion object {
        private const val HEADER_MEMBER_ID_PREFIX = "/members/"
    }
}
