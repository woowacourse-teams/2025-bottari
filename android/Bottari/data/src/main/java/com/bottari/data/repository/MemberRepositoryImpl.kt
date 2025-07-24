package com.bottari.data.repository

import com.bottari.data.mapper.MemberMapper.toDomain
import com.bottari.data.mapper.MemberMapper.toRequest
import com.bottari.data.source.remote.MemberRemoteDataSource
import com.bottari.domain.model.member.Member
import com.bottari.domain.model.member.RegisteredMember
import com.bottari.domain.repository.MemberRepository

class MemberRepositoryImpl(
    private val memberRemoteDataSource: MemberRemoteDataSource,
) : MemberRepository {
    override suspend fun registerMember(member: Member): Result<Boolean> =
        memberRemoteDataSource.registerMember(member.toRequest()).map { true }

    override suspend fun checkRegisteredMember(ssaid: String): Result<RegisteredMember> =
        memberRemoteDataSource.checkRegisteredMember(ssaid).mapCatching { it.toDomain() }
}
