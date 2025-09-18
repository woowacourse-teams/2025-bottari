package com.bottari.data.repository

import com.bottari.data.model.remote.fcm.FcmTokenSaveRequest
import com.bottari.data.source.local.MemberIdentifierLocalDataSource
import com.bottari.data.source.remote.FcmRemoteDataSource
import com.bottari.domain.model.exception.BottariResult
import com.bottari.domain.repository.FcmRepository
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
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
    private lateinit var localDataSource: MemberIdentifierLocalDataSource
    private lateinit var repository: FcmRepository
    private val errorResponseBody =
        """{"message":"FCM 토큰 정보가 존재하지 않습니다."}""".toResponseBody("application/json".toMediaType())

    @BeforeEach
    fun setUp() {
        dataSource = mockk<FcmRemoteDataSource>()
        localDataSource = mockk<MemberIdentifierLocalDataSource>()
        repository = FcmRepositoryImpl(dataSource, localDataSource)
        coEvery { localDataSource.getMemberId() } returns Result.success(1)
    }

    @DisplayName("FCM 토큰 저장에 성공하는 경우 Success를 반환한다")
    @Test
    fun saveFcmTokenReturnsSuccessTest() =
        runTest {
            // given
            val token = "token"
            val request = FcmTokenSaveRequest(token)
            coEvery { dataSource.saveFcmToken(request) } returns BottariResult.Success(Unit)

            // when
            val result = repository.saveFcmToken(token)

            // then
            result.shouldBeInstanceOf<BottariResult.Success<Unit>>()

            // verify
            coVerify(exactly = 1) { dataSource.saveFcmToken(request) }
        }

    @DisplayName("FCM 토큰 저장에 실패하는 경우 Failure를 반환한다")
    @Test
    fun saveFcmTokenReturnsFailureTest() =
        runTest {
            // given
            val token = "token"
            val request = FcmTokenSaveRequest(token)
            val exception = HttpException(Response.error<Unit>(404, errorResponseBody))
            coEvery { dataSource.saveFcmToken(request) } returns
                BottariResult.NetworkError(
                    exception,
                )

            // when
            val result = repository.saveFcmToken(token)

            // then
            result.shouldBeInstanceOf<BottariResult.NetworkError<Throwable>> { failure -> failure.throwable shouldBe exception }
        }
}
