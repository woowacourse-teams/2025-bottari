package com.bottari.data.source.remote

import com.bottari.data.model.member.CheckRegisteredMemberResponse
import com.bottari.data.model.member.RegisterMemberRequest
import com.bottari.data.model.member.SaveMemberNicknameRequest

interface MemberRemoteDataSource {
    suspend fun registerMember(request: RegisterMemberRequest): Result<Long?>

    suspend fun saveMemberNickname(request: SaveMemberNicknameRequest): Result<Unit>

    suspend fun checkRegisteredMember(): Result<CheckRegisteredMemberResponse>
}
