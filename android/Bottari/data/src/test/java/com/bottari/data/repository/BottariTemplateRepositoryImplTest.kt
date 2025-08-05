package com.bottari.data.repository

import com.bottari.data.model.template.CreateBottariTemplateRequest
import com.bottari.data.model.template.FetchMyBottariTemplatesResponse
import com.bottari.data.source.remote.BottariTemplateRemoteDataSource
import com.bottari.data.testFixture.fetchBottariTemplateResponseListFixture
import com.bottari.domain.repository.BottariTemplateRepository
import io.kotest.matchers.collections.shouldHaveSize
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

class BottariTemplateRepositoryImplTest {
    private lateinit var remoteDataSource: BottariTemplateRemoteDataSource
    private lateinit var repository: BottariTemplateRepository

    @BeforeEach
    fun setup() {
        remoteDataSource = mockk()
        repository = BottariTemplateRepositoryImpl(remoteDataSource)
    }

    private val ssaid = "ssaid"
    private val title = "title"
    private val items = listOf("item1", "item2")

    private fun createRequestMatcher(): (CreateBottariTemplateRequest) -> Boolean =
        { it.title == title && it.bottariTemplateItems == items }

    private fun successResponse() = fetchBottariTemplateResponseListFixture()

    @DisplayName("보따리 템플릿 목록 조회에 성공하면 도메인 모델 리스트로 매핑된다")
    @Test
    fun fetchTemplatesSuccessReturnsMappedList() =
        runTest {
            // given
            coEvery { remoteDataSource.fetchBottariTemplates(null) } returns Result.success(successResponse())

            // when
            val result = repository.fetchBottariTemplates(null)

            // then
            result.shouldBeSuccess {
                it shouldHaveSize 2
                it[0].title shouldBe "template1"
                it[1].title shouldBe "template2"
            }

            // verify
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
            result.shouldBeFailure { it shouldBe exception }

            // verify
            coVerify { remoteDataSource.fetchBottariTemplates("검색어") }
        }

    @DisplayName("보따리 템플릿 생성에 성공하면 템플릿 ID를 반환한다")
    @Test
    fun createTemplateSuccessReturnsTemplateId() =
        runTest {
            // given
            val expectedId = 123L
            coEvery {
                remoteDataSource.createBottariTemplate(ssaid, match(createRequestMatcher()))
            } returns Result.success(expectedId)

            // when
            val result = repository.createBottariTemplate(ssaid, title, items)

            // then
            result.shouldBeSuccess { it shouldBe expectedId }

            // verify
            coVerify {
                remoteDataSource.createBottariTemplate(ssaid, match(createRequestMatcher()))
            }
        }

    @DisplayName("보따리 템플릿 생성에 실패하면 실패를 반환한다")
    @Test
    fun createTemplateFailsReturnsException() =
        runTest {
            // given
            val exception = IllegalStateException("Creation failed")
            coEvery {
                remoteDataSource.createBottariTemplate(ssaid, match(createRequestMatcher()))
            } returns Result.failure(exception)

            // when
            val result = repository.createBottariTemplate(ssaid, title, items)

            // then
            result.shouldBeFailure { it shouldBe exception }

            // verify
            coVerify {
                remoteDataSource.createBottariTemplate(ssaid, match(createRequestMatcher()))
            }
        }

    @DisplayName("특정 보따리 템플릿 조회에 성공하면 도메인 모델로 매핑된다")
    @Test
    fun fetchTemplateDetailSuccessReturnsMappedDomain() =
        runTest {
            // given
            val bottariId = 100L
            val response = fetchBottariTemplateResponseListFixture().first()
            coEvery { remoteDataSource.fetchBottariTemplateDetail(bottariId) } returns Result.success(response)

            // when
            val result = repository.fetchBottariTemplate(bottariId)

            // then
            result.shouldBeSuccess { it.title shouldBe "template1" }

            // verify
            coVerify { remoteDataSource.fetchBottariTemplateDetail(bottariId) }
        }

