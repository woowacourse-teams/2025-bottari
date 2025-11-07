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
class TeamPersonalEditViewModel @Inject constructor(
    stateHandle: SavedStateHandle,
    private val fetchTeamPersonalItemsUseCase: FetchTeamPersonalItemsUseCase,
    private val createTeamPersonalItemUseCase: CreateTeamPersonalItemUseCase,
    private val deleteTeamBottariItemUseCase: DeleteTeamBottariItemUseCase,
) : FlowBaseViewModel<TeamPersonalEditUiState, TeamPersonalEditEditUiEvent>(
        TeamPersonalEditUiState(),
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
                .onSuccess {
                    fetchPersonalItems()
                    updateState { copy(inputText = "") }
                }.onFailure {
                    updateState { copy(isLoading = false) }
                    emitEvent(TeamPersonalEditEditUiEvent.CreateItemFailureCompose)
                }
        }
    }

    fun deleteItem(itemId: Long) {
        updateState { copy(isLoading = true) }

        launch {
            deleteTeamBottariItemUseCase(itemId, TeamBottariItemType.PERSONAL)
                .onSuccess {
                    fetchPersonalItems()
                }.onFailure {
                    updateState { copy(isLoading = false) }
                    emitEvent(TeamPersonalEditEditUiEvent.DeleteItemFailureCompose)
                }
        }
    }

    private fun fetchPersonalItems() {
        updateState { copy(isLoading = true) }

        launch {
            fetchTeamPersonalItemsUseCase(bottariId)
                .onSuccess { items ->
                    updateState {
                        copy(
                            personalItems =
                                items.map(BottariItemUiModel::fromDomain),
                            isFetched = true,
                        )
                    }
                }.onFailure {
                    updateState { copy(isFetched = false) }
                    emitEvent(TeamPersonalEditEditUiEvent.FetchTeamPersonalItemsFailure)
                }
        }.invokeOnCompletion { updateState { copy(isLoading = false) } }
    }

    companion object {
        const val KEY_BOTTARI_ID = "KEY_BOTTARI_ID"
        private const val ERROR_REQUIRE_BOTTARI_ID = "[ERROR] 보따리 ID가 존재하지 않습니다"
    }
}
