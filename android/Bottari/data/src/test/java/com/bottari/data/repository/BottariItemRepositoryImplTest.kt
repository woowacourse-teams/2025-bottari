package com.bottari.data.repository

import com.bottari.data.model.item.FetchChecklistResponse
import com.bottari.data.source.remote.BottariItemRemoteDataSource
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class BottariItemRepositoryImplTest {
    private lateinit var remoteDataSource: BottariItemRemoteDataSource
    private lateinit var repository: BottariItemRepositoryImpl

    @BeforeEach
    fun setup() {
        remoteDataSource = mockk()
        repository = BottariItemRepositoryImpl(remoteDataSource)
    }

    @DisplayName("체크리스트 조회에 성공하면 도메인 모델 리스트로 매핑된다")
    @Test
    fun fetchChecklistSuccessReturnsMappedDomainList() =
        runTest {
            // Given
            val ssaid = "ssaid1234"
            val bottariId = 1L
            val responseList =
                listOf(
                    FetchChecklistResponse(id = 1, name = "item1", isChecked = true),
                    FetchChecklistResponse(id = 2, name = "item2", isChecked = false),
                )
            coEvery { remoteDataSource.fetchChecklist(ssaid, bottariId) } returns Result.success(responseList)

            // When
            val result = repository.fetchChecklist(ssaid, bottariId)

            // Then
            result.shouldBeSuccess {
                it shouldHaveSize 2
                it[0].name shouldBe "item1"
            }

            // Verify
            coVerify(exactly = 1) { remoteDataSource.fetchChecklist(ssaid, bottariId) }
        }

    @DisplayName("체크리스트 조회에 실패하면 실패를 반환한다")
    @Test
    fun returnFailureIfLoadChecklistFails() =
        runTest {
            // Given
            val ssaid = "ssaid_error"
            val bottariId = 999L
            val expectedException = RuntimeException("서버 오류")
            coEvery { remoteDataSource.fetchChecklist(ssaid, bottariId) } returns Result.failure(expectedException)

            // When
            val result = repository.fetchChecklist(ssaid, bottariId)

            // Then
            result.shouldBeFailure {
                it shouldBe expectedException
            }

            // Verify
            coVerify(exactly = 1) { remoteDataSource.fetchChecklist(ssaid, bottariId) }
        }

    @DisplayName("아이템 체크에 성공하면 성공 결과를 반환한다")
    @Test
    fun checkItemSuccessReturnsSuccess() =
        runTest {
            val ssaid = "ssaid123"
            val itemId = 10L
            coEvery { remoteDataSource.checkBottariItem(ssaid, itemId) } returns Result.success(Unit)

            val result = repository.checkBottariItem(ssaid, itemId)

            result.shouldBeSuccess()

            coVerify { remoteDataSource.checkBottariItem(ssaid, itemId) }
        }

    @DisplayName("아이템 체크에 실패하면 실패를 반환한다")
    @Test
    fun returnFailureIfCheckItemFails() =
        runTest {
            val ssaid = "ssaid123"
            val itemId = 10L
            val expectedException = RuntimeException("체크 실패")

            coEvery { remoteDataSource.checkBottariItem(ssaid, itemId) } returns Result.failure(expectedException)

            val result = repository.checkBottariItem(ssaid, itemId)

            result.shouldBeFailure {
                it shouldBe expectedException
            }

            coVerify { remoteDataSource.checkBottariItem(ssaid, itemId) }
        }

    @DisplayName("아이템 체크 해제에 성공하면 성공 결과를 반환한다")
    @Test
    fun uncheckItemSuccessReturnsSuccess() =
        runTest {
            val ssaid = "ssaid123"
            val itemId = 11L
            coEvery { remoteDataSource.uncheckBottariItem(ssaid, itemId) } returns Result.success(Unit)

            val result = repository.uncheckBottariItem(ssaid, itemId)

            result.shouldBeSuccess()

            coVerify { remoteDataSource.uncheckBottariItem(ssaid, itemId) }
        }

    @DisplayName("아이템 체크 해제에 실패하면 실패를 반환한다")
    @Test
    fun returnFailureIfUncheckItemFails() =
        runTest {
            val ssaid = "ssaid123"
            val itemId = 11L
            val expectedException = RuntimeException("체크 해제 실패")

            coEvery { remoteDataSource.uncheckBottariItem(ssaid, itemId) } returns Result.failure(expectedException)
            val result = repository.uncheckBottariItem(ssaid, itemId)

            result.shouldBeFailure {
                it shouldBe expectedException
            }

            coVerify { remoteDataSource.uncheckBottariItem(ssaid, itemId) }
        }
}
