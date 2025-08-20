package com.bottari.data.repository

import com.bottari.data.mapper.MemberMapper.toDomain
import com.bottari.data.mapper.NicknameMapper.toRequest
import com.bottari.data.model.member.RegisterMemberRequest
import com.bottari.data.source.local.MemberIdentifierLocalDataSource
import com.bottari.data.source.remote.MemberRemoteDataSource
import com.bottari.domain.model.member.Nickname
import com.bottari.domain.model.member.RegisteredMember
import com.bottari.domain.repository.MemberRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MemberRepositoryImpl(
    private val memberRemoteDataSource: MemberRemoteDataSource,
    private val memberIdentifierLocalDataSource: MemberIdentifierLocalDataSource,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : MemberRepository {
    override suspend fun registerMember(fcmToken: String): Result<Long?> =
        withContext(coroutineDispatcher) {
            memberIdentifierLocalDataSource.getMemberIdentifier()
        }.mapCatching { memberId ->
            RegisterMemberRequest(memberId, fcmToken)
        }.mapCatching { registerMemberRequest ->
            memberRemoteDataSource
                .registerMember(registerMemberRequest)
                .getOrThrow()
                ?.also { memberIdentifierLocalDataSource.saveMemberId(it) }
        }

    override suspend fun saveMemberNickname(nickname: Nickname): Result<Unit> =
        memberRemoteDataSource.saveMemberNickname(nickname.toRequest())

    override suspend fun checkRegisteredMember(): Result<RegisteredMember> =
        memberRemoteDataSource
            .checkRegisteredMember()
            .mapCatching { it.toDomain() }
            .onSuccess { registeredMember ->
                if (registeredMember.isRegistered) {
                    registeredMember.id?.let { memberIdentifierLocalDataSource.saveMemberId(it) }
                }
            }

    override suspend fun getMemberIdentifier(): Result<String> =
        withContext(coroutineDispatcher) {
            memberIdentifierLocalDataSource.getMemberIdentifier()
        }

    override suspend fun getMemberId(): Result<Long> = memberIdentifierLocalDataSource.getMemberId()
}
