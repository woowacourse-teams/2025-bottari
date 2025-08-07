package com.bottari.data.repository

import com.bottari.data.mapper.MemberMapper.toDomain
import com.bottari.data.model.member.RegisterMemberRequest
import com.bottari.data.model.member.SaveMemberNicknameRequest
import com.bottari.data.source.local.UserInfoLocalDataSource
import com.bottari.data.source.remote.MemberRemoteDataSource
import com.bottari.domain.model.member.Member
import com.bottari.domain.model.member.RegisteredMember
import com.bottari.domain.repository.MemberRepository
import com.bottari.logger.BottariLogger

class MemberRepositoryImpl(
    private val memberRemoteDataSource: MemberRemoteDataSource,
    private val userInfoLocalDataSource: UserInfoLocalDataSource,
) : MemberRepository {
    override suspend fun registerMember(ssaid: String): Result<Long?> =
        memberRemoteDataSource
            .registerMember(RegisterMemberRequest(ssaid))
            .onSuccess { safeSaveUserId(it.toString()) }

    override suspend fun saveMemberNickname(member: Member): Result<Unit> =
        memberRemoteDataSource.saveMemberNickname(
            member.ssaid,
            SaveMemberNicknameRequest(member.nickname),
        )

    override suspend fun checkRegisteredMember(ssaid: String): Result<RegisteredMember> =
        memberRemoteDataSource
            .checkRegisteredMember(ssaid)
            .mapCatching { it.toDomain() }
            .onSuccess { safeSaveUserId(it.id.toString()) }

    private suspend fun safeSaveUserId(userId: String) {
        runCatching { userInfoLocalDataSource.saveUserId(userId) }
            .onFailure { BottariLogger.error(ERROR_SAVE_USER_ID_MESSAGE, it) }
    }

    companion object {
        private const val ERROR_SAVE_USER_ID_MESSAGE = "[ERROR] UserId 저장 실패"
    }
}
