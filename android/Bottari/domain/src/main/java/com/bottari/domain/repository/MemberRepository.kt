package com.bottari.domain.repository

import com.bottari.domain.model.member.RegisteredMember

interface MemberRepository {
    suspend fun registerMember(): Result<Long?>

    suspend fun saveMemberNickname(nickname: String): Result<Unit>

    suspend fun checkRegisteredMember(): Result<RegisteredMember>
}
