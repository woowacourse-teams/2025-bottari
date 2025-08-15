package com.bottari.data.repository

import com.bottari.data.model.fcm.SaveFcmTokenRequest
import com.bottari.data.source.remote.FcmRemoteDataSource
import com.bottari.domain.repository.FcmRepository
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

class FcmRepositoryTest {
    private lateinit var dataSource: FcmRemoteDataSource
    private lateinit var repository: FcmRepository
    private val errorResponseBody =
        """{"message":"FCM 토큰 정보가 존재하지 않습니다."}""".toResponseBody("application/json".toMediaType())

    @BeforeEach
    fun setUp() {
        dataSource = mockk<FcmRemoteDataSource>()
        repository = FcmRepositoryImpl(dataSource)
    }

    @DisplayName("FCM 토큰 저장에 성공하는 경우 Success를 반환한다")
    @Test
    fun saveFcmTokenReturnsSuccessTest() =
        runTest {
            // given
            val token = "token"
            val request = SaveFcmTokenRequest(token)
            coEvery { dataSource.saveFcmToken(request) } returns Result.success(Unit)

            // when
            val result = repository.saveFcmToken(token)

            // then
            result.shouldBeSuccess()

            // verify
            coVerify(exactly = 1) { dataSource.saveFcmToken(request) }
        }

    @DisplayName("FCM 토큰 저장에 실패하는 경우 Failure를 반환한다")
    @Test
    fun saveFcmTokenReturnsFailureTest() =
        runTest {
            // given
            val token = "token"
            val request = SaveFcmTokenRequest(token)
            val exception = HttpException(Response.error<Unit>(404, errorResponseBody))
            coEvery { dataSource.saveFcmToken(request) } returns Result.failure(exception)

            // when
            val result = repository.saveFcmToken(token)

            // then
            result.shouldBeFailure { error -> error shouldBe exception }
        }
}
