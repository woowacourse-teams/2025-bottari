package com.bottari.domain.repository

import com.bottari.domain.model.exception.BottariResult
import com.bottari.domain.model.member.Nickname
import com.bottari.domain.model.member.RegisteredMember

interface MemberRepository {
    suspend fun registerMember(fcmToken: String): BottariResult<Long>

    suspend fun saveMemberNickname(nickname: Nickname): BottariResult<Unit>

    suspend fun checkRegisteredMember(): BottariResult<RegisteredMember>

    suspend fun getInstallationId(): BottariResult<String>

    suspend fun getMemberId(): BottariResult<Long>
}
