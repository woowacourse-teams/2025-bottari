package com.bottari.data.repository

import com.bottari.data.mapper.MemberMapper.toDomain
import com.bottari.data.model.member.RegisterMemberRequest
import com.bottari.data.model.member.SaveMemberNicknameRequest
import com.bottari.data.source.local.MemberIdentifierLocalDataSource
import com.bottari.data.source.remote.MemberRemoteDataSource
import com.bottari.domain.model.member.Member
import com.bottari.domain.model.member.RegisteredMember
import com.bottari.domain.repository.MemberRepository

class MemberRepositoryImpl(
    private val memberRemoteDataSource: MemberRemoteDataSource,
    private val memberIdentifierLocalDataSource: MemberIdentifierLocalDataSource,
) : MemberRepository {
    override suspend fun registerMember(): Result<Long?> =
        memberRemoteDataSource
            .registerMember(RegisterMemberRequest(getMemberIdentifier()))

    override suspend fun saveMemberNickname(nickname: String): Result<Unit> =
        memberRemoteDataSource.saveMemberNickname(
            SaveMemberNicknameRequest(nickname),
        )

    override suspend fun checkRegisteredMember(): Result<RegisteredMember> =
        memberRemoteDataSource
            .checkRegisteredMember()
            .mapCatching { it.toDomain() }

    private fun getMemberIdentifier(): String = memberIdentifierLocalDataSource.getMemberIdentifier().getOrThrow()
}
