package com.bottari.data.source.remote

import com.bottari.data.model.member.MemberNicknameSaveRequest
import com.bottari.data.model.member.MemberRegisterCheckResponse
import com.bottari.data.model.member.MemberRegisterRequest

interface MemberRemoteDataSource {
    suspend fun registerMember(request: MemberRegisterRequest): Result<Long?>

    suspend fun saveMemberNickname(request: MemberNicknameSaveRequest): Result<Unit>

    suspend fun checkRegisteredMember(): Result<MemberRegisterCheckResponse>
}
