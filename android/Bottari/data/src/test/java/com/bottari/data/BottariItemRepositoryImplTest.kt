package com.bottari.data

import com.bottari.data.model.item.FetchChecklistResponse
import com.bottari.data.repository.BottariItemRepositoryImpl
import com.bottari.data.source.remote.BottariItemRemoteDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class BottariItemRepositoryImplTest {
    private lateinit var remoteDataSource: BottariItemRemoteDataSource
    private lateinit var repository: BottariItemRepositoryImpl

    @Before
    fun setup() {
        remoteDataSource = mockk()
        repository = BottariItemRepositoryImpl(remoteDataSource)
    }

    @Test
    fun `체크리스트 조회에 성공하면 도메인 모델 리스트로 매핑된다`() =
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
            assertTrue(result.isSuccess)
            val domainList = result.getOrNull()
            assertEquals(2, domainList?.size)
            assertEquals("item1", domainList?.get(0)?.name)

            // Verify
            coVerify(exactly = 1) { remoteDataSource.fetchChecklist(ssaid, bottariId) }
        }

    @Test
    fun `아이템 체크에 성공하면 성공 결과를 반환한다`() =
        runTest {
            // Given
            val ssaid = "ssaid123"
            val itemId = 10L
            coEvery { remoteDataSource.checkBottariItem(ssaid, itemId) } returns Result.success(Unit)

            // When
            val result = repository.checkBottariItem(ssaid, itemId)

            // Then
            assertTrue(result.isSuccess)

            // Verify
            coVerify { remoteDataSource.checkBottariItem(ssaid, itemId) }
        }

    @Test
    fun `아이템 체크 해제에 성공하면 성공 결과를 반환한다`() =
        runTest {
            // Given
            val ssaid = "ssaid123"
            val itemId = 11L
            coEvery { remoteDataSource.uncheckBottariItem(ssaid, itemId) } returns Result.success(Unit)

            // When
            val result = repository.uncheckBottariItem(ssaid, itemId)

            // Then
            assertTrue(result.isSuccess)

            // Verify
            coVerify { remoteDataSource.uncheckBottariItem(ssaid, itemId) }
        }
}
