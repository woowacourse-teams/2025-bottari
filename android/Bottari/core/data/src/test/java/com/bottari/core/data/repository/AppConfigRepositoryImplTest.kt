package com.bottari.core.data.repository

import com.bottari.core.data.source.local.AppConfigDataSource
import com.bottari.core.domain.repository.AppConfigRepository
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class AppConfigRepositoryImplTest {
    private lateinit var dataSource: AppConfigDataSource
    private lateinit var repository: AppConfigRepository

    @BeforeEach
    fun setUp() {
        dataSource = mockk<AppConfigDataSource>()
        repository = AppConfigRepositoryImpl(dataSource)
    }

    @DisplayName("권한 플래그 저장에 성공하면 Success를 반환한다")
    @Test
    fun savePermissionFlagReturnsSuccess() =
        runTest {
            // given
            val flag = true
            coEvery { dataSource.savePermissionFlag(flag) } returns Result.success(Unit)

            // when
            val result = repository.savePermissionFlag(flag)

            // then
            result.shouldBeSuccess()

            // verify
            coVerify(exactly = 1) { dataSource.savePermissionFlag(flag) }
        }

    @DisplayName("권한 플래그를 얻어오는데 성공하면 Success를 반환한다")
    @Test
    fun getPermissionFlagReturnsSuccess() =
        runTest {
            // given
            val flag = true
            coEvery { dataSource.getPermissionFlag() } returns Result.success(flag)

            // when
            val result = repository.getPermissionFlag()

            // then
            result.shouldBeSuccess {
                it shouldBe flag
            }

            // verify
            coVerify(exactly = 1) { dataSource.getPermissionFlag() }
        }
}
