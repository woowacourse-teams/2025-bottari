package com.bottari.presentation.compose.edit.team.main

import androidx.lifecycle.SavedStateHandle
import com.bottari.domain.model.event.EventData
import com.bottari.domain.model.event.EventState
import com.bottari.domain.model.team.bottari.TeamBottariDetail
import com.bottari.domain.usecase.event.ConnectTeamEventUseCase
import com.bottari.domain.usecase.event.DisconnectTeamEventUseCase
import com.bottari.domain.usecase.team.FetchTeamBottariDetailUseCase
import com.bottari.presentation.common.base.FlowBaseViewModel
import com.bottari.presentation.model.alarm.AlarmUiModel
import com.bottari.presentation.model.bottari.BottariItemUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ComposeTeamBottariEditViewModel @Inject constructor(
    createHandle: SavedStateHandle,
    private val fetchTeamBottariDetailUseCase: FetchTeamBottariDetailUseCase,
    private val connectTeamEventUseCase: ConnectTeamEventUseCase,
    private val disconnectTeamEventUseCase: DisconnectTeamEventUseCase,
) : FlowBaseViewModel<ComposeTeamBottariEditUiState, ComposeTeamBottariEditUiEvent>(ComposeTeamBottariEditUiState()) {
    private val bottariId: Long = createHandle[KEY_BOTTARI_ID] ?: error(ERROR_REQUIRE_BOTTARI_ID)

    init {
        handleEvent()
        fetchTeamBottariDetail()
    }

    override fun onCleared() {
        super.onCleared()
        CoroutineScope(Dispatchers.IO).launch { disconnectTeamEventUseCase() }
    }

    private fun fetchTeamBottariDetail() {
        updateState { copy(isLoading = true) }

        launch {
            fetchTeamBottariDetailUseCase(bottariId)
                .onSuccess { handleFetchTeamBottariDetail(it) }
                .onFailure { emitEvent(ComposeTeamBottariEditUiEvent.FetchComposeTeamBottariDetailFailure) }

            updateState { copy(isLoading = false, isFetched = true) }
        }
    }

    @OptIn(FlowPreview::class)
    private fun handleEvent() {
        launch {
            connectTeamEventUseCase()
                .filterIsInstance<EventState.OnEvent>()
                .map { event -> event.data }
                .filterNot { eventData -> eventData.shouldIgnore() }
                .debounce(DEBOUNCE_DELAY)
                .onEach { fetchTeamBottariDetail() }
                .launchIn(this)
        }
    }

    private fun EventData.shouldIgnore(): Boolean =
        when (this) {
            is EventData.AssignedItemInfoCreate,
            is EventData.AssignedItemInfoDelete,
            is EventData.SharedItemInfoCreate,
            is EventData.SharedItemInfoDelete,
            -> false

            else -> true
        }

    private fun handleFetchTeamBottariDetail(teamBottariDetail: TeamBottariDetail) {
        val alarmUi = teamBottariDetail.bottari.alarm?.let { AlarmUiModel.fromDomain(it) }
        updateState {
            copy(
                bottariTitle = teamBottariDetail.bottari.title,
                personalItems = teamBottariDetail.personalItems.map(BottariItemUiModel::fromDomain),
                assignedItems = teamBottariDetail.assignedItems.map(BottariItemUiModel::fromDomain),
                sharedItems = teamBottariDetail.sharedItems.map(BottariItemUiModel::fromDomain),
                alarm = alarmUi,
                alarmSwitchState = alarmUi?.isActive ?: false,
            )
        }
    }

    companion object {
        const val KEY_BOTTARI_ID = "KEY_BOTTARI_ID"
        private const val ERROR_REQUIRE_BOTTARI_ID = "[ERROR] 팀 보따리 ID를 찾을 수 없습니다"
        private const val DEBOUNCE_DELAY = 300L
    }
}
