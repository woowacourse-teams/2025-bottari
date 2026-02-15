package com.bottari.feature.team.edit.personal

import com.bottari.core.domain.model.team.bottari.item.TeamBottariItemType
import com.bottari.core.domain.usecase.team.CreateTeamPersonalItemUseCase
import com.bottari.core.domain.usecase.team.DeleteTeamBottariItemUseCase
import com.bottari.core.domain.usecase.team.FetchTeamPersonalItemsUseCase
import com.bottari.core.ui.base.BaseViewModel
import com.bottari.core.ui.model.bottari.BottariItemUiModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = TeamPersonalEditViewModel.Factory::class)
class TeamPersonalEditViewModel @AssistedInject constructor(
    @Assisted private val bottariId: Long,
    private val fetchTeamPersonalItemsUseCase: FetchTeamPersonalItemsUseCase,
    private val createTeamPersonalItemUseCase: CreateTeamPersonalItemUseCase,
    private val deleteTeamBottariItemUseCase: DeleteTeamBottariItemUseCase,
) : BaseViewModel<TeamPersonalEditUiState, TeamPersonalEditUiEvent>(
        TeamPersonalEditUiState(),
    ) {
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
            createTeamPersonalItemUseCase(bottariId = bottariId, name = currentState.inputText)
                .onSuccess {
                    fetchPersonalItems()
                    updateState { copy(inputText = "") }
                }.onFailure {
                    updateState { copy(isLoading = false) }
                    emitEvent(TeamPersonalEditUiEvent.CreateItemFailureCompose)
                }
        }
    }

    fun deleteItem(itemId: Long) {
        updateState { copy(isLoading = true) }

        launch {
            deleteTeamBottariItemUseCase(itemId = itemId, type = TeamBottariItemType.PERSONAL)
                .onSuccess {
                    fetchPersonalItems()
                }.onFailure {
                    updateState { copy(isLoading = false) }
                    emitEvent(TeamPersonalEditUiEvent.DeleteItemFailureCompose)
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
                    emitEvent(TeamPersonalEditUiEvent.FetchTeamPersonalItemsFailure)
                }
        }.invokeOnCompletion { updateState { copy(isLoading = false) } }
    }

    @AssistedFactory
    interface Factory {
        fun create(bottariId: Long): TeamPersonalEditViewModel
    }
}
