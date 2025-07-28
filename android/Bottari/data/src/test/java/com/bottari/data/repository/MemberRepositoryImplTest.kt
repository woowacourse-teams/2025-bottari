package com.bottari.data.repository

import com.bottari.data.mapper.MemberMapper.toRequest
import com.bottari.data.source.remote.MemberRemoteDataSource
import com.bottari.data.testFixture.memberFixture
import com.bottari.domain.repository.MemberRepository
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class MemberRepositoryImplTest {
    private lateinit var remoteDataSource :MemberRemoteDataSource
    private lateinit var repository : MemberRepository

    @BeforeEach
    fun setup() {
        remoteDataSource = mockk()
        repository = MemberRepositoryImpl(remoteDataSource)
    }

    @DisplayName("회원 등록에 성공하면 true를 반환한다")
    @Test
    fun registerMemberSuccessReturnsTrue() =
        runTest {
            // given
            val member = memberFixture()
            val request = member.toRequest()

            coEvery { remoteDataSource.registerMember(request) } returns Result.success(Unit)

            // when
            val result = repository.registerMember(member)

            // then
            result.shouldBeSuccess()

            // verify
            coVerify { remoteDataSource.registerMember(request) }
        }

    @DisplayName("회원 등록에 실패하면 실패를 반환한다")
    @Test
    fun registerMemberFailsReturnsException() =
        runTest {
            // given
            val member = memberFixture()
            val request = member.toRequest()
            val exception = RuntimeException("회원 등록 실패")

            coEvery { remoteDataSource.registerMember(request) } returns Result.failure(exception)

            // when
            val result = repository.registerMember(member)

            // then
            result.shouldBeFailure {
                it shouldBe exception
            }
            // verify
            coVerify { remoteDataSource.registerMember(request) }
        }
}
