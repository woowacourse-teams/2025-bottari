package com.bottari.data.repository

import com.bottari.data.model.remote.bottari.template.BottariTemplateCreateRequest
import com.bottari.data.model.remote.bottari.template.BottariTemplateFetchResponse
import com.bottari.data.source.remote.BottariTemplateRemoteDataSource
import com.bottari.data.testFixture.fetchBottariTemplateResponseListFixture
import com.bottari.domain.model.bottari.template.BottariTemplate
import com.bottari.domain.model.exception.BottariResult
import com.bottari.domain.repository.BottariTemplateRepository
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
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

    private val title = "title"
    private val items = listOf("item1", "item2")

    private fun createRequestMatcher(): (BottariTemplateCreateRequest) -> Boolean =
        { it.title == title && it.bottariTemplateItems == items }

    private fun successResponse() = fetchBottariTemplateResponseListFixture()

    @DisplayName("보따리 템플릿 목록 조회에 성공하면 도메인 모델 리스트로 매핑된다")
    @Test
    fun fetchTemplatesSuccessReturnsMappedList() =
        runTest {
            // given
            coEvery { remoteDataSource.fetchBottariTemplates(null) } returns
                BottariResult.Success(
                    successResponse(),
                )

            // when
            val result = repository.fetchBottariTemplates(null)

            // then
            result.shouldBeInstanceOf<BottariResult.Success<List<BottariTemplate>>> { success ->
                success.data shouldHaveSize 2
                success.data[0].title shouldBe "template1"
                success.data[1].title shouldBe "template2"
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
            coEvery { remoteDataSource.fetchBottariTemplates("검색어") } returns
                BottariResult.NetworkError(
                    exception,
                )

            // when
            val result = repository.fetchBottariTemplates("검색어")

            // then
            result.shouldBeInstanceOf<BottariResult.NetworkError<Throwable>> { it.throwable shouldBe exception }

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
                remoteDataSource.createBottariTemplate(match(createRequestMatcher()))
            } returns BottariResult.Success(expectedId)

            // when
            val result = repository.createBottariTemplate(title, items)

            // then
            result.shouldBeInstanceOf<BottariResult.Success<Long>> { success ->
                success.data shouldBe expectedId
            }

            // verify
            coVerify {
                remoteDataSource.createBottariTemplate(match(createRequestMatcher()))
            }
        }

    @DisplayName("보따리 템플릿 생성에 실패하면 실패를 반환한다")
    @Test
    fun createTemplateFailsReturnsException() =
        runTest {
            // given
            val exception = IllegalStateException("Creation failed")
            coEvery {
                remoteDataSource.createBottariTemplate(match(createRequestMatcher()))
            } returns BottariResult.NetworkError(exception)

            // when
            val result = repository.createBottariTemplate(title, items)

            // then
            result.shouldBeInstanceOf<BottariResult.NetworkError<Throwable>> { failure -> failure.throwable shouldBe exception }

            // verify
            coVerify {
                remoteDataSource.createBottariTemplate(match(createRequestMatcher()))
            }
        }

    @DisplayName("특정 보따리 템플릿 조회에 성공하면 도메인 모델로 매핑된다")
    @Test
    fun fetchTemplateDetailSuccessReturnsMappedDomain() =
        runTest {
            // given
            val bottariId = 100L
            val response = fetchBottariTemplateResponseListFixture().first()
            coEvery { remoteDataSource.fetchBottariTemplateDetail(bottariId) } returns
                BottariResult.Success(response)

            // when
            val result = repository.fetchBottariTemplate(bottariId)

            // then
            result.shouldBeInstanceOf<BottariResult.Success<BottariTemplate>> { success ->
                success.data.title shouldBe "template1"
            }

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
            coEvery { remoteDataSource.fetchBottariTemplateDetail(bottariId) } returns
                BottariResult.NetworkError(exception)

            // when
            val result = repository.fetchBottariTemplate(bottariId)

            // then
            result.shouldBeInstanceOf<BottariResult.NetworkError<Throwable>> { failure -> failure.throwable shouldBe exception }

            // verify
            coVerify { remoteDataSource.fetchBottariTemplateDetail(bottariId) }
        }

    @DisplayName("보따리 템플릿 가져오기에 성공하면 새로운 보따리 아이디를 반환한다")
    @Test
    fun getBottariTemplateSuccessReturnsMappedDetail() =
        runTest {
            // given
            val bottariTemplateId = 1L
            coEvery { remoteDataSource.takeBottariTemplate(bottariTemplateId) } returns
                BottariResult.Success(bottariTemplateId)

            // when
            val result = repository.takeBottariTemplate(bottariTemplateId)

            // then
            result.shouldBeInstanceOf<BottariResult.Success<Long>> { success ->
                success.data shouldBe bottariTemplateId
            }

            // verify
            coVerify { remoteDataSource.takeBottariTemplate(bottariTemplateId) }
        }

    @DisplayName("내 보따리 템플릿 목록 조회에 성공하면 도메인 모델 리스트로 매핑된다")
    @Test
    fun fetchMyTemplatesSuccessReturnsMappedList() =
        runTest {
            // given
            val successResponse =
                listOf(
                    BottariTemplateFetchResponse(
                        author = "다이스",
                        id = 1L,
                        items = listOf(),
                        title = "template1",
                        createdAt = "12:00",
                        takenCount = 3,
                    ),
                    BottariTemplateFetchResponse(
                        author = "다이스",
                        id = 2L,
                        items = listOf(),
                        title = "template2",
                        createdAt = "10:00",
                        takenCount = 4,
                    ),
                )
            coEvery { remoteDataSource.fetchMyBottariTemplates() } returns
                BottariResult.Success(successResponse)

            // when
            val result = repository.fetchMyBottariTemplates()

            // then
            result.shouldBeInstanceOf<BottariResult.Success<List<BottariTemplate>>> { success ->
                success.data shouldHaveSize 2
                success.data[0].title shouldBe "template1"
                success.data[1].title shouldBe "template2"
            }

            // verify
            coVerify { remoteDataSource.fetchMyBottariTemplates() }
        }

    @DisplayName("내 보따리 템플릿 목록 조회에 실패하면 실패를 반환한다")
    @Test
    fun fetchMyTemplatesFailsReturnsException() =
        runTest {
            // given
            val exception = Exception("Unknown error")
            coEvery { remoteDataSource.fetchMyBottariTemplates() } returns
                BottariResult.NetworkError(
                    exception,
                )

            // when
            val result = repository.fetchMyBottariTemplates()

            // then
            result.shouldBeInstanceOf<BottariResult.NetworkError<Throwable>> { failure -> failure.throwable shouldBe exception }

            // verify
            coVerify { remoteDataSource.fetchMyBottariTemplates() }
        }

    @DisplayName("내 보따리 템플릿 삭제에 성공하면 성공 결과를 반환한다")
    @Test
    fun deleteTemplateSuccessReturnsUnit() =
        runTest {
            // given
            val templateId = 99L
            coEvery { remoteDataSource.deleteMyBottariTemplate(templateId) } returns
                BottariResult.Success(Unit)

            // when
            val result = repository.deleteMyBottariTemplate(templateId)

            // then
            result.shouldBeInstanceOf<BottariResult.Success<Unit>>()

            // verify
            coVerify { remoteDataSource.deleteMyBottariTemplate(templateId) }
        }

    @DisplayName("내 보따리 템플릿 삭제에 실패하면 실패 결과를 반환한다")
    @Test
    fun deleteTemplateFailsReturnsException() =
        runTest {
            // given
            val templateId = 99L
            val exception = RuntimeException("Delete failed")
            coEvery { remoteDataSource.deleteMyBottariTemplate(templateId) } returns
                BottariResult.NetworkError(exception)

            // when
            val result = repository.deleteMyBottariTemplate(templateId)

            // then
            result.shouldBeInstanceOf<BottariResult.NetworkError<Throwable>> { failure -> failure.throwable shouldBe exception }

            // verify
            coVerify { remoteDataSource.deleteMyBottariTemplate(templateId) }
        }
}
