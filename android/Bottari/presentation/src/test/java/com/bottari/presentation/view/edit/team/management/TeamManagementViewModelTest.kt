package com.bottari.presentation.view.edit.team.management

import androidx.lifecycle.SavedStateHandle
import com.bottari.domain.usecase.event.ConnectTeamEventUseCase
import com.bottari.domain.usecase.event.DisconnectTeamEventUseCase
import com.bottari.domain.usecase.team.FetchTeamMembersUseCase
import com.bottari.presentation.CoroutinesTestExtension
import com.bottari.presentation.InstantTaskExecutorExtension
import com.bottari.presentation.fixture.TEAM_MEMBERS_FIXTURE
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
class TeamManagementViewModelTest {
    private lateinit var fetchTeamMembersUseCase: FetchTeamMembersUseCase
    private lateinit var connectTeamEventUseCase: ConnectTeamEventUseCase
    private lateinit var disconnectTeamEventUseCase: DisconnectTeamEventUseCase
    private lateinit var stateHandle: SavedStateHandle
    private lateinit var viewModel: TeamManagementViewModel

    @BeforeEach
    fun setUp() {
        fetchTeamMembersUseCase = mockk<FetchTeamMembersUseCase>()
        connectTeamEventUseCase = mockk<ConnectTeamEventUseCase>()
        disconnectTeamEventUseCase = mockk<DisconnectTeamEventUseCase>()
        stateHandle = SavedStateHandle(mapOf("KEY_TEAM_BOTTARI_ID" to 1L))
        viewModel =
            TeamManagementViewModel(
                stateHandle,
                fetchTeamMembersUseCase,
                connectTeamEventUseCase,
                disconnectTeamEventUseCase,
            )
    }

    @DisplayName("팀원 정보를 조회한다")
    @Test
    fun fetchTeamMembersTest() =
        runTest {
            // given
            val id = 1L
            coEvery { fetchTeamMembersUseCase(id) } returns Result.success(TEAM_MEMBERS_FIXTURE)

            // when
            viewModel.fetchTeamMembers()

            // then
            assertSoftly(viewModel.uiState.value!!) {
                inviteCode shouldBe "INVITE123"
                teamMemberHeadCount shouldBe 3
                maxHeadCount shouldBe 10
            }
        }
}
