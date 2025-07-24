package com.bottari.domain.repository

import com.bottari.domain.model.member.Member
import com.bottari.domain.model.member.RegisteredMember

interface MemberRepository {
    suspend fun registerMember(member: Member): Result<Boolean>

    suspend fun checkRegisteredMember(ssaid: String): Result<RegisteredMember>
}
