package com.bottari.data.repository

import com.bottari.data.mapper.MemberMapper.toDomain
import com.bottari.data.model.member.RegisterMemberRequest
import com.bottari.data.model.member.SaveMemberNicknameRequest
import com.bottari.data.source.remote.MemberRemoteDataSource
import com.bottari.domain.model.member.Member
import com.bottari.domain.model.member.RegisteredMember
import com.bottari.domain.repository.MemberRepository

class MemberRepositoryImpl(
    private val memberRemoteDataSource: MemberRemoteDataSource,
) : MemberRepository {
    override suspend fun registerMember(member: Member): Result<Unit> =
        memberRemoteDataSource.registerMember(
            RegisterMemberRequest(member.ssaid),
        )

    override suspend fun saveMemberNickname(member: Member): Result<Unit> =
        memberRemoteDataSource.saveMemberNickname(
            member.ssaid,
            SaveMemberNicknameRequest(member.nickname),
        )

    override suspend fun checkRegisteredMember(ssaid: String): Result<RegisteredMember> =
        memberRemoteDataSource.checkRegisteredMember(ssaid).mapCatching { it.toDomain() }
}
