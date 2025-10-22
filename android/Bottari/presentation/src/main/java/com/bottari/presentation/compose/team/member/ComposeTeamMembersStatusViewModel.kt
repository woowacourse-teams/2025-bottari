package com.bottari.presentation.compose.team.member

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.bottari.domain.model.event.EventState
import com.bottari.domain.model.team.member.TeamMemberStatus
import com.bottari.domain.usecase.event.ConnectTeamEventUseCase
import com.bottari.domain.usecase.event.DisconnectTeamEventUseCase
import com.bottari.domain.usecase.member.GetMemberIdUseCase
import com.bottari.domain.usecase.team.FetchTeamMembersStatusUseCase
import com.bottari.domain.usecase.team.SendRemindByMemberMessageUseCase
import com.bottari.presentation.common.base.FlowBaseViewModel
import com.bottari.presentation.model.bottari.team.member.TeamMemberStatusUiModel
import com.bottari.presentation.model.bottari.team.member.TeamMemberUiModel
import com.bottari.presentation.util.debounce
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ComposeTeamMembersStatusViewModel @Inject constructor(
    stateHandle: SavedStateHandle,
    private val fetchTeamMembersStatusUseCase: FetchTeamMembersStatusUseCase,
    private val sendRemindByMemberMessageUseCase: SendRemindByMemberMessageUseCase,
    private val getMemberIdUseCase: GetMemberIdUseCase,
    private val connectTeamEventUseCase: ConnectTeamEventUseCase,
    private val disconnectTeamEventUseCase: DisconnectTeamEventUseCase,
) : FlowBaseViewModel<ComposeTeamMembersStatusUiState, ComposeTeamMembersStatusUiEvent>(
        ComposeTeamMembersStatusUiState(),
    ) {
    private val teamBottariId: Long =
        stateHandle[KEY_BOTTARI_ID] ?: error(ERROR_REQUIRE_TEAM_BOTTARI_ID)

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

    fun selectMember(member: TeamMemberStatusUiModel?) {
        updateState {
            copy(selectedMember = member)
        }
    }

    private fun sendRemindMessage(member: TeamMemberUiModel) {
        val memberId =
            member.id ?: run {
                emitEvent(ComposeTeamMembersStatusUiEvent.SendRemindByMemberMessageFailure)
                return
            }
        launch {
            sendRemindByMemberMessageUseCase(teamBottariId, memberId)
                .onSuccess {
                    updateState { copy(selectedMember = null) }
                    emitEvent(
                        ComposeTeamMembersStatusUiEvent.SendRemindByMemberMessageSuccess(
                            member.nickname,
                        ),
                    )
                }.onFailure {
                    updateState { copy(selectedMember = null) }
                    emitEvent(ComposeTeamMembersStatusUiEvent.SendRemindByMemberMessageFailure)
                }
        }
    }

    private fun fetchMemberId() {
        launch {
            getMemberIdUseCase()
                .onSuccess { id ->
                    updateState { copy(myId = id) }
                    fetchTeamMembersStatus()
                }.onFailure { emitEvent(ComposeTeamMembersStatusUiEvent.FetchMemberIdFailure) }
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
                }.onFailure { emitEvent(ComposeTeamMembersStatusUiEvent.FetchMembersStatusFailure) }
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
            val uiModel = TeamMemberStatusUiModel.fromDomain(memberStatus, id)
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
        const val KEY_BOTTARI_ID = "KEY_BOTTARI_ID"
        private const val ERROR_REQUIRE_TEAM_BOTTARI_ID = "[ERROR] 팀 보따리 ID가 존재하지 않습니다."

        private const val DEBOUNCE_DELAY = 300L
    }
}
