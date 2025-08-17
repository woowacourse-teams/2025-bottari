package com.bottari.presentation.view.edit.team.item.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.model.BottariItemTypeUiModel

class TeamItemEditViewModel(
    stateHandle: SavedStateHandle,
) : BaseViewModel<TeamItemEditUiState, TeamItemEditUiEvent>(
        TeamItemEditUiState(
            currentTabType = stateHandle[KEY_TAB_TYPE] ?: error(ERROR_TYPE_NULL),
        ),
    ) {
    fun updateInput(input: String) = updateState { copy(itemInputText = input) }

    fun updateIsAlreadyExistState(isAlreadyExist: Boolean) {
        if (currentState.isAlreadyExist == isAlreadyExist) return
        updateState { copy(isAlreadyExist = isAlreadyExist) }
    }

    fun updateTabType(type: BottariItemTypeUiModel) = updateState { copy(currentTabType = type) }

    fun createItem() {
        when (currentState.currentTabType) {
            BottariItemTypeUiModel.SHARED -> emitEvent(TeamItemEditUiEvent.CreateTeamSharedItem)
            BottariItemTypeUiModel.PERSONAL -> emitEvent(TeamItemEditUiEvent.CreateTeamPersonalItem)
            is BottariItemTypeUiModel.ASSIGNED -> emitEvent(TeamItemEditUiEvent.CreateTeamAssignedItem)
        }
    }

    companion object {
        private const val KEY_TAB_TYPE = "KEY_TAB_TYPE"
        private const val ERROR_TYPE_NULL = "[ERROR] type이 null입니다"

        fun Factory(initialTabType: BottariItemTypeUiModel): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val stateHandle = createSavedStateHandle()
                    stateHandle[KEY_TAB_TYPE] = initialTabType
                    TeamItemEditViewModel(stateHandle)
                }
            }
    }
}
