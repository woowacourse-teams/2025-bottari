package com.bottari.domain.repository

import com.bottari.domain.model.member.Nickname
import com.bottari.domain.model.member.RegisteredMember

interface MemberRepository {
    suspend fun registerMember(fcmToken: String): Result<Long?>

    suspend fun saveMemberNickname(nickname: Nickname): Result<Unit>

    suspend fun checkRegisteredMember(): Result<RegisteredMember>
}
