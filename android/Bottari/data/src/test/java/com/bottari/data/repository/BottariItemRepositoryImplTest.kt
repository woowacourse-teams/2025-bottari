package com.bottari.data.repository

import com.bottari.data.source.remote.BottariItemRemoteDataSource
import com.bottari.data.testFixture.fetchChecklistResponseListFixture
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
            // given
            val ssaid = "ssaid1234"
            val bottariId = 1L
            val responseList = fetchChecklistResponseListFixture()
            coEvery { remoteDataSource.fetchChecklist(ssaid, bottariId) } returns Result.success(responseList)

            // when
            val result = repository.fetchChecklist(ssaid, bottariId)

            // then
            result.shouldBeSuccess {
                it shouldHaveSize 2
                it[0].name shouldBe "item1"
            }

            // verify
            coVerify(exactly = 1) { remoteDataSource.fetchChecklist(ssaid, bottariId) }
        }

    @DisplayName("체크리스트 조회에 실패하면 실패를 반환한다")
    @Test
    fun returnFailureIfLoadChecklistFails() =
        runTest {
            // given
            val ssaid = "ssaid_error"
            val bottariId = 999L
            val expectedException = RuntimeException("서버 오류")
            coEvery { remoteDataSource.fetchChecklist(ssaid, bottariId) } returns Result.failure(expectedException)

            // when
            val result = repository.fetchChecklist(ssaid, bottariId)

            // then
            result.shouldBeFailure {
                it shouldBe expectedException
            }

            // verify
            coVerify(exactly = 1) { remoteDataSource.fetchChecklist(ssaid, bottariId) }
        }

    @DisplayName("아이템 체크에 성공하면 성공 결과를 반환한다")
    @Test
    fun checkItemSuccessReturnsSuccess() =
        runTest {
            // given
            val ssaid = "ssaid123"
            val itemId = 10L
            coEvery { remoteDataSource.checkBottariItem(ssaid, itemId) } returns Result.success(Unit)

            // when
            val result = repository.checkBottariItem(ssaid, itemId)

            // then
            result.shouldBeSuccess()

            // verify
            coVerify { remoteDataSource.checkBottariItem(ssaid, itemId) }
        }

    @DisplayName("아이템 체크에 실패하면 실패를 반환한다")
    @Test
    fun returnFailureIfCheckItemFails() =
        runTest {
            // given
            val ssaid = "ssaid123"
            val itemId = 10L
            val expectedException = RuntimeException("체크 실패")
            coEvery { remoteDataSource.checkBottariItem(ssaid, itemId) } returns Result.failure(expectedException)

            // when
            val result = repository.checkBottariItem(ssaid, itemId)

            // then
            result.shouldBeFailure {
                it shouldBe expectedException
            }

            // verify
            coVerify { remoteDataSource.checkBottariItem(ssaid, itemId) }
        }

    @DisplayName("아이템 체크 해제에 성공하면 성공 결과를 반환한다")
    @Test
    fun uncheckItemSuccessReturnsSuccess() =
        runTest {
            // given
            val ssaid = "ssaid123"
            val itemId = 11L
            coEvery { remoteDataSource.uncheckBottariItem(ssaid, itemId) } returns Result.success(Unit)

            // when
            val result = repository.uncheckBottariItem(ssaid, itemId)

            // then
            result.shouldBeSuccess()

            // verify
            coVerify { remoteDataSource.uncheckBottariItem(ssaid, itemId) }
        }

    @DisplayName("아이템 체크 해제에 실패하면 실패를 반환한다")
    @Test
    fun returnFailureIfUncheckItemFails() =
        runTest {
            // given
            val ssaid = "ssaid123"
            val itemId = 11L
            val expectedException = RuntimeException("체크 해제 실패")
            coEvery { remoteDataSource.uncheckBottariItem(ssaid, itemId) } returns Result.failure(expectedException)

            // when
            val result = repository.uncheckBottariItem(ssaid, itemId)

            // then
            result.shouldBeFailure {
                it shouldBe expectedException
            }

            // verify
            coVerify { remoteDataSource.uncheckBottariItem(ssaid, itemId) }
        }
}
