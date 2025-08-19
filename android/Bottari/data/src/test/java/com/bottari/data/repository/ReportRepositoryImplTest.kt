package com.bottari.data.repository

import com.bottari.data.model.report.ReportTemplateRequest
import com.bottari.data.source.remote.ReportRemoteDataSource
import com.bottari.domain.repository.ReportRepository
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import retrofit2.HttpException
import retrofit2.Response

class ReportRepositoryImplTest {
    private lateinit var remoteDataSource: ReportRemoteDataSource
    private lateinit var repository: ReportRepository

    @BeforeEach
    fun setUp() {
        remoteDataSource = mockk()
        repository = ReportRepositoryImpl(remoteDataSource)
    }

    @DisplayName("신고에 성공하면 Success를 반환한다")
    @Test
    fun reportTemplateSuccessReturnsSuccess() =
        runTest {
            // given
            val templateId = 1L
            val reason = "reason"
            val request = ReportTemplateRequest("reason")

            coEvery {
                remoteDataSource.reportTemplate(templateId, request)
            } returns Result.success(Unit)

            // when
            val result = repository.reportTemplate(templateId, reason)

            // then
            result.shouldBeSuccess()

            // verify
            coVerify(exactly = 1) { remoteDataSource.reportTemplate(templateId, request) }
        }

    @DisplayName("이미 신고한 템플릿을 신고하면 HttpException으로 Failure를 반환한다")
    @Test
    fun reportTemplateAlreadyReportedReturnsFailure() =
        runTest {
            // given
            val templateId = 1L
            val reason = "reason"
            val request = ReportTemplateRequest(reason)

            val errorResponse =
                Response.error<Unit>(
                    400,
                    "{\"message\":\"이미 신고한 템플릿\"}".toResponseBody("application/json".toMediaTypeOrNull()),
                )
            val httpException = HttpException(errorResponse)

            coEvery {
                remoteDataSource.reportTemplate(templateId, request)
            } returns Result.failure(httpException)

            // when
            val result = repository.reportTemplate(templateId, reason)

            // then
            result.shouldBeFailure<HttpException>()

            // verify
            coVerify(exactly = 1) { remoteDataSource.reportTemplate(templateId, request) }
        }
}
