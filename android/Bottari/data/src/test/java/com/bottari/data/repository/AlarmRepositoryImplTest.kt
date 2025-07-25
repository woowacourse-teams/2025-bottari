package com.bottari.data.repository

import com.bottari.data.source.remote.AlarmRemoteDataSource
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
class AlarmRepositoryImplTest {
    private lateinit var remoteDataSource: AlarmRemoteDataSource
    private lateinit var repository: AlarmRepositoryImpl

    @BeforeEach
    fun setup() {
        remoteDataSource = mockk()
        repository = AlarmRepositoryImpl(remoteDataSource)
    }

    @DisplayName("알람을 활성화 할 수 있다")
    @Test
    fun activateAlarmReturnsSuccess() = runTest {
        // given
        coEvery { remoteDataSource.activeAlarmState(1L, "ssaid123") } returns Result.success(Unit)

        // when
        val result = repository.activeAlarm("ssaid123", 1L)

        // then
        result.shouldBeSuccess()

        // verify
        coVerify { remoteDataSource.activeAlarmState(1L, "ssaid123") }
    }

    @DisplayName("알람을 비활성화 할 수 있다")
    @Test
    fun deactivateAlarmReturnsSuccess() = runTest {
        // given
        coEvery { remoteDataSource.inactiveAlarmState(1L, "ssaid123") } returns Result.success(Unit)

        // when
        val result = repository.inactiveAlarm("ssaid123", 1L)

        // then
        result.shouldBeSuccess()

        // verify
        coVerify { remoteDataSource.inactiveAlarmState(1L, "ssaid123") }
    }

    @DisplayName("알람 활성화 실패 시 예외를 반환한다")
    @Test
    fun activateAlarmReturnsFailureIfRemoteFails() = runTest {
        // given
        val expectedException = RuntimeException("알람 활성화 실패")
        coEvery { remoteDataSource.activeAlarmState(1L, "ssaid123") } returns Result.failure(expectedException)

        // when
        val result = repository.activeAlarm("ssaid123", 1L)

        // then
        result.shouldBeFailure {
            it shouldBe expectedException
        }

        // verify
        coVerify { remoteDataSource.activeAlarmState(1L, "ssaid123") }
    }

    @DisplayName("알람 비활성화 실패 시 예외를 반환한다")
    @Test
    fun deactivateAlarmReturnsFailureIfRemoteFails() = runTest {
        // given
        val expectedException = RuntimeException("알람 비활성화 실패")
        coEvery { remoteDataSource.inactiveAlarmState(1L, "ssaid123") } returns Result.failure(expectedException)

        // when
        val result = repository.inactiveAlarm("ssaid123", 1L)

        // then
        result.shouldBeFailure {
            it shouldBe expectedException
        }

        // verify
        coVerify { remoteDataSource.inactiveAlarmState(1L, "ssaid123") }
    }
}
