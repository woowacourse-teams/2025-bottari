package com.bottari.core.data.source.remote

import com.bottari.core.data.common.safeApiCall
import com.bottari.core.network.dto.member.MemberNicknameSaveRequest
import com.bottari.core.network.dto.member.MemberRegisterCheckResponse
import com.bottari.core.network.dto.member.MemberRegisterRequest
import com.bottari.core.network.service.MemberService
import com.bottari.data.common.extension.extractIdFromHeader
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
