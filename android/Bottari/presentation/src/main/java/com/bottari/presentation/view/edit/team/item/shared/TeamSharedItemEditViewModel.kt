package com.bottari.presentation.view.edit.team.item.shared

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.model.bottari.BottariItemType
import com.bottari.domain.usecase.team.CreateTeamSharedItemUseCase
import com.bottari.domain.usecase.team.DeleteTeamBottariItemUseCase
import com.bottari.domain.usecase.team.FetchTeamSharedItemsUseCase
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.mapper.TeamBottariMapper.toUiModel

class TeamSharedItemEditViewModel(
    stateHandle: SavedStateHandle,
    private val fetchTeamSharedItemsUseCase: FetchTeamSharedItemsUseCase,
    private val createTeamSharedItemUseCase: CreateTeamSharedItemUseCase,
    private val deleteTeamBottariItemUseCase: DeleteTeamBottariItemUseCase,
) : BaseViewModel<TeamSharedItemEditUiState, TeamSharedItemEditEvent>(
        TeamSharedItemEditUiState(),
    ) {
    private val bottariId: Long = stateHandle[KEY_BOTTARI_ID] ?: error(ERROR_BOTTARI_ID)

    init {
        fetchPersonalItems()
    }

    fun updateInput(input: String) {
        if (currentState.inputText == input) return
        updateState { copy(inputText = input) }
    }

    fun createItem() {
        if (currentState.isAlreadyExist) return
        updateState { copy(isLoading = true) }

        launch {
            createTeamSharedItemUseCase(bottariId, currentState.inputText)
                .onFailure { emitEvent(TeamSharedItemEditEvent.CreateItemFailure) }
                .onSuccess {
                    fetchPersonalItems()
                    emitEvent(TeamSharedItemEditEvent.CreateItemSuccuss)
                }

            updateState { copy(isLoading = false) }
        }
    }

    fun deleteItem(itemId: Long) {
        updateState { copy(isLoading = true) }

        launch {
            deleteTeamBottariItemUseCase(itemId, BottariItemType.SHARED)
                .onSuccess { fetchPersonalItems() }
                .onFailure { emitEvent(TeamSharedItemEditEvent.DeleteItemFailure) }

            updateState { copy(isLoading = false) }
        }
    }

    private fun fetchPersonalItems() {
        updateState { copy(isLoading = true) }

        launch {
            fetchTeamSharedItemsUseCase(bottariId)
                .onSuccess { items -> updateState { copy(sharedItems = items.map { it.toUiModel() }) } }
                .onFailure { emitEvent(TeamSharedItemEditEvent.FetchTeamPersonalItemsFailure) }

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
                    TeamSharedItemEditViewModel(
                        stateHandle,
                        UseCaseProvider.fetchTeamSharedItemsUseCase,
                        UseCaseProvider.createTeamSharedItemUseCase,
                        UseCaseProvider.deleteTeamBottariItemUseCase,
                    )
                }
            }
    }
}
