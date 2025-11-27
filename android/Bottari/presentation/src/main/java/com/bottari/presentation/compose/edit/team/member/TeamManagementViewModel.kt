package com.bottari.presentation.compose.edit.team.member

import androidx.lifecycle.SavedStateHandle
import com.bottari.core.domain.model.event.EventData
import com.bottari.core.domain.model.event.EventState
import com.bottari.core.domain.model.team.member.TeamStatus
import com.bottari.core.domain.usecase.event.ConnectTeamEventUseCase
import com.bottari.core.domain.usecase.event.DisconnectTeamEventUseCase
import com.bottari.core.domain.usecase.team.FetchTeamMembersUseCase
import com.bottari.logger.BottariLogger
import com.bottari.logger.model.UiEventType
import com.bottari.presentation.common.base.FlowBaseViewModel
import com.bottari.presentation.compose.edit.team.TeamBottariEditActivity.Companion.KEY_BOTTARI_ID
import com.bottari.presentation.model.bottari.team.member.TeamMemberUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamManagementViewModel @Inject constructor(
    stateHandle: SavedStateHandle,
    private val fetchTeamMembersUseCase: FetchTeamMembersUseCase,
    private val connectTeamEventUseCase: ConnectTeamEventUseCase,
    private val disconnectTeamEventUseCase: DisconnectTeamEventUseCase,
) : FlowBaseViewModel<TeamManagementUiState, TeamManagementUiEvent>(
        TeamManagementUiState(),
    ) {
    private val teamBottariId: Long =
        stateHandle[KEY_BOTTARI_ID] ?: error(ERROR_REQUIRE_BOTTARI_ID)

    init {
        handleEvent()
        fetchTeamMembers()
    }

    override fun onCleared() {
        super.onCleared()
        CoroutineScope(Dispatchers.IO).launch { disconnectTeamEventUseCase() }
    }

    private fun fetchTeamMembers() {
        updateState { copy(isLoading = true) }
        launch {
            fetchTeamMembersUseCase(teamBottariId)
                .onSuccess { teamMembers ->
                    updateState { copyFromTeamMembers(teamMembers) }
                    logTeamMembersFetch(teamMembers)
                }.onFailure { emitEvent(TeamManagementUiEvent.FetchTeamMembersFailure) }
        }.invokeOnCompletion { updateState { copy(isLoading = false) } }
    }

    @OptIn(FlowPreview::class)
    private fun handleEvent() {
        launch {
            connectTeamEventUseCase()
                .filterIsInstance<EventState.OnEvent>()
                .map { event -> event.data }
                .filter { eventData ->
                    eventData is EventData.TeamMemberCreate || eventData is EventData.TeamMemberDelete
                }.debounce(DEBOUNCE_DELAY)
                .collect { fetchTeamMembers() }
        }
    }

    private fun TeamManagementUiState.copyFromTeamMembers(teamStatus: TeamStatus): TeamManagementUiState =
        copy(
            inviteCode = teamStatus.inviteCode,
            teamMemberHeadCount = teamStatus.memberCount.value,
            maxHeadCount = teamStatus.memberCount.maxValue,
            members = TeamMemberUiModel.fromDomain(teamStatus),
        )

    private fun logTeamMembersFetch(teamStatus: TeamStatus) {
        BottariLogger.ui(
            type = UiEventType.TEAM_BOTTARI_MEMBERS_FETCH,
            params =
                mapOf(
                    "invite_code" to teamStatus.inviteCode,
                    "member_head_count" to teamStatus.memberCount.value,
                    "host_name" to teamStatus.hostName.value,
                    "members" to teamStatus.nicknames.map { nickname -> nickname.value },
                ),
        )
    }

    companion object {
        private const val ERROR_REQUIRE_BOTTARI_ID = "[ERROR] 보따리 ID가 존재하지 않습니다."
        private const val DEBOUNCE_DELAY = 500L
    }
}
