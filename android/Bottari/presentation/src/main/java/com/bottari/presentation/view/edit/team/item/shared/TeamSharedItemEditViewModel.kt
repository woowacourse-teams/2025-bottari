package com.bottari.presentation.view.edit.team.item.shared

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.bottari.domain.model.event.EventData
import com.bottari.domain.model.event.EventState
import com.bottari.domain.model.team.bottari.item.TeamBottariItemType
import com.bottari.domain.usecase.event.ConnectTeamEventUseCase
import com.bottari.domain.usecase.team.CreateTeamSharedItemUseCase
import com.bottari.domain.usecase.team.DeleteTeamBottariItemUseCase
import com.bottari.domain.usecase.team.FetchTeamSharedItemsUseCase
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.model.bottari.BottariItemUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamSharedItemEditViewModel @Inject constructor(
    stateHandle: SavedStateHandle,
    private val fetchTeamSharedItemsUseCase: FetchTeamSharedItemsUseCase,
    private val createTeamSharedItemUseCase: CreateTeamSharedItemUseCase,
    private val deleteTeamBottariItemUseCase: DeleteTeamBottariItemUseCase,
    private val connectTeamEventUseCase: ConnectTeamEventUseCase,
) : BaseViewModel<TeamSharedItemEditUiState, TeamSharedItemEditEvent>(
        TeamSharedItemEditUiState(),
    ) {
    private val bottariId: Long = stateHandle[KEY_BOTTARI_ID] ?: error(ERROR_REQUIRE_BOTTARI_ID)

    private val debouncedJobs: MutableMap<Long, Job> = mutableMapOf()

    init {
        fetchSharedItems()
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
                    fetchSharedItems()
                    emitEvent(TeamSharedItemEditEvent.CreateItemSuccuss)
                }

            updateState { copy(isLoading = false) }
        }
    }

    fun requestDeleteItem(itemId: Long) {
        updateState { copy(sharedItems = sharedItems.filterNot { it.id == itemId }) }

        debouncedJobs[itemId]?.cancel()
        debouncedJobs[itemId] =
            viewModelScope
                .launch {
                    delay(DEBOUNCE_DELAY)
                    performDeleteItem(itemId)
                }.also { job -> job.invokeOnCompletion { debouncedJobs.remove(itemId) } }
    }

    private fun performDeleteItem(itemId: Long) {
        launch {
            deleteTeamBottariItemUseCase(itemId, TeamBottariItemType.SHARED)
                .onFailure {
                    fetchSharedItems()
                    emitEvent(TeamSharedItemEditEvent.DeleteItemFailure)
                }
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
                .onEach { fetchSharedItems() }
                .launchIn(this)
        }
    }

    private fun fetchSharedItems() {
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
