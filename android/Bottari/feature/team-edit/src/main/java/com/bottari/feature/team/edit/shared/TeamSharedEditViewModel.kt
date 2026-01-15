package com.bottari.feature.team.edit.shared

import com.bottari.core.domain.model.event.EventData
import com.bottari.core.domain.model.event.EventState
import com.bottari.core.domain.model.team.bottari.item.TeamBottariItemType
import com.bottari.core.domain.usecase.event.ConnectTeamEventUseCase
import com.bottari.core.domain.usecase.team.CreateTeamSharedItemUseCase
import com.bottari.core.domain.usecase.team.DeleteTeamBottariItemUseCase
import com.bottari.core.domain.usecase.team.FetchTeamSharedItemsUseCase
import com.bottari.core.ui.base.FlowBaseViewModel
import com.bottari.core.ui.model.bottari.BottariItemUiModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map

@HiltViewModel(assistedFactory = TeamSharedEditViewModel.Factory::class)
class TeamSharedEditViewModel @AssistedInject constructor(
    @Assisted private val bottariId: Long,
    private val fetchTeamSharedItemsUseCase: FetchTeamSharedItemsUseCase,
    private val createTeamSharedItemUseCase: CreateTeamSharedItemUseCase,
    private val deleteTeamBottariItemUseCase: DeleteTeamBottariItemUseCase,
    private val connectTeamEventUseCase: ConnectTeamEventUseCase,
) : FlowBaseViewModel<TeamSharedEditUiState, TeamSharedEditUiEvent>(
        TeamSharedEditUiState(),
    ) {
    init {
        fetchPersonalItems()
        handleEvent()
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
                .onFailure { emitEvent(TeamSharedEditUiEvent.CreateItemFailure) }
                .onSuccess {
                    fetchPersonalItems()
                    updateState { copy(inputText = "") }
                }
        }.invokeOnCompletion { updateState { copy(isLoading = false) } }
    }

    fun deleteItem(itemId: Long) {
        updateState { copy(isLoading = true) }

        launch {
            deleteTeamBottariItemUseCase(itemId, TeamBottariItemType.SHARED)
                .onSuccess { fetchPersonalItems() }
                .onFailure { emitEvent(TeamSharedEditUiEvent.DeleteItemFailure) }
        }.invokeOnCompletion { updateState { copy(isLoading = false) } }
    }

    @OptIn(FlowPreview::class)
    private fun handleEvent() {
        launch {
            connectTeamEventUseCase()
                .filterIsInstance<EventState.OnEvent>()
                .map { event -> event.data }
                .filterNot { eventData -> eventData.shouldIgnore() }
                .debounce(DEBOUNCE_DELAY)
                .collect { fetchPersonalItems() }
        }
    }

    private fun fetchPersonalItems() {
        updateState { copy(isLoading = true) }

        launch {
            fetchTeamSharedItemsUseCase(bottariId)
                .onSuccess { items ->
                    updateState {
                        copy(
                            sharedItems = items.map(BottariItemUiModel::fromDomain),
                            isFetched = true,
                        )
                    }
                }.onFailure {
                    updateState { copy(isFetched = false) }
                    emitEvent(TeamSharedEditUiEvent.FetchTeamSharedItemsFailure)
                }
        }.invokeOnCompletion { updateState { copy(isLoading = false) } }
    }

    private fun EventData.shouldIgnore(): Boolean =
        when (this) {
            is EventData.SharedItemInfoCreate,
            is EventData.SharedItemInfoDelete,
            is EventData.TeamMemberCreate,
            is EventData.TeamMemberDelete,
            -> false

            else -> true
        }

    @AssistedFactory
    interface Factory {
        fun create(bottariId: Long): TeamSharedEditViewModel
    }

    companion object {
        private const val DEBOUNCE_DELAY = 300L
    }
}
