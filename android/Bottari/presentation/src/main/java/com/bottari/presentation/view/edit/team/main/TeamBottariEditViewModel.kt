package com.bottari.presentation.view.edit.team.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.usecase.CommonUseCaseProvider
import com.bottari.di.usecase.TeamBottariUseCaseProvider
import com.bottari.domain.model.event.EventData
import com.bottari.domain.model.event.EventState
import com.bottari.domain.model.team.bottari.TeamBottariDetail
import com.bottari.domain.usecase.event.ConnectTeamEventUseCase
import com.bottari.domain.usecase.event.DisconnectTeamEventUseCase
import com.bottari.domain.usecase.team.FetchTeamBottariDetailUseCase
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.model.alarm.AlarmUiModel
import com.bottari.presentation.model.bottari.BottariItemUiModel
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

class TeamBottariEditViewModel(
    createHandle: SavedStateHandle,
    private val fetchTeamBottariDetailUseCase: FetchTeamBottariDetailUseCase,
    private val connectTeamEventUseCase: ConnectTeamEventUseCase,
    private val disconnectTeamEventUseCase: DisconnectTeamEventUseCase,
) : BaseViewModel<TeamBottariEditUiState, TeamBottariEditUiEvent>(TeamBottariEditUiState()) {
    private val bottariId: Long = createHandle[KEY_BOTTARI_ID] ?: error(ERROR_BOTTARI_ID)

    init {
        handleEvent()
    }

    override fun onCleared() {
        super.onCleared()
        CoroutineScope(Dispatchers.IO).launch { disconnectTeamEventUseCase() }
    }

    fun fetchTeamBottariDetail() {
        updateState { copy(isLoading = true) }

        launch {
            fetchTeamBottariDetailUseCase(bottariId)
                .onSuccess { handleFetchTeamBottariDetail(it) }
                .onFailure { emitEvent(TeamBottariEditUiEvent.FetchTeamBottariDetailFailure) }

            updateState { copy(isLoading = false, isFetched = true) }
        }
    }

    @OptIn(FlowPreview::class)
    private fun handleEvent() {
        launch {
            connectTeamEventUseCase(bottariId)
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
                personalItems =
                    teamBottariDetail.personalItems.map {
                        BottariItemUiModel.fromDomain(
                            it,
                        )
                    },
                assignedItems =
                    teamBottariDetail.assignedItems.map {
                        BottariItemUiModel.fromDomain(
                            it,
                        )
                    },
                sharedItems = teamBottariDetail.sharedItems.map { BottariItemUiModel.fromDomain(it) },
                alarm = alarmUi,
                alarmSwitchState = alarmUi?.isActive ?: false,
            )
        }
    }

    companion object {
        private const val KEY_BOTTARI_ID = "KEY_BOTTARI_ID"
        private const val ERROR_BOTTARI_ID = "[ERROR] 팀 보따리 ID를 찾을 수 없습니다"
        private const val DEBOUNCE_DELAY = 300L

        fun Factory(bottariId: Long): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val stateHandle = createSavedStateHandle()
                    stateHandle[KEY_BOTTARI_ID] = bottariId
                    TeamBottariEditViewModel(
                        stateHandle,
                        TeamBottariUseCaseProvider.fetchTeamBottariDetailUseCase,
                        CommonUseCaseProvider.connectTeamEventUseCase,
                        CommonUseCaseProvider.disconnectTeamEventUseCase,
                    )
                }
            }
    }
}
