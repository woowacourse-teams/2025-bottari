package com.bottari.domain.repository

import com.bottari.domain.model.member.Member
import com.bottari.domain.model.member.RegisteredMember

interface MemberRepository {
    suspend fun registerMember(ssaid: String): Result<Unit>

    suspend fun saveMemberNickname(member: Member): Result<Unit>

    suspend fun checkRegisteredMember(ssaid: String): Result<RegisteredMember>
}
