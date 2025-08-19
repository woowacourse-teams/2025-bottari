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
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

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
            val bottariId = 1L
            val responseList = fetchChecklistResponseListFixture()
            coEvery { remoteDataSource.fetchChecklist(bottariId) } returns
                Result.success(
                    responseList,
                )

            // when
            val result = repository.fetchChecklist(bottariId)

            // then
            result.shouldBeSuccess {
                it shouldHaveSize 2
                it[0].name shouldBe "item1"
            }

            // verify
            coVerify(exactly = 1) { remoteDataSource.fetchChecklist(bottariId) }
        }

    @DisplayName("체크리스트 조회에 실패하면 실패를 반환한다")
    @Test
    fun returnFailureIfLoadChecklistFails() =
        runTest {
            // given
            val bottariId = 999L
            val expectedException = RuntimeException("서버 오류")
            coEvery { remoteDataSource.fetchChecklist(bottariId) } returns
                Result.failure(
                    expectedException,
                )

            // when
            val result = repository.fetchChecklist(bottariId)

            // then
            result.shouldBeFailure {
                it shouldBe expectedException
            }

            // verify
            coVerify(exactly = 1) { remoteDataSource.fetchChecklist(bottariId) }
        }

    @DisplayName("아이템 체크에 성공하면 성공 결과를 반환한다")
    @Test
    fun checkItemSuccessReturnsSuccess() =
        runTest {
            // given
            val itemId = 10L
            coEvery { remoteDataSource.checkBottariItem(itemId) } returns Result.success(Unit)

            // when
            val result = repository.checkBottariItem(itemId)

            // then
            result.shouldBeSuccess()

            // verify
            coVerify { remoteDataSource.checkBottariItem(itemId) }
        }

    @DisplayName("아이템 체크에 실패하면 실패를 반환한다")
    @Test
    fun returnFailureIfCheckItemFails() =
        runTest {
            // given

            val itemId = 10L
            val expectedException = RuntimeException("체크 실패")
            coEvery { remoteDataSource.checkBottariItem(itemId) } returns
                Result.failure(
                    expectedException,
                )

            // when
            val result = repository.checkBottariItem(itemId)

            // then
            result.shouldBeFailure {
                it shouldBe expectedException
            }

            // verify
            coVerify { remoteDataSource.checkBottariItem(itemId) }
        }

    @DisplayName("아이템 체크 해제에 성공하면 성공 결과를 반환한다")
    @Test
    fun uncheckItemSuccessReturnsSuccess() =
        runTest {
            // given

            val itemId = 11L
            coEvery { remoteDataSource.uncheckBottariItem(itemId) } returns Result.success(Unit)

            // when
            val result = repository.uncheckBottariItem(itemId)

            // then
            result.shouldBeSuccess()

            // verify
            coVerify { remoteDataSource.uncheckBottariItem(itemId) }
        }

    @DisplayName("아이템 체크 해제에 실패하면 실패를 반환한다")
    @Test
    fun returnFailureIfUncheckItemFails() =
        runTest {
            // given

            val itemId = 11L
            val expectedException = RuntimeException("체크 해제 실패")
            coEvery { remoteDataSource.uncheckBottariItem(itemId) } returns
                Result.failure(
                    expectedException,
                )

            // when
            val result = repository.uncheckBottariItem(itemId)

            // then
            result.shouldBeFailure {
                it shouldBe expectedException
            }

            // verify
            coVerify { remoteDataSource.uncheckBottariItem(itemId) }
        }

    @DisplayName("체크 상태 초기화에 성공하면 성공 결과를 반환한다")
    @Test
    fun resetCheckStateSuccessReturnsSuccess() =
        runTest {
            // given
            val bottariId = 1L
            coEvery { remoteDataSource.resetBottariItemCheckState(bottariId) } returns
                Result.success(Unit)

            // when
            val result = repository.resetBottariItemCheckState(bottariId)

            // then
            result.shouldBeSuccess()

            // verify
            coVerify { remoteDataSource.resetBottariItemCheckState(bottariId) }
        }

    @DisplayName("체크 상태 초기화에 실패하면 실패를 반환한다")
    @Test
    fun resetCheckStateFailureReturnsFailure() =
        runTest {
            // given
            val bottariId = 1L
            val expectedException = Exception()
            coEvery { remoteDataSource.resetBottariItemCheckState(bottariId) } returns
                Result.failure(expectedException)

            // when
            val result = repository.resetBottariItemCheckState(bottariId)

            // then
            result.shouldBeFailure { error -> error shouldBe expectedException }

            // verify
            coVerify { remoteDataSource.resetBottariItemCheckState(bottariId) }
        }
}
