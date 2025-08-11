package com.bottari.data.repository

import com.bottari.data.model.bottari.CreateBottariRequest
import com.bottari.data.model.bottari.UpdateBottariTitleRequest
import com.bottari.data.source.remote.BottariRemoteDataSource
import com.bottari.data.testFixture.bottariResponseFixture
import com.bottari.data.testFixture.fetchBottariesResponseFixture
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

class BottariRepositoryImplTest {
    private lateinit var remoteDataSource: BottariRemoteDataSource
    private lateinit var repository: BottariRepositoryImpl

    @BeforeEach
    fun setup() {
        remoteDataSource = mockk()
        repository = BottariRepositoryImpl(remoteDataSource)
    }

    @DisplayName("보따리 목록 조회에 성공하면 도메인 모델 리스트로 매핑된다")
    @Test
    fun fetchBottariesSuccessReturnsMappedList() =
        runTest {
            // given
            val response = fetchBottariesResponseFixture()
            coEvery { remoteDataSource.fetchBottaries() } returns Result.success(response)

            // when
            val result = repository.fetchBottaries()

            // then
            result.shouldBeSuccess {
                it shouldHaveSize 2
                it[0].title shouldBe "title1"
                it[1].title shouldBe "title2"
            }

            // verify
            coVerify { remoteDataSource.fetchBottaries() }
        }

    @DisplayName("보따리 단건 조회에 성공하면 도메인 모델로 매핑된다")
    @Test
    fun fetchBottariDetailSuccessReturnsMappedDetail() =
        runTest {
            // given
            val bottariId = 100L
            val detailResponse = bottariResponseFixture()
            coEvery { remoteDataSource.fetchBottariDetail(bottariId) } returns
                Result.success(
                    detailResponse,
                )

            // when
            val result = repository.fetchBottariDetail(bottariId)

            // then
            result.shouldBeSuccess {
                it.title shouldBe "detail"
            }

            // verify
            coVerify { remoteDataSource.fetchBottariDetail(bottariId) }
        }

    @DisplayName("보따리 생성을 성공하면 ID를 반환한다")
    @Test
    fun createBottariSuccessReturnsId() =
        runTest {
            // given
            val title = "new bottari"
            val expectedId = 42L
            coEvery {
                remoteDataSource.createBottari(CreateBottariRequest(title))
            } returns Result.success(expectedId)

            // when
            val result = repository.createBottari(title)

            // then
            result.shouldBeSuccess {
                it shouldBe expectedId
            }

            // verify
            coVerify { remoteDataSource.createBottari(CreateBottariRequest(title)) }
        }

    @DisplayName("보따리 생성 성공 시 Unit을 반환한다")
    @Test
    fun deleteBottariSuccessReturnsUnit() =
        runTest {
            // given
            val id = 1L
            coEvery {
                remoteDataSource.deleteBottari(id)
            } returns Result.success(Unit)

            // when
            val result = repository.deleteBottari(id)

            // then
            result.shouldBeSuccess()

            // verify
            coVerify { remoteDataSource.deleteBottari(id) }
        }

    @DisplayName("보따리 목록 조회 실패 시 예외를 반환한다")
    @Test
    fun fetchBottariesFailureReturnsException() =
        runTest {
            // given
            val exception = RuntimeException("불러오기 실패")
            coEvery { remoteDataSource.fetchBottaries() } returns Result.failure(exception)

            // when
            val result = repository.fetchBottaries()

            // then
            result.shouldBeFailure {
                it shouldBe exception
            }

            // verify
            coVerify { remoteDataSource.fetchBottaries() }
        }

    @DisplayName("보따리 이름 변경에 성공하면 Unit을 반환한다")
    @Test
    fun saveBottariTitleSuccessReturnsUnit() =
        runTest {
            // given
            val id = 1L
            val title = "renamed title"
            coEvery {
                remoteDataSource.saveBottariTitle(id, UpdateBottariTitleRequest(title))
            } returns Result.success(Unit)

            // when
            val result = repository.saveBottariTitle(id, title)

            // then
            result.shouldBeSuccess {
                it shouldBe Unit
            }

            // verify
            coVerify { remoteDataSource.saveBottariTitle(id, UpdateBottariTitleRequest(title)) }
        }

    @DisplayName("보따리 단건 조회 실패 시 예외를 반환한다")
    @Test
    fun fetchBottariDetailFailureReturnsException() =
        runTest {
            // given
            val id = 1L
            val exception = RuntimeException("단건 조회 실패")
            coEvery { remoteDataSource.fetchBottariDetail(id) } returns Result.failure(exception)

            // when
            val result = repository.fetchBottariDetail(id)

            // then
            result.shouldBeFailure {
                it shouldBe exception
            }

            // verify
            coVerify { remoteDataSource.fetchBottariDetail(id) }
        }

    @DisplayName("보따리 생성 실패 시 예외를 반환한다")
    @Test
    fun createBottariFailureReturnsException() =
        runTest {
            // given
            val title = "error title"
            val exception = RuntimeException("생성 실패")
            coEvery {
                remoteDataSource.createBottari(CreateBottariRequest(title))
            } returns Result.failure(exception)

            // when
            val result = repository.createBottari(title)

            // then
            result.shouldBeFailure {
                it shouldBe exception
            }

            // verify
            coVerify { remoteDataSource.createBottari(CreateBottariRequest(title)) }
        }

    @DisplayName("보따리 이름 변경 실패 시 예외를 반환한다")
    @Test
    fun saveBottariTitleFailureReturnsException() =
        runTest {
            // given
            val id = 1L
            val title = "error title"
            val exception = RuntimeException("제목 변경 실패")
            coEvery {
                remoteDataSource.saveBottariTitle(id, UpdateBottariTitleRequest(title))
            } returns Result.failure(exception)

            // when
            val result = repository.saveBottariTitle(id, title)

            // then
            result.shouldBeFailure {
                it shouldBe exception
            }

            // verify
            coVerify { remoteDataSource.saveBottariTitle(id, UpdateBottariTitleRequest(title)) }
        }

    @DisplayName("보따리 삭제 실패 시 예외를 반환한다")
    @Test
    fun deleteBottariFailureReturnsException() =
        runTest {
            // given
            val id = -1L
            val exception = RuntimeException("삭제 실패")
            coEvery {
                remoteDataSource.deleteBottari(id)
            } returns Result.failure(exception)

            // when
            val result = repository.deleteBottari(id)

            // then
            result.shouldBeFailure {
                it shouldBe exception
            }

            // verify
            coVerify { remoteDataSource.deleteBottari(id) }
        }
}
