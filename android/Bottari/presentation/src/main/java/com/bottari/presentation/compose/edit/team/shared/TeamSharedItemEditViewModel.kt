package com.bottari.presentation.compose.edit.team.shared

import androidx.lifecycle.SavedStateHandle
import com.bottari.domain.model.event.EventData
import com.bottari.domain.model.event.EventState
import com.bottari.domain.model.team.bottari.item.TeamBottariItemType
import com.bottari.domain.usecase.event.ConnectTeamEventUseCase
import com.bottari.domain.usecase.team.CreateTeamSharedItemUseCase
import com.bottari.domain.usecase.team.DeleteTeamBottariItemUseCase
import com.bottari.domain.usecase.team.FetchTeamSharedItemsUseCase
import com.bottari.presentation.common.base.FlowBaseViewModel
import com.bottari.presentation.model.bottari.BottariItemUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class TeamSharedItemEditViewModel @Inject constructor(
    stateHandle: SavedStateHandle,
    private val fetchTeamSharedItemsUseCase: FetchTeamSharedItemsUseCase,
    private val createTeamSharedItemUseCase: CreateTeamSharedItemUseCase,
    private val deleteTeamBottariItemUseCase: DeleteTeamBottariItemUseCase,
    private val connectTeamEventUseCase: ConnectTeamEventUseCase,
) : FlowBaseViewModel<TeamSharedItemEditUiState, TeamSharedItemEditEvent>(
        TeamSharedItemEditUiState(),
    ) {
    private val bottariId: Long = stateHandle[KEY_BOTTARI_ID] ?: error(ERROR_REQUIRE_BOTTARI_ID)

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
                .onFailure { emitEvent(TeamSharedItemEditEvent.CreateItemFailure) }
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
            deleteTeamBottariItemUseCase(itemId, TeamBottariItemType.SHARED)
                .onSuccess { fetchPersonalItems() }
                .onFailure { emitEvent(TeamSharedItemEditEvent.DeleteItemFailure) }

            updateState { copy(isLoading = false) }
        }
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
                .onSuccess { items -> updateState { copy(sharedItems = items.map(BottariItemUiModel::fromDomain)) } }
                .onFailure { emitEvent(TeamSharedItemEditEvent.FetchTeamSharedItemsFailure) }

            updateState { copy(isLoading = false, isFetched = true) }
        }
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

    companion object {
        const val KEY_BOTTARI_ID = "KEY_BOTTARI_ID"
        private const val ERROR_REQUIRE_BOTTARI_ID = "[ERROR] 보따리 ID가 존재하지 않습니다"
        private const val DEBOUNCE_DELAY = 300L
    }
}
