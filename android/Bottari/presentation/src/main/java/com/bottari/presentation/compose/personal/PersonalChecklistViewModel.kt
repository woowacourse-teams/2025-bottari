package com.bottari.presentation.compose.personal

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.bottari.domain.model.tooltip.TooltipType
import com.bottari.domain.usecase.item.FetchItemsUseCase
import com.bottari.domain.usecase.item.ResetItemsCheckStateUseCase
import com.bottari.domain.usecase.item.UpdateItemCheckStateUseCase
import com.bottari.domain.usecase.tooltip.FetchTooltipStatusUseCase
import com.bottari.domain.usecase.tooltip.UpdateTooltipStatusUseCase
import com.bottari.presentation.common.base.FlowBaseViewModel
import com.bottari.presentation.model.bottari.ChecklistItemUiModel
import com.bottari.presentation.util.debounce
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PersonalChecklistViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val fetchItemsUseCase: FetchItemsUseCase,
    private val fetchTooltipStatusUseCase: FetchTooltipStatusUseCase,
    private val updateTooltipStatusUseCase: UpdateTooltipStatusUseCase,
    private val updateItemCheckStateUseCase: UpdateItemCheckStateUseCase,
    private val resetItemsCheckStateUseCase: ResetItemsCheckStateUseCase,
) : FlowBaseViewModel<PersonalChecklistUiState, PersonalChecklistUiEvent>(PersonalChecklistUiState()) {
    private val bottariId: Long = savedStateHandle[KEY_BOTTARI_ID] ?: INVALID_BOTTARI_ID

    private val pendingCheckStatusMap = mutableMapOf<Long, ChecklistItemUiModel>()

    private val debouncedCheck: (List<ChecklistItemUiModel>) -> Unit =
        debounce(
            timeMillis = DEBOUNCE_DELAY,
            coroutineScope = viewModelScope,
        ) { items -> performCheck(items) }

    init {
        fetchChecklist()
        checkIfTooltipWasDismissed()
    }

    fun fetchChecklist() {
        updateState { copy(isLoading = true) }
        fetchItemsUseCase(bottariId)
            .onEach { items ->
                val itemUiModels = items.map(ChecklistItemUiModel::fromDomain)
                updateState {
                    copy(
                        isLoading = false,
                        initialItems = itemUiModels,
                        bottariItems = itemUiModels,
                    )
                }
            }.catch {
                emitEvent(PersonalChecklistUiEvent.FetchChecklistFailure)
                updateState { copy(isLoading = false) }
            }.launchIn(viewModelScope)
    }

    fun resetItemsCheckState() {
        updateState { copy(isLoading = true) }
        launch {
            resetItemsCheckStateUseCase(bottariId)
                .onSuccess {
                    val clearedItems =
                        currentState.bottariItems.map { item -> item.copy(isChecked = false) }
                    updateState {
                        copy(
                            bottariItems = clearedItems,
                            initialItems = clearedItems,
                        )
                    }
                    pendingCheckStatusMap.clear()
                }.onFailure { emitEvent(PersonalChecklistUiEvent.ResetCheckStateFailure) }
            updateState { copy(isLoading = false) }
        }
    }

    fun addSwipedItem(itemId: Long) {
        updateState { copy(swipedItemIds = this.swipedItemIds + itemId) }
    }

    fun toggleItemChecked(itemId: Long) {
        val updatedItems =
            currentState.bottariItems.map { item ->
                if (item.id != itemId) return@map item
                val newItem = item.copy(isChecked = item.isChecked.not())
                recordPendingCheckStatus(newItem)
                newItem
            }
        updateState { copy(bottariItems = updatedItems) }
        debouncedCheck(pendingCheckStatusMap.values.toList())
    }

    fun closeTooltip() {
        launch {
            updateTooltipStatusUseCase(TooltipType.PERSONAL)
                .onSuccess {
                    Log.d("test", "ok")
                }.onFailure { error -> Log.d("test", error.message.toString()) }
            updateState { copy(isTooltipClosed = true) }
        }
    }

    private fun checkIfTooltipWasDismissed() {
        fetchTooltipStatusUseCase(TooltipType.PERSONAL)
            .onEach { state ->
                updateState { copy(isTooltipClosed = state) }
            }.catch {
                emitEvent(PersonalChecklistUiEvent.FetchChecklistFailure)
                updateState { copy(isLoading = true) }
            }.launchIn(viewModelScope)
    }

    private fun performCheck(items: List<ChecklistItemUiModel>) {
        launch {
            val originalItemsById = currentState.initialItems.associateBy { it.id }
            val jobs =
                items
                    .filter { pendingItem ->
                        val originalItem = originalItemsById[pendingItem.id]
                        originalItem != null && originalItem.isChecked != pendingItem.isChecked
                    }.map { changedItem ->
                        async { processItemCheck(changedItem) }
                    }
            jobs.awaitAll()
            pendingCheckStatusMap.clear()
        }
    }

    private suspend fun processItemCheck(item: ChecklistItemUiModel) {
        updateItemCheckStateUseCase(item.id, item.isChecked)
            .onSuccess {
                updateOriginalItem(item)
            }.onFailure {
                revertItemCheckStatus(item.id)
            }
    }

    private fun revertItemCheckStatus(failedItemId: Long) {
        val originalItem =
            currentState.initialItems.find { it.id == failedItemId } ?: return
        val revertedItems =
            currentState.bottariItems.map { uiItem ->
                if (uiItem.id == failedItemId) {
                    return@map uiItem.copy(isChecked = originalItem.isChecked)
                }
                uiItem
            }
        updateState {
            copy(bottariItems = revertedItems)
        }
    }

    private fun updateOriginalItem(updatedItem: ChecklistItemUiModel) {
        val currentOriginals = currentState.initialItems.toMutableList()
        val index = currentOriginals.indexOfFirst { it.id == updatedItem.id }
        if (index != -1) {
            currentOriginals[index] = updatedItem
        }
        updateState { copy(initialItems = currentOriginals) }
    }

    private fun recordPendingCheckStatus(item: ChecklistItemUiModel) {
        pendingCheckStatusMap[item.id] = item
    }

    companion object {
        private const val INVALID_BOTTARI_ID = -1L
        private const val KEY_BOTTARI_ID = "EXTRA_BOTTARI_ID"
        private const val DEBOUNCE_DELAY = 250L
    }
}
