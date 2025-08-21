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
            memberIdentifierLocalDataSource
                .getInstallationId()
                .mapCatching { installationId ->
                    RegisterMemberRequest(installationId, fcmToken)
                }.mapCatching { request ->
                    memberRemoteDataSource
                        .registerMember(request)
                        .getOrThrow()
                        ?.also { memberIdentifierLocalDataSource.saveMemberId(it) }
                }
        }

    override suspend fun saveMemberNickname(nickname: Nickname): Result<Unit> =
        memberRemoteDataSource.saveMemberNickname(nickname.toRequest())

    override suspend fun checkRegisteredMember(): Result<RegisteredMember> =
        withContext(coroutineDispatcher) {
            memberRemoteDataSource
                .checkRegisteredMember()
                .mapCatching { it.toDomain() }
                .onSuccess { registeredMember ->
                    if (registeredMember.isRegistered) {
                        registeredMember.id?.let { memberIdentifierLocalDataSource.saveMemberId(it) }
                    }
                }
        }

    override suspend fun getInstallationId(): Result<String> =
        withContext(coroutineDispatcher) {
            memberIdentifierLocalDataSource.getInstallationId()
        }

    override suspend fun getMemberId(): Result<Long> =
        withContext(coroutineDispatcher) {
            memberIdentifierLocalDataSource.getMemberId()
        }
}
