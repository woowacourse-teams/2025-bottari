package com.bottari.feature.team.edit.main

import com.bottari.core.domain.model.event.EventData
import com.bottari.core.domain.model.event.EventState
import com.bottari.core.domain.model.team.bottari.TeamBottariDetail
import com.bottari.core.domain.usecase.event.ConnectTeamEventUseCase
import com.bottari.core.domain.usecase.event.DisconnectTeamEventUseCase
import com.bottari.core.domain.usecase.team.FetchTeamBottariDetailUseCase
import com.bottari.core.ui.base.FlowBaseViewModel
import com.bottari.core.ui.model.alarm.AlarmUiModel
import com.bottari.core.ui.model.bottari.BottariItemUiModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = TeamBottariEditViewModel.Factory::class)
class TeamBottariEditViewModel @AssistedInject constructor(
    @Assisted private val bottariId: Long,
    private val fetchTeamBottariDetailUseCase: FetchTeamBottariDetailUseCase,
    private val connectTeamEventUseCase: ConnectTeamEventUseCase,
    private val disconnectTeamEventUseCase: DisconnectTeamEventUseCase,
) : FlowBaseViewModel<TeamBottariEditUiState, TeamBottariEditUiEvent>(TeamBottariEditUiState()) {
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
                .onSuccess { teamBottariDetail ->
                    handleFetchTeamBottariDetail(teamBottariDetail)
                    updateState { copy(isFetched = true) }
                }.onFailure {
                    emitEvent(TeamBottariEditUiEvent.FetchTeamBottariDetailFailure)
                    updateState { copy(isFetched = false) }
                }
        }.invokeOnCompletion {
            updateState { copy(isLoading = false) }
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
                .collect { fetchTeamBottariDetail() }
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
        val alarmUi = teamBottariDetail.bottari.alarm?.let(AlarmUiModel::fromDomain)
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

    @AssistedFactory
    interface Factory {
        fun create(bottariId: Long): TeamBottariEditViewModel
    }

    companion object {
        private const val DEBOUNCE_DELAY = 300L
    }
}
