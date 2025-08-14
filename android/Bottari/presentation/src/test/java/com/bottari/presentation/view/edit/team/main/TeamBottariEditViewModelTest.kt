package com.bottari.presentation.view.edit.team.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import com.bottari.domain.usecase.team.FetchTeamBottariDetailUseCase
import com.bottari.presentation.CoroutinesTestExtension
import com.bottari.presentation.InstantTaskExecutorExtension
import com.bottari.presentation.fixture.TEAM_BOTTARI_DETAIL_FIXTURE
import com.bottari.presentation.getOrAwaitValue
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantTaskExecutorExtension::class, CoroutinesTestExtension::class)
class TeamBottariEditViewModelTest {
    private lateinit var fetchTeamBottariDetailUseCase: FetchTeamBottariDetailUseCase
    private lateinit var stateHandle: SavedStateHandle
    private lateinit var viewModel: TeamBottariEditViewModel

    @BeforeEach
    fun setUp() {
        fetchTeamBottariDetailUseCase = mockk<FetchTeamBottariDetailUseCase>()
        stateHandle = SavedStateHandle(mapOf("KEY_BOTTARI_ID" to 1L))
        viewModel = TeamBottariEditViewModel(stateHandle, fetchTeamBottariDetailUseCase)
    }

    @DisplayName("팀 보따리 상세 정보를 조회한다")
    @Test
    fun fetchTeamBottariDetailTest() =
        runTest {
            // given
            val id = 1L
            coEvery { fetchTeamBottariDetailUseCase(id) } returns
                Result.success(
                    TEAM_BOTTARI_DETAIL_FIXTURE,
                )

            // when - fetchBottariDetail()l
            // then
            viewModel.uiState.awaitFetchedState { uiState ->
                assertSoftly(uiState) {
                    bottariTitle shouldBe TEAM_BOTTARI_DETAIL_FIXTURE.title
                    personalItems.size shouldBe TEAM_BOTTARI_DETAIL_FIXTURE.personalItems.size
                    sharedItems.size shouldBe TEAM_BOTTARI_DETAIL_FIXTURE.sharedItems.size
                    assignedItems.size shouldBe TEAM_BOTTARI_DETAIL_FIXTURE.assignedItems.size
                    isFetched shouldBe true
                }
            }
        }

    @DisplayName("팀 보따리 상세 정보를 실패한 경우 실패 이벤트를 발생시킨다")
    @Test
    fun fetchTeamBottariDetailFailureTest() =
        runTest {
            // given
            val id = 1L
            coEvery { fetchTeamBottariDetailUseCase(id) } returns Result.failure(Throwable())

            // when - fetchBottariDetail()l
            // then
            val expected = TeamBottariEditUiEvent.FetchTeamBottariDetailFailure
            viewModel.uiState.awaitFetchedState {
                val uiEvent = viewModel.uiEvent.getOrAwaitValue()
                uiEvent shouldBe expected
            }
        }

    private fun LiveData<TeamBottariEditUiState>.awaitFetchedState(action: (TeamBottariEditUiState) -> Unit) {
        observeForever { uiState ->
            if (uiState.isFetched) action(uiState)
        }
    }
}
