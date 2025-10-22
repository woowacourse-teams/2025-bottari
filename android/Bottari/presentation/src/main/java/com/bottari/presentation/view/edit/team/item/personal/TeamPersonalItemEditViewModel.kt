package com.bottari.presentation.view.edit.team.item.personal

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.bottari.domain.model.team.bottari.item.TeamBottariItemType
import com.bottari.domain.usecase.team.CreateTeamPersonalItemUseCase
import com.bottari.domain.usecase.team.DeleteTeamBottariItemUseCase
import com.bottari.domain.usecase.team.FetchTeamPersonalItemsUseCase
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.model.bottari.BottariItemUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamPersonalItemEditViewModel @Inject constructor(
    stateHandle: SavedStateHandle,
    private val fetchTeamPersonalItemsUseCase: FetchTeamPersonalItemsUseCase,
    private val createTeamPersonalItemUseCase: CreateTeamPersonalItemUseCase,
    private val deleteTeamBottariItemUseCase: DeleteTeamBottariItemUseCase,
) : BaseViewModel<TeamPersonalItemEditUiState, TeamPersonalItemEditEvent>(
        TeamPersonalItemEditUiState(),
    ) {
    private val bottariId: Long = stateHandle[KEY_BOTTARI_ID] ?: error(ERROR_REQUIRE_BOTTARI_ID)

    private val debouncedJobs: MutableMap<Long, Job> = mutableMapOf()

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
                .onFailure { emitEvent(TeamPersonalItemEditEvent.CreateItemFailure) }
                .onSuccess {
                    fetchPersonalItems()
                    emitEvent(TeamPersonalItemEditEvent.CreateItemSuccess)
                }

            updateState { copy(isLoading = false) }
        }
    }

    fun requestDeleteItem(itemId: Long) {
        updateState { copy(personalItems = personalItems.filterNot { it.id == itemId }) }

        debouncedJobs[itemId]?.cancel()
        debouncedJobs[itemId] =
            viewModelScope
                .launch {
                    delay(DEBOUNCE_DELAY)
                    performDeleteItem(itemId)
                }.also { job ->
                    job.invokeOnCompletion { debouncedJobs.remove(itemId) }
                }
    }

    private fun performDeleteItem(itemId: Long) {
        launch {
            deleteTeamBottariItemUseCase(itemId, TeamBottariItemType.PERSONAL)
                .onFailure {
                    fetchPersonalItems()
                    emitEvent(TeamPersonalItemEditEvent.DeleteItemFailure)
                }
        }
    }

    private fun fetchPersonalItems() {
        updateState { copy(isLoading = true) }

        launch {
            fetchTeamPersonalItemsUseCase(bottariId)
                .onSuccess { items ->
                    updateState {
                        copy(personalItems = items.map { BottariItemUiModel.fromDomain(it) })
                    }
                }.onFailure { emitEvent(TeamPersonalItemEditEvent.FetchTeamPersonalItemsFailure) }

            updateState { copy(isLoading = false, isFetched = true) }
        }
    }

    companion object {
        const val KEY_BOTTARI_ID = "KEY_BOTTARI_ID"
        private const val ERROR_REQUIRE_BOTTARI_ID = "[ERROR] 보따리 ID가 존재하지 않습니다"
        private const val DEBOUNCE_DELAY = 300L
    }
}
