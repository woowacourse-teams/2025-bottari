package com.bottari.presentation.view.checklist.team.main.member

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.model.event.EventState
import com.bottari.domain.model.team.TeamMemberStatus
import com.bottari.domain.usecase.event.ConnectTeamEventUseCase
import com.bottari.domain.usecase.event.DisconnectTeamEventUseCase
import com.bottari.domain.usecase.member.GetMemberIdUseCase
import com.bottari.domain.usecase.team.FetchTeamMembersStatusUseCase
import com.bottari.domain.usecase.team.SendRemindByMemberMessageUseCase
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.mapper.TeamMembersMapper.toUiModel
import com.bottari.presentation.model.TeamMemberStatusUiModel
import com.bottari.presentation.model.TeamMemberUiModel
import com.bottari.presentation.util.debounce
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class TeamMembersStatusViewModel(
    stateHandle: SavedStateHandle,
    private val fetchTeamMembersStatusUseCase: FetchTeamMembersStatusUseCase,
    private val sendRemindByMemberMessageUseCase: SendRemindByMemberMessageUseCase,
    private val getMemberIdUseCase: GetMemberIdUseCase,
    private val connectTeamEventUseCase: ConnectTeamEventUseCase,
    private val disconnectTeamEventUseCase: DisconnectTeamEventUseCase,
) : BaseViewModel<TeamMembersStatusUiState, TeamMembersStatusUiEvent>(
        TeamMembersStatusUiState(),
    ) {
    private val teamBottariId: Long =
        stateHandle[KEY_TEAM_BOTTARI_ID] ?: error(ERROR_REQUIRE_TEAM_BOTTARI_ID)

    val debouncedSendRemindMessage: (member: TeamMemberUiModel) -> Unit =
        debounce(
            timeMillis = DEBOUNCE_DELAY,
            coroutineScope = viewModelScope,
        ) { member -> sendRemindMessage(member) }

    init {
        fetchMemberId()
        handleEvent()
    }

    override fun onCleared() {
        super.onCleared()
        CoroutineScope(Dispatchers.IO).launch { disconnectTeamEventUseCase() }
    }

    private fun sendRemindMessage(member: TeamMemberUiModel) {
        val memberId =
            member.id ?: run {
                emitEvent(TeamMembersStatusUiEvent.SendRemindByMemberMessageFailure)
                return
            }
        launch {
            sendRemindByMemberMessageUseCase(teamBottariId, memberId)
                .onSuccess {
                    emitEvent(
                        TeamMembersStatusUiEvent.SendRemindByMemberMessageSuccess(
                            member.nickname,
                        ),
                    )
                }.onFailure { emitEvent(TeamMembersStatusUiEvent.SendRemindByMemberMessageFailure) }
        }
    }

    fun updateExpandState(id: Long) {
        val newMembersStatus =
            currentState.membersStatus.map { memberStatus ->
                if (memberStatus.member.id == id) return@map memberStatus.copy(isExpanded = !memberStatus.isExpanded)
                memberStatus
            }
        updateState { copy(membersStatus = newMembersStatus) }
    }

    private fun fetchMemberId() {
        launch {
            getMemberIdUseCase()
                .onSuccess { id ->
                    updateState { copy(myId = id) }
                    fetchTeamMembersStatus()
                }.onFailure { emitEvent(TeamMembersStatusUiEvent.FetchMemberIdFailure) }
        }
    }

    private fun fetchTeamMembersStatus() {
        val myId = currentState.myId
        updateState { copy(isLoading = true) }
        launch {
            fetchTeamMembersStatusUseCase(teamBottariId)
                .onSuccess { membersStatus ->
                    val updated = mergeWithPreviousState(membersStatus, myId)
                    updateState {
                        copy(
                            membersStatus = updated,
                        )
                    }
                }.onFailure { emitEvent(TeamMembersStatusUiEvent.FetchMembersStatusFailure) }
            updateState { copy(isLoading = false) }
        }
    }

    @OptIn(FlowPreview::class)
    private fun handleEvent() {
        launch {
            connectTeamEventUseCase(teamBottariId)
                .filterIsInstance<EventState.OnEvent>()
                .map { event -> event.data }
                .debounce(DEBOUNCE_DELAY)
                .onEach { fetchMemberId() }
                .launchIn(this)
        }
    }

    private fun mergeWithPreviousState(
        teamMembersStatus: List<TeamMemberStatus>,
        id: Long,
    ): List<TeamMemberStatusUiModel> =
        teamMembersStatus.map { memberStatus ->
            val uiModel = memberStatus.toUiModel(id)
            val previousState =
                currentState.membersStatus.find { it.member.id == uiModel.member.id }
            if (previousState != null && uiModel.isItemsEmpty.not()) {
                return@map uiModel.copy(
                    isExpanded = previousState.isExpanded,
                )
            }
            uiModel
        }

    companion object {
        private const val KEY_TEAM_BOTTARI_ID = "KEY_TEAM_BOTTARI_ID"
        private const val ERROR_REQUIRE_TEAM_BOTTARI_ID = "[ERROR] 팀 보따리 ID가 존재하지 않습니다."

        private const val DEBOUNCE_DELAY = 300L

        fun Factory(id: Long): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val stateHandle = createSavedStateHandle()
                    stateHandle[KEY_TEAM_BOTTARI_ID] = id
                    TeamMembersStatusViewModel(
                        stateHandle = stateHandle,
                        fetchTeamMembersStatusUseCase = UseCaseProvider.fetchTeamMembersStatusUseCase,
                        sendRemindByMemberMessageUseCase = UseCaseProvider.sendRemindByMemberMessageUseCase,
                        getMemberIdUseCase = UseCaseProvider.getMemberIdUseCase,
                        connectTeamEventUseCase = UseCaseProvider.connectTeamEventUseCase,
                        disconnectTeamEventUseCase = UseCaseProvider.disconnectTeamEventUseCase,
                    )
                }
            }
    }
}
