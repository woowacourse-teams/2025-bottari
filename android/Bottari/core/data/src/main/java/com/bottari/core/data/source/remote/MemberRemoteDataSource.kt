package com.bottari.core.data.source.remote

import com.bottari.core.network.dto.member.MemberNicknameSaveRequest
import com.bottari.core.network.dto.member.MemberRegisterCheckResponse
import com.bottari.core.network.dto.member.MemberRegisterRequest

interface MemberRemoteDataSource {
    suspend fun registerMember(request: MemberRegisterRequest): Result<Long?>

    suspend fun saveMemberNickname(request: MemberNicknameSaveRequest): Result<Unit>

    suspend fun checkRegisteredMember(): Result<MemberRegisterCheckResponse>
}
