package com.bottari.data.source.remote

import com.bottari.data.model.remote.member.MemberNicknameSaveRequest
import com.bottari.data.model.remote.member.MemberRegisterCheckResponse
import com.bottari.data.model.remote.member.MemberRegisterRequest

interface MemberRemoteDataSource {
    suspend fun registerMember(request: MemberRegisterRequest): Result<Long?>

    suspend fun saveMemberNickname(request: MemberNicknameSaveRequest): Result<Unit>

    suspend fun checkRegisteredMember(): Result<MemberRegisterCheckResponse>
}
