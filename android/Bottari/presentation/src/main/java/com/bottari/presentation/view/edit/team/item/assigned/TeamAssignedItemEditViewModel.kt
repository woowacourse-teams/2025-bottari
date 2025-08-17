package com.bottari.presentation.view.edit.team.item.assigned

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.model.bottari.BottariItemType
import com.bottari.domain.usecase.team.CreateTeamAssignedItemUseCase
import com.bottari.domain.usecase.team.DeleteTeamBottariItemUseCase
import com.bottari.domain.usecase.team.FetchTeamAssignedItemsUseCase
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.mapper.TeamBottariMapper.toUiModel

class TeamAssignedItemEditViewModel(
    stateHandle: SavedStateHandle,
    private val fetchTeamAssignedItemsUseCase: FetchTeamAssignedItemsUseCase,
    private val createTeamAssignedItemUseCase: CreateTeamAssignedItemUseCase,
    private val deleteTeamBottariItemUseCase: DeleteTeamBottariItemUseCase,
) : BaseViewModel<TeamAssignedItemEditUiState, TeamAssignedItemEditEvent>(
        TeamAssignedItemEditUiState(),
    ) {
    private val bottariId: Long = stateHandle[KEY_BOTTARI_ID] ?: error(ERROR_BOTTARI_ID)

    init {
        fetchAssignedItems()
    }

    fun updateInput(input: String) = updateState { copy(inputText = input) }

    fun createItem() {
        if (currentState.isAlreadyExist) return
        updateState { copy(isLoading = true) }

        launch {
            createTeamAssignedItemUseCase(bottariId, currentState.inputText, currentState.selectedMemberIds)
                .onSuccess { fetchAssignedItems() }
                .onFailure { emitEvent(TeamAssignedItemEditEvent.AddItemFailure) }

            updateState { copy(isLoading = false) }
        }
    }

    fun deleteItem(itemId: Long) {
        updateState { copy(isLoading = true) }

        launch {
            deleteTeamBottariItemUseCase(itemId, BottariItemType.PERSONAL)
                .onSuccess { fetchAssignedItems() }
                .onFailure { emitEvent(TeamAssignedItemEditEvent.DeleteItemFailure) }

            updateState { copy(isLoading = false) }
        }
    }

    private fun fetchAssignedItems() {
        updateState { copy(isLoading = true) }

        launch {
            fetchTeamAssignedItemsUseCase(bottariId)
                .onSuccess { items -> updateState { copy(assignedItems = items.map { it.toUiModel() }) } }
                .onFailure { emitEvent(TeamAssignedItemEditEvent.FetchTeamAssignedItemsFailure) }

            updateState { copy(isLoading = false, isFetched = true) }
        }
    }

    companion object {
        private const val KEY_BOTTARI_ID = "KEY_BOTTARI_ID"
        private const val ERROR_BOTTARI_ID = "[ERROR] bottariId가 존재하지 않습니다"

        fun Factory(bottariId: Long): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val stateHandle = createSavedStateHandle()
                    stateHandle[KEY_BOTTARI_ID] = bottariId
                    TeamAssignedItemEditViewModel(
                        stateHandle,
                        UseCaseProvider.fetchTeamAssignedItemsUseCase,
                        UseCaseProvider.createTeamAssignedItemUseCase,
                        UseCaseProvider.deleteTeamBottariItemUseCase,
                    )
                }
            }
    }
}
