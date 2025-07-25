package com.bottari.data.repository

import com.bottari.data.model.bottari.AlarmResponse
import com.bottari.data.model.bottari.BottariResponse
import com.bottari.data.model.bottari.CreateBottariRequest
import com.bottari.data.model.bottari.FetchBottariesResponse
import com.bottari.data.model.bottari.RoutineResponse
import com.bottari.data.source.remote.BottariRemoteDataSource
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
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
            val ssaid = "ssaid123"
            val response =
                listOf(
                    FetchBottariesResponse(
                        id = 2,
                        title = "title1",
                        alarmResponse = null,
                        checkedItemsCount = 0,
                        totalItemsCount = 0,
                    ),
                    FetchBottariesResponse(
                        id = 3,
                        title = "title2",
                        alarmResponse = null,
                        checkedItemsCount = 1,
                        totalItemsCount = 3,
                    ),
                )
            coEvery { remoteDataSource.fetchBottaries(ssaid) } returns Result.success(response)

            val result = repository.fetchBottaries(ssaid)

            result.shouldBeSuccess {
                it shouldHaveSize 2
                it[0].title shouldBe "title1"
                it[1].title shouldBe "title2"
            }

            coVerify { remoteDataSource.fetchBottaries(ssaid) }
        }

    @DisplayName("보따리 단건 조회에 성공하면 도메인 모델로 매핑된다")
    @Test
    fun findBottariSuccessReturnsMappedDetail() =
        runTest {
            val ssaid = "ssaid123"
            val bottariId = 100L
            val mockAlarm =
                mockk<AlarmResponse>(relaxed = true) {
                    every { id } returns 1
                    every { isActive } returns true
                    every { routine } returns
                        mockk<RoutineResponse> (relaxed = true) {
                            every { type } returns "NON_REPEAT"
                        }
                }
            val detailResponse =
                BottariResponse(
                    id = bottariId,
                    title = "detail",
                    items = emptyList(),
                    alarm = mockAlarm,
                )
            coEvery { remoteDataSource.findBottari(bottariId, ssaid) } returns
                Result.success(
                    detailResponse,
                )

            val result = repository.findBottari(bottariId, ssaid)

            result.shouldBeSuccess {
                it.title shouldBe "detail"
            }

            coVerify { remoteDataSource.findBottari(bottariId, ssaid) }
        }

    @DisplayName("보따리 생성을 성공하면 ID를 반환한다")
    @Test
    fun createBottariSuccessReturnsId() =
        runTest {
            val ssaid = "ssaid123"
            val title = "new bottari"
            val expectedId = 42L
            coEvery {
                remoteDataSource.createBottari(
                    ssaid,
                    CreateBottariRequest(title),
                )
            } returns Result.success(expectedId)

            val result = repository.createBottari(ssaid, title)

            result.shouldBeSuccess {
                it shouldBe expectedId
            }

            coVerify { remoteDataSource.createBottari(ssaid, CreateBottariRequest(title)) }
        }

    @DisplayName("보따리 목록 조회 실패 시 예외를 반환한다")
    @Test
    fun fetchBottariesFailureReturnsException() =
        runTest {
            val ssaid = "ssaid_error"
            val exception = RuntimeException("불러오기 실패")
            coEvery { remoteDataSource.fetchBottaries(ssaid) } returns Result.failure(exception)

            val result = repository.fetchBottaries(ssaid)

            result.shouldBeFailure {
                it shouldBe exception
            }

            coVerify { remoteDataSource.fetchBottaries(ssaid) }
        }

    @DisplayName("보따리 단건 조회 실패 시 예외를 반환한다")
    @Test
    fun findBottariFailureReturnsException() =
        runTest {
            val ssaid = "ssaid_error"
            val id = 1L
            val exception = RuntimeException("단건 조회 실패")
            coEvery { remoteDataSource.findBottari(id, ssaid) } returns Result.failure(exception)

            val result = repository.findBottari(id, ssaid)

            result.shouldBeFailure {
                it shouldBe exception
            }

            coVerify { remoteDataSource.findBottari(id, ssaid) }
        }

    @DisplayName("보따리 생성 실패 시 예외를 반환한다")
    @Test
    fun createBottariFailureReturnsException() =
        runTest {
            val ssaid = "ssaid_error"
            val title = "error title"
            val exception = RuntimeException("생성 실패")
            coEvery {
                remoteDataSource.createBottari(
                    ssaid,
                    CreateBottariRequest(title),
                )
            } returns Result.failure(exception)

            val result = repository.createBottari(ssaid, title)

            result.shouldBeFailure {
                it shouldBe exception
            }

            coVerify { remoteDataSource.createBottari(ssaid, CreateBottariRequest(title)) }
        }
}
