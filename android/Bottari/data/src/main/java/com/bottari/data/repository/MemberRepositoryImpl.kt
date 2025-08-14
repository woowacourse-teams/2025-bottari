package com.bottari.data.repository

import com.bottari.data.mapper.MemberMapper.toDomain
import com.bottari.data.mapper.NicknameMapper.toRequest
import com.bottari.data.model.member.RegisterMemberRequest
import com.bottari.data.source.local.MemberIdentifierLocalDataSource
import com.bottari.data.source.remote.MemberRemoteDataSource
import com.bottari.domain.model.member.Nickname
import com.bottari.domain.model.member.RegisteredMember
import com.bottari.domain.repository.MemberRepository

class MemberRepositoryImpl(
    private val memberRemoteDataSource: MemberRemoteDataSource,
    private val memberIdentifierLocalDataSource: MemberIdentifierLocalDataSource,
) : MemberRepository {
    override suspend fun registerMember(fcmToken: String): Result<Long?> =
        memberRemoteDataSource
            .registerMember(RegisterMemberRequest(getMemberIdentifier(), fcmToken))

    override suspend fun saveMemberNickname(nickname: Nickname): Result<Unit> =
        memberRemoteDataSource.saveMemberNickname(nickname.toRequest())

    override suspend fun checkRegisteredMember(): Result<RegisteredMember> =
        memberRemoteDataSource
            .checkRegisteredMember()
            .mapCatching { it.toDomain() }

    private fun getMemberIdentifier(): String = memberIdentifierLocalDataSource.getMemberIdentifier().getOrThrow()
}
