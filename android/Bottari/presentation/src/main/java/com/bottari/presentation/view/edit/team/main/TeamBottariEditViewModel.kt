package com.bottari.presentation.view.edit.team.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.model.team.TeamBottariDetail
import com.bottari.domain.usecase.team.FetchTeamBottariDetailUseCase
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.mapper.AlarmMapper.toUiModel
import com.bottari.presentation.mapper.BottariMapper.toUiModel

class TeamBottariEditViewModel(
    createHandle: SavedStateHandle,
    private val fetchTeamBottariDetailUseCase: FetchTeamBottariDetailUseCase,
) : BaseViewModel<TeamBottariEditUiState, TeamBottariEditUiEvent>(TeamBottariEditUiState()) {
    private val bottariId: Long = createHandle[KEY_BOTTARI_ID] ?: error(ERROR_BOTTARI_ID)

    fun fetchTeamBottariDetail() {
        updateState { copy(isLoading = true) }

        launch {
            fetchTeamBottariDetailUseCase(bottariId)
                .onSuccess { handleFetchTeamBottariDetail(it) }
                .onFailure { emitEvent(TeamBottariEditUiEvent.FetchTeamBottariDetailFailure) }

            updateState { copy(isLoading = false, isFetched = true) }
        }
    }

    private fun handleFetchTeamBottariDetail(teamBottariDetail: TeamBottariDetail) {
        updateState {
            copy(
                bottariTitle = teamBottariDetail.title,
                personalItems = teamBottariDetail.personalItems.map { it.toUiModel() },
                assignedItems = teamBottariDetail.assignedItems.map { it.toUiModel() },
                sharedItems = teamBottariDetail.sharedItems.map { it.toUiModel() },
                alarm = teamBottariDetail.alarm?.toUiModel(),
                alarmSwitchState = teamBottariDetail.alarm?.isActive ?: false,
            )
        }
    }

    companion object {
        private const val KEY_BOTTARI_ID = "KEY_BOTTARI_ID"
        private const val ERROR_BOTTARI_ID = "[ERROR] 팀 보따리 ID를 찾을 수 없습니다"

        fun Factory(bottariId: Long): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val stateHandle = createSavedStateHandle()
                    stateHandle[KEY_BOTTARI_ID] = bottariId
                    TeamBottariEditViewModel(
                        stateHandle,
                        UseCaseProvider.fetchTeamBottariDetailUseCase,
                    )
                }
            }
    }
}
