package com.bottari.data

import com.bottari.data.repository.AlarmRepositoryImpl
import com.bottari.data.source.remote.AlarmRemoteDataSource
import io.mockk.MockKAnnotations
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test
import kotlinx.coroutines.test.runTest
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Assert.assertTrue


@ExperimentalCoroutinesApi
class AlarmRepositoryImplTest {

    private val mockRemoteDataSource = mockk<AlarmRemoteDataSource>()

    private val repository = AlarmRepositoryImpl(mockRemoteDataSource)

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `알람을 활성화 할 수 있다`() = runTest {
        // given
        coEvery {
            mockRemoteDataSource.activeAlarmState(any(), any())
        } returns Result.success(Unit)

        // when
        val result = repository.activeAlarm("ssaid123", 1L)

        // then
        assertTrue(result.isSuccess)

        // verify
        coVerify { mockRemoteDataSource.activeAlarmState(1L, "ssaid123") }
    }


    @Test
    fun `알람을 비활성화 할 수 있다`() = runTest {
        // given
        coEvery {
            mockRemoteDataSource.inactiveAlarmState(any(), any())
        } returns Result.success(Unit)

        // when
        val result = repository.inactiveAlarm("ssaid123", 1L)

        // then
        assertTrue(result.isSuccess)

        // verify
        coVerify { mockRemoteDataSource.inactiveAlarmState(1L, "ssaid123") }
    }
}
