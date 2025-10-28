package com.bottari.presentation.compose.team.item

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.bottari.domain.model.event.EventState
import com.bottari.domain.model.team.bottari.TeamBottariStatus
import com.bottari.domain.usecase.event.ConnectTeamEventUseCase
import com.bottari.domain.usecase.event.DisconnectTeamEventUseCase
import com.bottari.domain.usecase.member.CheckRegisteredMemberUseCase
import com.bottari.domain.usecase.team.FetchTeamStatusUseCase
import com.bottari.domain.usecase.team.SendRemindByItemUseCase
import com.bottari.presentation.common.base.FlowBaseViewModel
import com.bottari.presentation.model.bottari.personal.BottariItemTypeUiModel
import com.bottari.presentation.model.bottari.team.TeamBottariUiModelStatus
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
class ComposeTeamBottariItemStatusViewModel @Inject constructor(
    stateHandle: SavedStateHandle,
    private val fetchTeamStatusUseCase: FetchTeamStatusUseCase,
    private val sendRemindByItemUseCase: SendRemindByItemUseCase,
    private val connectTeamEventUseCase: ConnectTeamEventUseCase,
    private val disconnectTeamEventUseCase: DisconnectTeamEventUseCase,
    private val registeredMemberUseCase: CheckRegisteredMemberUseCase,
) : FlowBaseViewModel<ComposeTeamBottariItemStatusUiState, ComposeTeamBottariItemStatusUiEvent>(
        ComposeTeamBottariItemStatusUiState(),
    ) {
    private val teamBottariId: Long =
        stateHandle[KEY_BOTTARI_ID] ?: error(ERROR_REQUIRE_BOTTARI_ID)

    val debouncedSendRemindByItem: (TeamBottariUiModelStatus) -> Unit =
        debounce(
            timeMillis = DEBOUNCE_DELAY,
            coroutineScope = viewModelScope,
        ) { item -> sendRemindByItem(item) }

    init {
        fetchMemberNickname()
        fetchTeamStatus()
        handleEvent()
    }

    override fun onCleared() {
        super.onCleared()
        CoroutineScope(Dispatchers.IO).launch { disconnectTeamEventUseCase() }
    }

    fun fetchTeamStatus() {
        launch {
            updateState { copy(isLoading = true) }
            fetchTeamStatusUseCase(teamBottariId)
                .onSuccess { teamBottariStatus -> handleFetchTeamStatusSuccess(teamBottariStatus) }
                .onFailure { emitEvent(ComposeTeamBottariItemStatusUiEvent.FetchTeamBottariItemStatusFailure) }
        }.invokeOnCompletion { updateState { copy(isLoading = false, isFetched = true) } }
    }

    fun selectItem(item: TeamBottariUiModelStatus?) {
        updateState { copy(selectedProduct = item) }
    }

    private fun fetchMemberNickname() {
        launch {
            updateState { copy(isLoading = true) }
            registeredMemberUseCase()
                .onSuccess { updateState { copy(myNickname = it.name.orEmpty()) } }
                .onFailure { emitEvent(ComposeTeamBottariItemStatusUiEvent.FetchTeamBottariItemStatusFailure) }
        }.invokeOnCompletion { updateState { copy(isLoading = false) } }
    }

    private fun sendRemindByItem(selectedProduct: TeamBottariUiModelStatus) {
        val itemId = selectedProduct.id
        val itemType = selectedProduct.type.toTypeString()

        launch {
            sendRemindByItemUseCase(itemId, itemType)
                .onSuccess {
                    updateState { copy(selectedProduct = null) }
                    emitEvent(ComposeTeamBottariItemStatusUiEvent.SendRemindSuccess)
                }.onFailure {
                    updateState { copy(selectedProduct = null) }
                    emitEvent(ComposeTeamBottariItemStatusUiEvent.SendRemindFailure)
                }
        }
    }

    @OptIn(FlowPreview::class)
    private fun handleEvent() {
        launch {
            connectTeamEventUseCase()
                .filterIsInstance<EventState.OnEvent>()
                .map { event -> event.data }
                .debounce(DEBOUNCE_DELAY)
                .onEach { fetchTeamStatus() }
                .launchIn(this)
        }
    }

    private fun handleFetchTeamStatusSuccess(teamBottariStatus: TeamBottariStatus) {
        val sharedItems =
            teamBottariStatus.sharedItems.map {
                TeamBottariUiModelStatus.fromDomain(
                    it,
                    BottariItemTypeUiModel.SHARED,
                )
            }
        val assignedItems =
            teamBottariStatus.assignedItems.map {
                TeamBottariUiModelStatus.fromDomain(
                    it,
                    BottariItemTypeUiModel.ASSIGNED(),
                )
            }

        updateState {
            copy(
                selectedProduct = selectedProduct,
                items = sharedItems + assignedItems,
            )
        }
    }

    companion object {
        const val KEY_BOTTARI_ID = "KEY_BOTTARI_ID"
        private const val ERROR_REQUIRE_BOTTARI_ID = "[ERROR] 보따리 ID가 존재하지 않습니다."
        private const val DEBOUNCE_DELAY = 300L
    }
}
