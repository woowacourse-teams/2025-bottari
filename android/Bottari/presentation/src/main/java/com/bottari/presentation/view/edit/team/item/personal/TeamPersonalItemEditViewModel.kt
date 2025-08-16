package com.bottari.presentation.view.edit.team.item.personal

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.usecase.team.CreateTeamPersonalItemUseCase
import com.bottari.domain.usecase.team.FetchTeamPersonalItemsUseCase
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.mapper.TeamBottariMapper.toUiModel

class TeamPersonalItemEditViewModel(
    stateHandle: SavedStateHandle,
    private val fetchTeamPersonalItemsUseCase: FetchTeamPersonalItemsUseCase,
    private val createTeamPersonalItemUseCase: CreateTeamPersonalItemUseCase,
) : BaseViewModel<TeamPersonalItemEditUiState, TeamPersonalItemEditEvent>(
        TeamPersonalItemEditUiState(),
    ) {
    private val bottariId: Long = stateHandle[KEY_BOTTARI_ID] ?: error(ERROR_BOTTARI_ID)

    init {
        fetchPersonalItems()
    }

    fun updateInput(input: String) = updateState { copy(inputText = input) }

    fun createItem() {
        if (currentState.isAlreadyExist) return
        updateState { copy(isLoading = true) }

        launch {
            createTeamPersonalItemUseCase(bottariId, currentState.inputText)
                .onSuccess { fetchPersonalItems() }
                .onFailure { emitEvent(TeamPersonalItemEditEvent.AddItemFailure) }

            updateState { copy(isLoading = false) }
        }
    }

    private fun fetchPersonalItems() {
        updateState { copy(isLoading = true) }

        launch {
            fetchTeamPersonalItemsUseCase(bottariId)
                .onSuccess { items -> updateState { copy(personalItems = items.map { it.toUiModel() }) } }
                .onFailure { emitEvent(TeamPersonalItemEditEvent.FetchTeamPersonalItemsFailure) }

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
                    TeamPersonalItemEditViewModel(
                        stateHandle,
                        UseCaseProvider.fetchTeamPersonalItemsUseCase,
                        UseCaseProvider.createTeamPersonalItemUseCase,
                    )
                }
            }
    }
}
