package com.bottari.presentation.view.edit.team.item.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.model.bottari.personal.BottariItemTypeUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TeamItemEditViewModel @Inject constructor(
    stateHandle: SavedStateHandle,
) : BaseViewModel<TeamItemEditUiState, TeamItemEditUiEvent>(
        TeamItemEditUiState(
            currentTabType = stateHandle[KEY_TAB_TYPE] ?: error(ERROR_TYPE_NULL),
        ),
    ) {
    private val _createItemEvent: MutableLiveData<TeamItemEditUiEvent?> = MutableLiveData()
    val createItemEvent: LiveData<TeamItemEditUiEvent?> get() = _createItemEvent

    fun updateInput(input: String) {
        if (currentState.itemInputText == input) return
        updateState { copy(itemInputText = input) }
    }

    fun updateSendCondition(sendCondition: Boolean) {
        if (currentState.sendCondition == sendCondition) return
        updateState { copy(sendCondition = sendCondition) }
    }

    fun updateIsAlreadyExistState(isAlreadyExist: Boolean) {
        if (currentState.isAlreadyExist == isAlreadyExist) return
        updateState { copy(isAlreadyExist = isAlreadyExist) }
    }

    fun updateTabType(type: BottariItemTypeUiModel) = updateState { copy(currentTabType = type) }

    fun createItem() {
        when (currentState.currentTabType) {
            BottariItemTypeUiModel.SHARED -> emitCreateEvent(TeamItemEditUiEvent.CreateTeamSharedItem)
            BottariItemTypeUiModel.PERSONAL -> emitCreateEvent(TeamItemEditUiEvent.CreateTeamPersonalItem)
            is BottariItemTypeUiModel.ASSIGNED -> emitCreateEvent(TeamItemEditUiEvent.CreateTeamAssignedItem)
        }
        updateState { copy(itemInputText = EMPTY_INPUT) }
        resetCreateEvent()
    }

    private fun emitCreateEvent(event: TeamItemEditUiEvent) {
        _createItemEvent.value = event
    }

    private fun resetCreateEvent() {
        _createItemEvent.value = null
    }

    companion object {
        const val KEY_TAB_TYPE = "KEY_TAB_TYPE"
        private const val ERROR_TYPE_NULL = "[ERROR] type이 null입니다"
        private const val EMPTY_INPUT = ""
    }
}
