package com.bottari.data.repository

import com.bottari.data.mapper.MemberMapper.toDomain
import com.bottari.data.mapper.NicknameMapper.toRequest
import com.bottari.data.model.member.RegisterMemberRequest
import com.bottari.data.source.local.MemberIdentifierLocalDataSource
import com.bottari.data.source.remote.MemberRemoteDataSource
import com.bottari.domain.extension.flatMapCatching
import com.bottari.domain.extension.map
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
    override suspend fun registerMember(fcmToken: String): Result<Long> =
        withContext(coroutineDispatcher) {
            memberIdentifierLocalDataSource
                .getInstallationId()
                .mapCatching { installationId ->
                    RegisterMemberRequest(installationId, fcmToken)
                }.flatMapCatching { request ->
                    memberRemoteDataSource.registerMember(request)
                }.flatMapCatching { memberId ->
                    saveMemberIdToLocal(memberId)
                }
        }

    override suspend fun saveMemberNickname(nickname: Nickname): Result<Unit> =
        withContext(coroutineDispatcher) {
            memberRemoteDataSource.saveMemberNickname(nickname.toRequest())
        }

    override suspend fun checkRegisteredMember(): Result<RegisteredMember> =
        memberRemoteDataSource
            .checkRegisteredMember()
            .mapCatching { checkInfo -> checkInfo.toDomain() }
            .flatMapCatching { registeredMember ->
                if (registeredMember.isRegistered.not()) return@flatMapCatching Result.success(registeredMember)
                saveMemberIdToLocal(registeredMember.id).map { registeredMember }
            }

    override suspend fun getInstallationId(): Result<String> =
        withContext(coroutineDispatcher) {
            memberIdentifierLocalDataSource.getInstallationId()
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

    private fun saveMemberIdToLocal(memberId: Long?): Result<Long> =
        runCatching {
            requireNotNull(memberId) { ERROR_MEMBER_ID_NULL }
            memberIdentifierLocalDataSource.saveMemberId(memberId)
            memberId
        }

    companion object {
        private const val ERROR_MEMBER_ID_NULL = "[ERROR] 회원 ID가 null 입니다"
    }
}
