package com.bottari.data.source.remote

import com.bottari.data.model.member.RegisterMemberRequest
import com.bottari.data.service.MemberService
import com.bottari.data.util.safeApiCall

class MemberRemoteDataSourceImpl(
    private val memberService: MemberService,
) : MemberRemoteDataSource {
    override suspend fun registerMember(request: RegisterMemberRequest): Result<Unit> =
        safeApiCall {
            memberService.registerMember(request)
        }
}


