package com.bottari.data.repository

import com.bottari.data.model.remote.member.MemberNicknameSaveRequest
import com.bottari.data.model.remote.member.MemberRegisterRequest
import com.bottari.data.source.local.MemberIdentifierLocalDataSource
import com.bottari.data.source.remote.MemberRemoteDataSource
import com.bottari.domain.extension.map
import com.bottari.domain.extension.mapCatching
import com.bottari.domain.model.exception.BottariResult
import com.bottari.domain.model.exception.getOrConvert
import com.bottari.domain.model.exception.getOrThrow
import com.bottari.domain.model.exception.mapCatching
import com.bottari.domain.model.exception.onSuccess
import com.bottari.domain.model.exception.toBottariResult
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
    override suspend fun registerMember(fcmToken: String): BottariResult<Long> =
        withContext(coroutineDispatcher) {
            memberIdentifierLocalDataSource
                .getInstallationId()
                .mapCatching {
                    MemberRegisterRequest(it, fcmToken)
                }.mapCatching { request ->
                    memberRemoteDataSource.registerMember(request)
                }.getOrConvert()
                .onSuccess { saveMemberIdToLocal(it) }
        }

    override suspend fun saveMemberNickname(nickname: Nickname): BottariResult<Unit> =
        withContext(coroutineDispatcher) {
            memberRemoteDataSource.saveMemberNickname(MemberNicknameSaveRequest.fromDomain(nickname))
        }

    override suspend fun checkRegisteredMember(): BottariResult<RegisteredMember> =
        memberRemoteDataSource
            .checkRegisteredMember()
            .mapCatching { checkInfo -> checkInfo.toDomain() }
            .mapCatching { registeredMember ->
                if (registeredMember.isRegistered.not()) {
                    return@mapCatching registeredMember
                }
                saveMemberIdToLocal(registeredMember.id).getOrThrow()
                registeredMember
            }

    override suspend fun getInstallationId(): BottariResult<String> =
        withContext(coroutineDispatcher) {
            memberIdentifierLocalDataSource.getInstallationId().toBottariResult()
        }

    override suspend fun getMemberId(): BottariResult<Long> =
        memberIdentifierLocalDataSource
            .getMemberId()
            .recoverCatching {
                syncMemberIdFromRemote().getOrThrow()
            }.toBottariResult()

    private suspend fun syncMemberIdFromRemote(): BottariResult<Long> =
        memberRemoteDataSource
            .checkRegisteredMember()
            .mapCatching { checkInfo ->
                saveMemberIdToLocal(checkInfo.id).getOrThrow()
            }

    private suspend fun saveMemberIdToLocal(memberId: Long?): BottariResult<Long> =
        runCatching {
            requireNotNull(memberId) { ERROR_MEMBER_ID_NULL }
            memberIdentifierLocalDataSource
                .saveMemberId(memberId)
                .map { memberId }
                .getOrThrow()
        }.toBottariResult()

    companion object {
        private const val ERROR_MEMBER_ID_NULL = "[ERROR] 회원 ID가 null 입니다"
    }
}
