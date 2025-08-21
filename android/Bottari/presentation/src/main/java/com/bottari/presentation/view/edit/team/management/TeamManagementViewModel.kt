package com.bottari.presentation.view.edit.team.management

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.model.event.EventData
import com.bottari.domain.model.event.EventState
import com.bottari.domain.model.team.TeamMembers
import com.bottari.domain.usecase.event.ConnectTeamEventUseCase
import com.bottari.domain.usecase.event.DisconnectTeamEventUseCase
import com.bottari.domain.usecase.team.FetchTeamMembersUseCase
import com.bottari.logger.BottariLogger
import com.bottari.logger.model.UiEventType
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.mapper.TeamMembersMapper.toUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class TeamManagementViewModel(
    stateHandle: SavedStateHandle,
    private val fetchTeamMembersUseCase: FetchTeamMembersUseCase,
    private val connectTeamEventUseCase: ConnectTeamEventUseCase,
    private val disconnectTeamEventUseCase: DisconnectTeamEventUseCase,
) : BaseViewModel<TeamManagementUiState, TeamManagementUiEvent>(
        TeamManagementUiState(),
    ) {
    private val teamBottariId: Long =
        stateHandle[KEY_TEAM_BOTTARI_ID] ?: error(ERROR_REQUIRE_TEAM_BOTTARI_ID)

    init {
        handleEvent()
    }

    override fun onCleared() {
        super.onCleared()
        CoroutineScope(Dispatchers.IO).launch { disconnectTeamEventUseCase() }
    }

    fun fetchTeamMembers() {
        updateState { copy(isLoading = true) }
        launch {
            fetchTeamMembersUseCase(teamBottariId)
                .onSuccess { teamMembers ->
                    updateState { copyFromTeamMembers(teamMembers) }
                    logTeamMembersFetch(teamMembers)
                }.onFailure {
                    emitEvent(TeamManagementUiEvent.FetchTeamMembersFailure)
                }
            updateState { copy(isLoading = false) }
        }
    }

    @OptIn(FlowPreview::class)
    private fun handleEvent() {
        launch {
            connectTeamEventUseCase(teamBottariId)
                .filterIsInstance<EventState.OnEvent>()
                .map { event -> event.data }
                .filter { eventData ->
                    eventData is EventData.TeamMemberCreate || eventData is EventData.TeamMemberDelete
                }.debounce(DEBOUNCE_DELAY)
                .onEach { fetchTeamMembers() }
                .launchIn(this)
        }
    }

    private fun TeamManagementUiState.copyFromTeamMembers(teamMembers: TeamMembers): TeamManagementUiState =
        copy(
            inviteCode = teamMembers.inviteCode,
            teamMemberHeadCount = teamMembers.teamMemberHeadCount.value,
            maxHeadCount = teamMembers.teamMemberHeadCount.maxValue,
            members = teamMembers.toUiModel(),
        )

    private fun logTeamMembersFetch(teamMembers: TeamMembers) {
        BottariLogger.ui(
            UiEventType.TEAM_BOTTARI_MEMBERS_FETCH,
            mapOf(
                "invite_code" to teamMembers.inviteCode,
                "member_head_count" to teamMembers.teamMemberHeadCount.value,
                "host_name" to teamMembers.hostName.value,
                "members" to teamMembers.memberNicknames.map { nickname -> nickname.value },
            ),
        )
    }

    companion object {
        private const val KEY_TEAM_BOTTARI_ID = "KEY_TEAM_BOTTARI_ID"
        private const val ERROR_REQUIRE_TEAM_BOTTARI_ID = "[ERROR] 팀 보따리 ID가 존재하지 않습니다."
        private const val DEBOUNCE_DELAY = 500L

        fun Factory(teamBottariId: Long): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val stateHandle = createSavedStateHandle()
                    stateHandle[KEY_TEAM_BOTTARI_ID] = teamBottariId
                    TeamManagementViewModel(
                        stateHandle = stateHandle,
                        fetchTeamMembersUseCase = UseCaseProvider.fetchTeamMembersUseCase,
                        connectTeamEventUseCase = UseCaseProvider.connectTeamEventUseCase,
                        disconnectTeamEventUseCase = UseCaseProvider.disconnectTeamEventUseCase,
                    )
                }
            }
    }
}
