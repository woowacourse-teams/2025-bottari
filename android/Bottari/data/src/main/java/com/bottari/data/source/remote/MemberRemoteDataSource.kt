package com.bottari.data.source.remote

import com.bottari.data.model.member.CheckRegisteredMemberResponse
import com.bottari.data.model.member.RegisterMemberRequest
import com.bottari.data.model.member.SaveMemberNicknameRequest

interface MemberRemoteDataSource {
    suspend fun registerMember(request: RegisterMemberRequest): Result<Unit>

    suspend fun saveMemberNickname(
        ssaid: String,
        request: SaveMemberNicknameRequest,
    ): Result<Unit>

    suspend fun checkRegisteredMember(ssaid: String): Result<CheckRegisteredMemberResponse>
}
