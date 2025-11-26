package com.bottari.core.data.repository

import com.bottari.core.data.source.local.MemberIdentifierLocalDataSource
import com.bottari.core.data.source.remote.MemberRemoteDataSource
import com.bottari.core.domain.extension.flatMapCatching
import com.bottari.core.domain.model.member.Nickname
import com.bottari.core.domain.model.member.RegisteredMember
import com.bottari.core.domain.repository.MemberRepository
import com.bottari.core.network.client.interceptor.FirebaseInstallationIdProvider
import com.bottari.core.network.dto.member.MemberNicknameSaveRequest
import com.bottari.core.network.dto.member.MemberRegisterRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MemberRepositoryImpl @Inject constructor(
    private val installationIdProvider: FirebaseInstallationIdProvider,
    private val memberRemoteDataSource: MemberRemoteDataSource,
    private val memberIdentifierLocalDataSource: MemberIdentifierLocalDataSource,
    private val coroutineDispatcher: CoroutineDispatcher,
) : MemberRepository {
    override suspend fun registerMember(fcmToken: String): Result<Long> =
        withContext(coroutineDispatcher) {
            installationIdProvider
                .getInstallationId()
                .mapCatching { installationId ->
                    MemberRegisterRequest(installationId, fcmToken)
                }.flatMapCatching { request ->
                    memberRemoteDataSource.registerMember(request)
                }.flatMapCatching { memberId ->
                    saveMemberIdToLocal(memberId)
                }
        }

    override suspend fun saveMemberNickname(nickname: Nickname): Result<Unit> =
        withContext(coroutineDispatcher) {
            memberRemoteDataSource.saveMemberNickname(MemberNicknameSaveRequest.fromDomain(nickname))
        }

    override suspend fun checkRegisteredMember(): Result<RegisteredMember> =
        memberRemoteDataSource
            .checkRegisteredMember()
            .mapCatching { checkInfo -> checkInfo.toDomain() }
            .flatMapCatching { registeredMember ->
                if (registeredMember.isRegistered.not()) {
                    return@flatMapCatching Result.success(
                        registeredMember,
                    )
                }
                saveMemberIdToLocal(registeredMember.id).map { registeredMember }
            }

    override suspend fun getMemberId(): Result<Long> =
        memberIdentifierLocalDataSource
            .getMemberId()
            .recoverCatching {
                syncMemberIdFromRemote().getOrThrow()
            }

    private suspend fun syncMemberIdFromRemote(): Result<Long> =
        memberRemoteDataSource
            .checkRegisteredMember()
            .flatMapCatching { checkInfo -> saveMemberIdToLocal(checkInfo.id) }

    private suspend fun saveMemberIdToLocal(memberId: Long?): Result<Long> =
        runCatching {
            requireNotNull(memberId) { ERROR_MEMBER_ID_NULL }
            memberIdentifierLocalDataSource
                .saveMemberId(memberId)
                .map { memberId }
                .getOrThrow()
        }

    companion object {
        private const val ERROR_MEMBER_ID_NULL = "[ERROR] 회원 ID가 null 입니다"
    }
}
