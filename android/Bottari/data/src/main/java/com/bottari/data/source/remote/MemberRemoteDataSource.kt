package com.bottari.data.source.remote

import com.bottari.data.model.member.MemberRegisterRequest
import com.bottari.data.model.member.MemberResponse
import com.bottari.data.model.member.MemberSaveNicknameRequest

interface MemberRemoteDataSource {
    suspend fun registerMember(request: MemberRegisterRequest): Result<Long?>

    suspend fun saveMemberNickname(request: MemberSaveNicknameRequest): Result<Unit>

    suspend fun checkRegisteredMember(): Result<MemberResponse>
}
