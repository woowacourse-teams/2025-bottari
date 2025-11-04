package com.bottari.presentation.compose.edit.team.personal

import androidx.lifecycle.SavedStateHandle
import com.bottari.domain.model.team.bottari.item.TeamBottariItemType
import com.bottari.domain.usecase.team.CreateTeamPersonalItemUseCase
import com.bottari.domain.usecase.team.DeleteTeamBottariItemUseCase
import com.bottari.domain.usecase.team.FetchTeamPersonalItemsUseCase
import com.bottari.presentation.common.base.FlowBaseViewModel
import com.bottari.presentation.model.bottari.BottariItemUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TeamPersonalItemEditViewModel @Inject constructor(
    stateHandle: SavedStateHandle,
    private val fetchTeamPersonalItemsUseCase: FetchTeamPersonalItemsUseCase,
    private val createTeamPersonalItemUseCase: CreateTeamPersonalItemUseCase,
    private val deleteTeamBottariItemUseCase: DeleteTeamBottariItemUseCase,
) : FlowBaseViewModel<TeamPersonalItemEditUiState, TeamPersonalItemEditUiEvent>(
        TeamPersonalItemEditUiState(),
    ) {
    private val bottariId: Long = stateHandle[KEY_BOTTARI_ID] ?: error(ERROR_REQUIRE_BOTTARI_ID)

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
            createTeamPersonalItemUseCase(bottariId, currentState.inputText)
                .onFailure { emitEvent(TeamPersonalItemEditUiEvent.CreateItemFailureCompose) }
                .onSuccess {
                    fetchPersonalItems()
                    updateState { copy(inputText = "") }
                }

            updateState { copy(isLoading = false) }
        }
    }

    fun deleteItem(itemId: Long) {
        updateState { copy(isLoading = true) }

        launch {
            deleteTeamBottariItemUseCase(itemId, TeamBottariItemType.PERSONAL)
                .onSuccess { fetchPersonalItems() }
                .onFailure { emitEvent(TeamPersonalItemEditUiEvent.DeleteItemFailureCompose) }

            updateState { copy(isLoading = false) }
        }
    }

    private fun fetchPersonalItems() {
        updateState { copy(isLoading = true) }

        launch {
            fetchTeamPersonalItemsUseCase(bottariId)
                .onSuccess { items -> updateState { copy(personalItems = items.map { BottariItemUiModel.fromDomain(it) }) } }
                .onFailure { emitEvent(TeamPersonalItemEditUiEvent.FetchTeamPersonalItemsFailure) }

            updateState { copy(isLoading = false, isFetched = true) }
        }
    }

    companion object {
        const val KEY_BOTTARI_ID = "KEY_BOTTARI_ID"
        private const val ERROR_REQUIRE_BOTTARI_ID = "[ERROR] 보따리 ID가 존재하지 않습니다"
    }
}
