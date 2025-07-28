package com.bottari.data.repository

import com.bottari.data.model.bottari.CreateBottariRequest
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
            val ssaid = "ssaid123"
            val response = fetchBottariesResponseFixture()
            coEvery { remoteDataSource.fetchBottaries(ssaid) } returns Result.success(response)

            // when
            val result = repository.fetchBottaries(ssaid)

            // then
            result.shouldBeSuccess {
                it shouldHaveSize 2
                it[0].title shouldBe "title1"
                it[1].title shouldBe "title2"
            }

            // verify
            coVerify { remoteDataSource.fetchBottaries(ssaid) }
        }

    @DisplayName("보따리 단건 조회에 성공하면 도메인 모델로 매핑된다")
    @Test
    fun findBottariSuccessReturnsMappedDetail() =
        runTest {
            // given
            val ssaid = "ssaid123"
            val bottariId = 100L
            val detailResponse = bottariResponseFixture()
            coEvery { remoteDataSource.findBottari(bottariId, ssaid) } returns Result.success(detailResponse)

            // when
            val result = repository.findBottari(bottariId, ssaid)

            // then
            result.shouldBeSuccess {
                it.title shouldBe "detail"
            }

            // verify
            coVerify { remoteDataSource.findBottari(bottariId, ssaid) }
        }

    @DisplayName("보따리 생성을 성공하면 ID를 반환한다")
    @Test
    fun createBottariSuccessReturnsId() =
        runTest {
            // given
            val ssaid = "ssaid123"
            val title = "new bottari"
            val expectedId = 42L
            coEvery {
                remoteDataSource.createBottari(ssaid, CreateBottariRequest(title))
            } returns Result.success(expectedId)

            // when
            val result = repository.createBottari(ssaid, title)

            // then
            result.shouldBeSuccess {
                it shouldBe expectedId
            }

            // verify
            coVerify { remoteDataSource.createBottari(ssaid, CreateBottariRequest(title)) }
        }

    @DisplayName("보따리 생성 성공 시 Unit을 반환한다")
    @Test
    fun deleteBottariSuccessReturnsUnit() =
        runTest {
            // given
            val ssaid = "ssaid_error"
            val id = 1L
            coEvery {
                remoteDataSource.deleteBottari(id, ssaid)
            } returns Result.success(Unit)

            // when
            val result = repository.deleteBottari(id, ssaid)

            // then
            result.shouldBeSuccess()

            // verify
            coVerify { remoteDataSource.deleteBottari(id, ssaid) }
        }

    @DisplayName("보따리 목록 조회 실패 시 예외를 반환한다")
    @Test
    fun fetchBottariesFailureReturnsException() =
        runTest {
            // given
            val ssaid = "ssaid_error"
            val exception = RuntimeException("불러오기 실패")
            coEvery { remoteDataSource.fetchBottaries(ssaid) } returns Result.failure(exception)

            // when
            val result = repository.fetchBottaries(ssaid)

            // then
            result.shouldBeFailure {
                it shouldBe exception
            }

            // verify
            coVerify { remoteDataSource.fetchBottaries(ssaid) }
        }

    @DisplayName("보따리 단건 조회 실패 시 예외를 반환한다")
    @Test
    fun findBottariFailureReturnsException() =
        runTest {
            // given
            val ssaid = "ssaid_error"
            val id = 1L
            val exception = RuntimeException("단건 조회 실패")
            coEvery { remoteDataSource.findBottari(id, ssaid) } returns Result.failure(exception)

            // when
            val result = repository.findBottari(id, ssaid)

            // then
            result.shouldBeFailure {
                it shouldBe exception
            }

            // verify
            coVerify { remoteDataSource.findBottari(id, ssaid) }
        }

    @DisplayName("보따리 생성 실패 시 예외를 반환한다")
    @Test
    fun createBottariFailureReturnsException() =
        runTest {
            // given
            val ssaid = "ssaid_error"
            val title = "error title"
            val exception = RuntimeException("생성 실패")
            coEvery {
                remoteDataSource.createBottari(ssaid, CreateBottariRequest(title))
            } returns Result.failure(exception)

            // when
            val result = repository.createBottari(ssaid, title)

            // then
            result.shouldBeFailure {
                it shouldBe exception
            }

            // verify
            coVerify { remoteDataSource.createBottari(ssaid, CreateBottariRequest(title)) }
        }

    @DisplayName("보따리 삭제 실패 시 예외를 반환한다")
    @Test
    fun deleteBottariFailureReturnsException() =
        runTest {
            // given
            val ssaid = "ssaid_error"
            val id = -1L
            val exception = RuntimeException("삭제 실패")
            coEvery {
                remoteDataSource.deleteBottari(id, ssaid)
            } returns Result.failure(exception)

            // when
            val result = repository.deleteBottari(id, ssaid)

            // then
            result.shouldBeFailure {
                it shouldBe exception
            }

            // verify
            coVerify { remoteDataSource.deleteBottari(id, ssaid) }
        }
}
