package com.bottari.data.repository

import com.bottari.data.mapper.MemberMapper.toDomain
import com.bottari.data.mapper.MemberMapper.toRequest
import com.bottari.data.mapper.MemberMapper.toUpdateRequest
import com.bottari.data.source.remote.MemberRemoteDataSource
import com.bottari.domain.model.member.Member
import com.bottari.domain.model.member.RegisteredMember
import com.bottari.domain.repository.MemberRepository

class MemberRepositoryImpl(
    private val memberRemoteDataSource: MemberRemoteDataSource,
) : MemberRepository {
    override suspend fun registerMember(member: Member): Result<Unit> = memberRemoteDataSource.registerMember(member.toRequest())

    override suspend fun saveMemberNickname(
        ssaid: String,
        member: Member,
    ): Result<Unit> = memberRemoteDataSource.saveMemberNickname(ssaid, member.toUpdateRequest())

    override suspend fun checkRegisteredMember(ssaid: String): Result<RegisteredMember> =
        memberRemoteDataSource.checkRegisteredMember(ssaid).mapCatching { it.toDomain() }
}
