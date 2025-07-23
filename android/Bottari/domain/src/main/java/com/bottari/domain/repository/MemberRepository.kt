package com.bottari.domain.repository

import com.bottari.domain.model.member.Member

interface MemberRepository {
    suspend fun registerMember(member: Member): Result<Boolean>
}
