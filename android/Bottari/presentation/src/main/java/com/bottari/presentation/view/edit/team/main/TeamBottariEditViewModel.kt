package com.bottari.presentation.view.edit.team.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.presentation.common.base.BaseViewModel

class TeamBottariEditViewModel(
    createHandle: SavedStateHandle,
) : BaseViewModel<TeamBottariEditUiState, TeamBottariEditUiEvent>(TeamBottariEditUiState()) {
    private val bottariId: Long = createHandle[KEY_BOTTARI_ID] ?: error(ERROR_BOTTARI_ID)

    init {
        fetchTeamBottari()
    }

    private fun fetchTeamBottari() {
        updateState { copy(isFetched = true) }
    }

    companion object {
        private const val KEY_BOTTARI_ID = "KEY_BOTTARI_ID"
        private const val ERROR_BOTTARI_ID = "[ERROR] 팀 보따리 ID를 찾을 수 없습니다"

        fun Factory(bottariId: Long): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val stateHandle = createSavedStateHandle()
                    stateHandle[KEY_BOTTARI_ID] = bottariId
                    TeamBottariEditViewModel(stateHandle)
                }
            }
    }
}