    @DisplayName("특정 보따리 템플릿 조회에 실패하면 실패를 반환한다")
    @Test
    fun fetchTemplateDetailFailsReturnsException() =
        runTest {
            // given
            val bottariId = 100L
            val exception = IllegalArgumentException("Template not found")
            coEvery { remoteDataSource.fetchBottariTemplateDetail(bottariId) } returns Result.failure(exception)

            // when
            val result = repository.fetchBottariTemplate(bottariId)

            // then
            result.shouldBeFailure { it shouldBe exception }

            // verify
            coVerify { remoteDataSource.fetchBottariTemplateDetail(bottariId) }
        }

    @DisplayName("보따리 템플릿 가져오기에 성공하면 새로운 보따리 아이디를 반환한다")
    @Test
    fun getBottariTemplateSuccessReturnsMappedDetail() =
        runTest {
            // given
            val bottariTemplateId = 1L
            coEvery { remoteDataSource.takeBottariTemplate(ssaid, bottariTemplateId) } returns Result.success(bottariTemplateId)

            // when
            val result = repository.takeBottariTemplate(ssaid, bottariTemplateId)

            // then
            result.shouldBeSuccess { it shouldBe bottariTemplateId }

            // verify
            coVerify { remoteDataSource.takeBottariTemplate(ssaid, bottariTemplateId) }
        }

    @DisplayName("내 보따리 템플릿 목록 조회에 성공하면 도메인 모델 리스트로 매핑된다")
    @Test
    fun fetchMyTemplatesSuccessReturnsMappedList() =
        runTest {
            // given
            val successResponse =
                listOf(
                    FetchMyBottariTemplatesResponse("다이스", 1L, listOf(), "template1", "12:00", 3),
                    FetchMyBottariTemplatesResponse("다이스", 2L, listOf(), "template2", "12:00", 4),
                )
            coEvery { remoteDataSource.fetchMyBottariTemplates(ssaid) } returns Result.success(successResponse)

            // when
            val result = repository.fetchMyBottariTemplates(ssaid)

            // then
            result.shouldBeSuccess {
                it shouldHaveSize 2
                it[0].title shouldBe "template1"
                it[1].title shouldBe "template2"
            }

            // verify
            coVerify { remoteDataSource.fetchMyBottariTemplates(ssaid) }
        }

    @DisplayName("내 보따리 템플릿 목록 조회에 실패하면 실패를 반환한다")
    @Test
    fun fetchMyTemplatesFailsReturnsException() =
        runTest {
            // given
            val exception = Exception("Unknown error")
            coEvery { remoteDataSource.fetchMyBottariTemplates(ssaid) } returns Result.failure(exception)

            // when
            val result = repository.fetchMyBottariTemplates(ssaid)

            // then
            result.shouldBeFailure { it shouldBe exception }

            // verify
            coVerify { remoteDataSource.fetchMyBottariTemplates(ssaid) }
        }

    @DisplayName("내 보따리 템플릿 삭제에 성공하면 성공 결과를 반환한다")
    @Test
    fun deleteTemplateSuccessReturnsUnit() =
        runTest {
            // given
            val templateId = 99L
            coEvery { remoteDataSource.deleteMyBottariTemplate(ssaid, templateId) } returns Result.success(Unit)

            // when
            val result = repository.deleteMyBottariTemplate(ssaid, templateId)

            // then
            result.shouldBeSuccess { it shouldBe Unit }

            // verify
            coVerify { remoteDataSource.deleteMyBottariTemplate(ssaid, templateId) }
        }

    @DisplayName("내 보따리 템플릿 삭제에 실패하면 실패 결과를 반환한다")
    @Test
    fun deleteTemplateFailsReturnsException() =
        runTest {
            // given
            val templateId = 99L
            val exception = RuntimeException("Delete failed")
            coEvery { remoteDataSource.deleteMyBottariTemplate(ssaid, templateId) } returns Result.failure(exception)

            // when
            val result = repository.deleteMyBottariTemplate(ssaid, templateId)

            // then
            result.shouldBeFailure { it shouldBe exception }

            // verify
            coVerify { remoteDataSource.deleteMyBottariTemplate(ssaid, templateId) }
        }
}
