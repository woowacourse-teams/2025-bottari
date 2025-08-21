package com.bottari.presentation.view.edit.team.item.shared

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.model.bottari.BottariItemType
import com.bottari.domain.model.event.EventData
import com.bottari.domain.model.event.EventState
import com.bottari.domain.usecase.event.ConnectTeamEventUseCase
import com.bottari.domain.usecase.event.DisconnectTeamEventUseCase
import com.bottari.domain.usecase.team.CreateTeamSharedItemUseCase
import com.bottari.domain.usecase.team.DeleteTeamBottariItemUseCase
import com.bottari.domain.usecase.team.FetchTeamSharedItemsUseCase
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.mapper.TeamBottariMapper.toUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class TeamSharedItemEditViewModel(
    stateHandle: SavedStateHandle,
    private val fetchTeamSharedItemsUseCase: FetchTeamSharedItemsUseCase,
    private val createTeamSharedItemUseCase: CreateTeamSharedItemUseCase,
    private val deleteTeamBottariItemUseCase: DeleteTeamBottariItemUseCase,
    private val connectTeamEventUseCase: ConnectTeamEventUseCase,
) : BaseViewModel<TeamSharedItemEditUiState, TeamSharedItemEditEvent>(
        TeamSharedItemEditUiState(),
    ) {
    private val bottariId: Long = stateHandle[KEY_BOTTARI_ID] ?: error(ERROR_BOTTARI_ID)

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

    @OptIn(FlowPreview::class)
    private fun handleEvent() {
        launch {
            connectTeamEventUseCase(bottariId)
                .filterIsInstance<EventState.OnEvent>()
                .map { event -> event.data }
                .filterNot { eventData -> eventData.shouldIgnore() }
                .debounce(DEBOUNCE_DELAY)
                .onEach { fetchPersonalItems() }
                .launchIn(this)
        }
    }

    private fun fetchPersonalItems() {
        updateState { copy(isLoading = true) }

        launch {
            fetchTeamSharedItemsUseCase(bottariId)
                .onSuccess { items -> updateState { copy(sharedItems = items.map { it.toUiModel() }) } }
                .onFailure { emitEvent(TeamSharedItemEditEvent.FetchTeamSharedItemsFailure) }

            updateState { copy(isLoading = false, isFetched = true) }
        }
    }

    private fun EventData.shouldIgnore(): Boolean =
        when (this) {
            is EventData.SharedItemChange,
            is EventData.SharedItemInfoCreate,
            is EventData.SharedItemInfoDelete,
            -> false

            else -> true
        }

    companion object {
        private const val KEY_BOTTARI_ID = "KEY_BOTTARI_ID"
        private const val ERROR_BOTTARI_ID = "[ERROR] bottariId가 존재하지 않습니다"
        private const val DEBOUNCE_DELAY = 300L

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
                        UseCaseProvider.connectTeamEventUseCase,
                    )
                }
            }
    }
}
