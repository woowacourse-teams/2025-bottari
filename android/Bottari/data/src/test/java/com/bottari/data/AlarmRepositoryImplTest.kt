package com.bottari.data

import com.bottari.data.repository.AlarmRepositoryImpl
import com.bottari.data.source.remote.AlarmRemoteDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.shouldBe

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
        // Given
        coEvery { remoteDataSource.activeAlarmState(1L, "ssaid123") } returns Result.success(Unit)

        // When
        val result = repository.activeAlarm("ssaid123", 1L)

        // Then
        result.shouldBeSuccess()

        // Verify
        coVerify { remoteDataSource.activeAlarmState(1L, "ssaid123") }
    }

    @DisplayName("알람을 비활성화 할 수 있다")
    @Test
    fun deactivateAlarmReturnsSuccess() = runTest {
        // Given
        coEvery { remoteDataSource.inactiveAlarmState(1L, "ssaid123") } returns Result.success(Unit)

        // When
        val result = repository.inactiveAlarm("ssaid123", 1L)

        // Then
        result.shouldBeSuccess()

        // Verify
        coVerify { remoteDataSource.inactiveAlarmState(1L, "ssaid123") }
    }

    @DisplayName("알람 활성화 실패 시 예외를 반환한다")
    @Test
    fun activateAlarmReturnsFailureIfRemoteFails() = runTest {
        val expectedException = RuntimeException("알람 활성화 실패")
        coEvery { remoteDataSource.activeAlarmState(1L, "ssaid123") } returns Result.failure(expectedException)

        val result = repository.activeAlarm("ssaid123", 1L)

        result.shouldBeFailure {
            it shouldBe expectedException
        }

        coVerify { remoteDataSource.activeAlarmState(1L, "ssaid123") }
    }

    @DisplayName("알람 비활성화 실패 시 예외를 반환한다")
    @Test
    fun deactivateAlarmReturnsFailureIfRemoteFails() = runTest {
        val expectedException = RuntimeException("알람 비활성화 실패")
        coEvery { remoteDataSource.inactiveAlarmState(1L, "ssaid123") } returns Result.failure(expectedException)

        val result = repository.inactiveAlarm("ssaid123", 1L)

        result.shouldBeFailure {
            it shouldBe expectedException
        }

        coVerify { remoteDataSource.inactiveAlarmState(1L, "ssaid123") }
    }
}