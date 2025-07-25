package com.bottari.data

import com.bottari.data.model.template.FetchBottariTemplateResponse
import com.bottari.data.repository.BottariTemplateRepositoryImpl
import com.bottari.data.source.remote.BottariTemplateRemoteDataSource
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class BottariTemplateRepositoryImplTest {
    private val remoteDataSource = mockk<BottariTemplateRemoteDataSource>()
    private val repository = BottariTemplateRepositoryImpl(remoteDataSource)

    @DisplayName("보따리 템플릿 목록 조회에 성공하면 도메인 모델 리스트로 매핑된다")
    @Test
    fun fetchTemplatesSuccessReturnsMappedList() =
        runTest {
            // given
            val response =
                listOf(
                    FetchBottariTemplateResponse(id = 1, title = "template1", items = emptyList(), author = "author"),
                    FetchBottariTemplateResponse(id = 2, title = "template2", items = emptyList(), author = "author"),
                )
            coEvery { remoteDataSource.fetchBottariTemplates(null) } returns Result.success(response)

            // when
            val result = repository.fetchBottariTemplates(null)

            // then
            result.shouldBeSuccess {
                it shouldHaveSize 2
                it[0].title shouldBe "template1"
                it[1].title shouldBe "template2"
            }

            coVerify { remoteDataSource.fetchBottariTemplates(null) }
        }

    @DisplayName("보따리 템플릿 목록 조회에 실패하면 실패를 반환한다")
    @Test
    fun fetchTemplatesFailsReturnsException() =
        runTest {
            // given
            val exception = RuntimeException("Network error")
            coEvery { remoteDataSource.fetchBottariTemplates("검색어") } returns Result.failure(exception)

            // when
            val result = repository.fetchBottariTemplates("검색어")

            // then
            result.isFailure shouldBe true
            result.exceptionOrNull() shouldBe exception

            coVerify { remoteDataSource.fetchBottariTemplates("검색어") }
        }
}
