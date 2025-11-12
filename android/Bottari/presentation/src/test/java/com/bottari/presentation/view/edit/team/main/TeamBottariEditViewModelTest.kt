package com.bottari.presentation.view.edit.team.main

import androidx.lifecycle.SavedStateHandle
import com.bottari.domain.usecase.event.ConnectTeamEventUseCase
import com.bottari.domain.usecase.event.DisconnectTeamEventUseCase
import com.bottari.domain.usecase.team.FetchTeamBottariDetailUseCase
import com.bottari.presentation.CoroutinesTestExtension
import com.bottari.presentation.InstantTaskExecutorExtension
import com.bottari.presentation.compose.edit.team.main.TeamBottariEditUiEvent
import com.bottari.presentation.compose.edit.team.main.TeamBottariEditViewModel
import com.bottari.presentation.fixture.TEAM_BOTTARI_DETAIL_FIXTURE
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantTaskExecutorExtension::class, CoroutinesTestExtension::class)
class TeamBottariEditViewModelTest {
    private lateinit var fetchTeamBottariDetailUseCase: FetchTeamBottariDetailUseCase
    private lateinit var connectTeamEventUseCase: ConnectTeamEventUseCase
    private lateinit var disconnectTeamEventUseCase: DisconnectTeamEventUseCase
    private lateinit var stateHandle: SavedStateHandle

    @BeforeEach
    fun setUp() {
        fetchTeamBottariDetailUseCase = mockk<FetchTeamBottariDetailUseCase>()
        connectTeamEventUseCase = mockk<ConnectTeamEventUseCase>()
        disconnectTeamEventUseCase = mockk<DisconnectTeamEventUseCase>()
        stateHandle = SavedStateHandle(mapOf("KEY_BOTTARI_ID" to 1L))
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

            // when
            val viewModel =
                TeamBottariEditViewModel(
                    stateHandle,
                    fetchTeamBottariDetailUseCase,
                    connectTeamEventUseCase,
                    disconnectTeamEventUseCase,
                )
            advanceUntilIdle()

            // then
            val uiState = viewModel.uiState.value
            assertSoftly(uiState) {
                bottariTitle shouldBe TEAM_BOTTARI_DETAIL_FIXTURE.bottari.title
                personalItems.size shouldBe TEAM_BOTTARI_DETAIL_FIXTURE.personalItems.size
                sharedItems.size shouldBe TEAM_BOTTARI_DETAIL_FIXTURE.sharedItems.size
                assignedItems.size shouldBe TEAM_BOTTARI_DETAIL_FIXTURE.assignedItems.size
                isFetched shouldBe true
            }
        }

    @DisplayName("팀 보따리 상세 정보를 실패한 경우 실패 이벤트를 발생시킨다")
    @Test
    fun fetchTeamBottariDetailFailureTest() =
        runTest {
            // given + when
            val id = 1L
            coEvery { fetchTeamBottariDetailUseCase(id) } returns Result.failure(Throwable())
            val expected = TeamBottariEditUiEvent.FetchTeamBottariDetailFailure
            val collectedEvents = mutableListOf<TeamBottariEditUiEvent>()

            val viewModel =
                TeamBottariEditViewModel(
                    stateHandle,
                    fetchTeamBottariDetailUseCase,
                    connectTeamEventUseCase,
                    disconnectTeamEventUseCase,
                )

            val job =
                launch {
                    viewModel.uiEvent.collect { event -> collectedEvents.add(event) }
                }
            advanceUntilIdle()

            // then
            assertSoftly {
                collectedEvents.size shouldBe 1
                collectedEvents[0] shouldBe expected
                viewModel.uiState.value.isFetched shouldBe false
            }

            job.cancel()
        }
}
