package com.bottari.feature.team.checklist.member

import androidx.compose.runtime.Stable
import androidx.lifecycle.viewModelScope
import com.bottari.common.util.debounce
import com.bottari.core.domain.model.event.EventState
import com.bottari.core.domain.model.team.member.TeamMemberStatus
import com.bottari.core.domain.usecase.event.ConnectTeamEventUseCase
import com.bottari.core.domain.usecase.event.DisconnectTeamEventUseCase
import com.bottari.core.domain.usecase.member.GetMemberIdUseCase
import com.bottari.core.domain.usecase.team.FetchTeamMembersStatusUseCase
import com.bottari.core.domain.usecase.team.SendRemindByMemberMessageUseCase
import com.bottari.core.ui.base.FlowBaseViewModel
import com.bottari.core.ui.model.bottari.team.member.TeamMemberStatusUiModel
import com.bottari.core.ui.model.bottari.team.member.TeamMemberUiModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
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

@Stable
@HiltViewModel(assistedFactory = ComposeTeamMembersStatusViewModel.Factory::class)
class ComposeTeamMembersStatusViewModel @AssistedInject constructor(
    @Assisted private val teamBottariId: Long,
    private val fetchTeamMembersStatusUseCase: FetchTeamMembersStatusUseCase,
    private val sendRemindByMemberMessageUseCase: SendRemindByMemberMessageUseCase,
    private val getMemberIdUseCase: GetMemberIdUseCase,
    private val connectTeamEventUseCase: ConnectTeamEventUseCase,
    private val disconnectTeamEventUseCase: DisconnectTeamEventUseCase,
) : FlowBaseViewModel<ComposeTeamMembersStatusUiState, ComposeTeamMembersStatusUiEvent>(
        ComposeTeamMembersStatusUiState(),
    ) {
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

    fun fetchMemberId() {
        launch {
            getMemberIdUseCase()
                .onSuccess { id ->
                    updateState { copy(myId = id) }
                }
        }.invokeOnCompletion { fetchTeamMembersStatus() }
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

    private fun fetchTeamMembersStatus() {
        val myId = currentState.myId
        launch {
            updateState { copy(isLoading = true) }
            fetchTeamMembersStatusUseCase(teamBottariId)
                .onSuccess { membersStatus ->
                    val updated = mergeWithPreviousState(membersStatus, myId)
                    updateState { copy(membersStatus = updated) }
                }.onFailure { emitEvent(ComposeTeamMembersStatusUiEvent.FetchMembersStatusFailure) }
        }.invokeOnCompletion { updateState { copy(isLoading = false, isFetched = true) } }
    }

    @OptIn(FlowPreview::class)
    private fun handleEvent() {
        launch {
            connectTeamEventUseCase()
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

    @AssistedFactory
    interface Factory {
        fun create(teamBottariId: Long): ComposeTeamMembersStatusViewModel
    }

    companion object {
        private const val DEBOUNCE_DELAY = 300L
    }
}
