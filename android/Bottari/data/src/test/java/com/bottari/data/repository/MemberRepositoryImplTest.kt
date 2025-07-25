package com.bottari.data.repository

import com.bottari.data.mapper.MemberMapper.toRequest
import com.bottari.data.source.remote.MemberRemoteDataSource
import com.bottari.domain.model.member.Member
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class MemberRepositoryImplTest {
    private val remoteDataSource = mockk<MemberRemoteDataSource>()
    private val repository = MemberRepositoryImpl(remoteDataSource)

    @DisplayName("회원 등록에 성공하면 true를 반환한다")
    @Test
    fun registerMemberSuccessReturnsTrue() =
        runTest {
            // given
            val member = Member(ssaid = "ssaid123", nickname = "닉네임")
            val request = member.toRequest()

            coEvery { remoteDataSource.registerMember(request) } returns Result.success(Unit)

            // when
            val result = repository.registerMember(member)

            // then
            result.isSuccess shouldBe true
            result.getOrNull() shouldBe true

            coVerify { remoteDataSource.registerMember(request) }
        }

    @DisplayName("회원 등록에 실패하면 실패를 반환한다")
    @Test
    fun registerMemberFailsReturnsException() =
        runTest {
            // given
            val member = Member(ssaid = "ssaid123", nickname = "닉네임")
            val request = member.toRequest()
            val exception = RuntimeException("회원 등록 실패")

            coEvery { remoteDataSource.registerMember(request) } returns Result.failure(exception)

            // when
            val result = repository.registerMember(member)

            // then
            result.isFailure shouldBe true
            result.exceptionOrNull() shouldBe exception

            coVerify { remoteDataSource.registerMember(request) }
        }
}
