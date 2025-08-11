package com.bottari.data.repository

import com.bottari.data.model.member.CheckRegisteredMemberResponse
import com.bottari.data.model.member.RegisterMemberRequest
import com.bottari.data.model.member.SaveMemberNicknameRequest
import com.bottari.data.source.local.MemberIdentifierLocalDataSource
import com.bottari.data.source.remote.MemberRemoteDataSource
import com.bottari.domain.repository.MemberRepository
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import retrofit2.HttpException
import retrofit2.Response

class MemberRepositoryImplTest {
    private lateinit var remoteDataSource: MemberRemoteDataSource
    private lateinit var userInfoLocalDataSource: MemberIdentifierLocalDataSource
    private lateinit var repository: MemberRepository
    private val errorResponseBody =
        """{"message":"잘못된 요청입니다."}""".toResponseBody("application/json".toMediaType())

    @BeforeEach
    fun setUp() {
        remoteDataSource = mockk<MemberRemoteDataSource>()
        userInfoLocalDataSource = mockk<MemberIdentifierLocalDataSource>()
        repository = MemberRepositoryImpl(remoteDataSource, userInfoLocalDataSource)
    }

    @DisplayName("회원 등록에 성공하면 Success를 반환한다")
    @Test
    fun registerMemberSuccessReturnsSuccess() =
        runTest {
            // given
            val request = RegisterMemberRequest("ssaid")
            coEvery { remoteDataSource.registerMember(request) } returns Result.success(1)
            coEvery { userInfoLocalDataSource.getMemberIdentifier() } returns Result.success("ssaid")

            // when
            val result = repository.registerMember()

            // then
            result.shouldBeSuccess()

            // verify
            coVerify(exactly = 1) { remoteDataSource.registerMember(request) }
        }

    @DisplayName("회원 등록에 실패하면 Failure를 반환한다")
    @Test
    fun registerMemberFailsReturnsFailure() =
        runTest {
            // given
            val request = RegisterMemberRequest("ssaid")
            val exception = HttpException(Response.error<Unit>(400, errorResponseBody))
            coEvery { remoteDataSource.registerMember(request) } returns Result.failure(exception)
            coEvery { userInfoLocalDataSource.getMemberIdentifier() } returns Result.success("ssaid")

            // when
            val result = repository.registerMember()

            // then
            result.shouldBeFailure { it shouldBe exception }

            // verify
            coVerify(exactly = 1) { remoteDataSource.registerMember(request) }
        }

    @DisplayName("닉네임 갱신에 성공하면 Success를 반환한다")
    @Test
    fun saveMemberNicknameSuccess() =
        runTest {
            // given
            val newNickname = "nickname"
            val request = SaveMemberNicknameRequest(newNickname)
            coEvery {
                remoteDataSource.saveMemberNickname(
                    request,
                )
            } returns Result.success(Unit)

            // when
            val result = repository.saveMemberNickname(newNickname)

            // then
            result.shouldBeSuccess()

            // verify
            coVerify(exactly = 1) { remoteDataSource.saveMemberNickname(request) }
        }

    @DisplayName("닉네임 갱신에 실패하면 Failure를 반환한다")
    @Test
    fun saveMemberNicknameFailsReturnsFailure() =
        runTest {
            // given
            val newNickname = "nickname"
            val request = SaveMemberNicknameRequest(newNickname)
            val httpException = HttpException(Response.error<Unit>(400, errorResponseBody))
            coEvery {
                remoteDataSource.saveMemberNickname(
                    request,
                )
            } returns Result.failure(httpException)

            // when
            val result = repository.saveMemberNickname(newNickname)

            // then
            result.shouldBeFailure { it shouldBe httpException }

            // verify
            coVerify(exactly = 1) { remoteDataSource.saveMemberNickname(request) }
        }

    @DisplayName("회원가입된 상태에서 회원가입 여부 확인에 성공하면 Success를 반환한다")
    @Test
    fun checkRegisteredMemberSuccess() =
        runTest {
            // given
            val response = CheckRegisteredMemberResponse(true, 1, "test")
            coEvery { remoteDataSource.checkRegisteredMember() } returns
                Result.success(
                    response,
                )

            // when
            val result = repository.checkRegisteredMember()

            // then
            assertSoftly(result) {
                shouldBeSuccess()
                getOrThrow().isRegistered shouldBe true
                getOrThrow().id shouldBe 1
                getOrThrow().name shouldBe "test"
            }

            // verify
            coVerify(exactly = 1) { remoteDataSource.checkRegisteredMember() }
        }

    @DisplayName("회원가입이 되지 않은 상태에서 회원가입 여부 확인에 성공하면 Success를 반환한다")
    @Test
    fun checkRegisteredMemberFailsReturnsFailure() =
        runTest {
            // given
            val response = CheckRegisteredMemberResponse(false, 1, "test")
            coEvery { remoteDataSource.checkRegisteredMember() } returns
                Result.success(
                    response,
                )

            // when
            val result = repository.checkRegisteredMember()

            // then
            assertSoftly(result) {
                shouldBeSuccess()
                getOrThrow().isRegistered shouldBe false
                getOrThrow().id shouldBe 1
                getOrThrow().name shouldBe "test"
            }
        }
}
