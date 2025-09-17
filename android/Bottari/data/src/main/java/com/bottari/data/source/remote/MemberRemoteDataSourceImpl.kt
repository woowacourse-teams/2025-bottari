package com.bottari.data.source.remote

import com.bottari.data.model.remote.member.MemberNicknameSaveRequest
import com.bottari.data.model.remote.member.MemberRegisterCheckResponse
import com.bottari.data.model.remote.member.MemberRegisterRequest
import com.bottari.data.service.MemberService
import com.bottari.domain.model.exception.BottariResult

class MemberRemoteDataSourceImpl(
    private val memberService: MemberService,
) : MemberRemoteDataSource {
    override suspend fun registerMember(request: MemberRegisterRequest): BottariResult<Long> = memberService.registerMember(request)

    override suspend fun saveMemberNickname(request: MemberNicknameSaveRequest): BottariResult<Unit> =
        memberService.saveMemberNickname(request)

    override suspend fun checkRegisteredMember(): BottariResult<MemberRegisterCheckResponse> = memberService.checkRegisteredMember()
}
