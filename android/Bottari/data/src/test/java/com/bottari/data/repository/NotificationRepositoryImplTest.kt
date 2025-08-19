package com.bottari.data.repository

import com.bottari.data.source.remote.NotificationLocalDataSource
import com.bottari.data.testFixture.NOTIFICATION_ENTITIES_FIXTURE
import com.bottari.data.testFixture.NOTIFICATION_ENTITY_FIXTURE
import com.bottari.data.testFixture.NOTIFICATION_FIXTURE
import com.bottari.domain.repository.NotificationRepository
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.collections.shouldContain
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

class NotificationRepositoryImplTest {
    private lateinit var dataSource: NotificationLocalDataSource
    private lateinit var repository: NotificationRepository

    @BeforeEach
    fun setUp() {
        dataSource = mockk<NotificationLocalDataSource>()
        repository = NotificationRepositoryImpl(dataSource)
    }

    @DisplayName("알람 조회에 성공하면 Success를 반환한다")
    @Test
    fun getNotificationsReturnsSuccessTest() =
        runTest {
            // given
            coEvery { dataSource.getNotifications() } returns
                Result.success(
                    NOTIFICATION_ENTITIES_FIXTURE,
                )

            // when
            val result = repository.getNotifications()

            // then
            assertSoftly(result) {
                shouldBeSuccess()
                getOrThrow().shouldContain(NOTIFICATION_FIXTURE)
            }

            // verify
            coVerify(exactly = 1) { dataSource.getNotifications() }
        }

    @DisplayName("알람 조회에 실패하면 Failure를 반환한다")
    @Test
    fun getNotificationsReturnsFailureTest() =
        runTest {
            // given
            val exception = IllegalArgumentException("[ERROR] 알람 조회에 실패했습니다.")
            coEvery { dataSource.getNotifications() } returns Result.failure(exception)

            // when
            val result = repository.getNotifications()

            // then
            result.shouldBeFailure { error ->
                assertSoftly(error) {
                    shouldBe(exception)
                    message shouldBe "[ERROR] 알람 조회에 실패했습니다."
                }
            }

            // verify
            coVerify(exactly = 1) { dataSource.getNotifications() }
        }

    @DisplayName("알람 저장에 성공하면 Success를 반환한다")
    @Test
    fun saveNotificationReturnsSuccessTest() =
        runTest {
            // given
            coEvery { dataSource.saveNotification(NOTIFICATION_ENTITY_FIXTURE) } returns
                Result.success(
                    Unit,
                )

            // when
            val result = repository.saveNotification(NOTIFICATION_FIXTURE)

            // then
            result.shouldBeSuccess()

            // verify
            coVerify(exactly = 1) { dataSource.saveNotification(NOTIFICATION_ENTITY_FIXTURE) }
        }

    @DisplayName("알람 저장에 실패하면 Failure를 반환한다")
    @Test
    fun saveNotificationReturnsFailureTest() =
        runTest {
            // given
            val exception = IllegalArgumentException("[ERROR] 알람 저장에 실패했습니다.")
            coEvery { dataSource.saveNotification(NOTIFICATION_ENTITY_FIXTURE) } returns
                Result.failure(
                    exception,
                )

            // when
            val result = repository.saveNotification(NOTIFICATION_FIXTURE)

            // then
            result.shouldBeFailure { error ->
                assertSoftly(error) {
                    shouldBe(exception)
                    message shouldBe "[ERROR] 알람 저장에 실패했습니다."
                }
            }
        }

    @DisplayName("알람 삭제에 성공하면 Success를 반환한다")
    @Test
    fun deleteNotificationReturnsSuccessTest() =
        runTest {
            // given
            coEvery { dataSource.deleteNotification(1L) } returns Result.success(Unit)

            // when
            val result = repository.deleteNotification(1L)

            // then
            result.shouldBeSuccess()

            // verify
            coVerify(exactly = 1) { dataSource.deleteNotification(1L) }
        }

    @DisplayName("알람 삭제에 실패하면 Failure를 반환한다")
    @Test
    fun deleteNotificationReturnsFailureTest() =
        runTest {
            // given
            val exception = IllegalArgumentException("[ERROR] 알람 삭제에 실패했습니다.")
            coEvery { dataSource.deleteNotification(1L) } returns Result.failure(exception)

            // when
            val result = repository.deleteNotification(1L)

            // then
            result.shouldBeFailure { error ->
                error shouldBe exception
            }

            // verify
            coVerify(exactly = 1) { dataSource.deleteNotification(1L) }
        }
}
