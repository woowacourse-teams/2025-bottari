package com.bottari.data.source.remote

import com.bottari.data.model.member.CheckRegisteredMemberResponse
import com.bottari.data.model.member.RegisterMemberRequest
import com.bottari.data.model.member.UpdateMemberNicknameRequest

interface MemberRemoteDataSource {
    suspend fun registerMember(request: RegisterMemberRequest): Result<Unit>

    suspend fun saveMemberNickname(
        ssaid: String,
        request: UpdateMemberNicknameRequest,
    ): Result<Unit>

    suspend fun checkRegisteredMember(ssaid: String): Result<CheckRegisteredMemberResponse>
}
