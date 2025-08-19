package com.bottari.presentation.view.checklist.personal

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.model.bottari.ChecklistItem
import com.bottari.domain.usecase.item.CheckBottariItemUseCase
import com.bottari.domain.usecase.item.FetchChecklistUseCase
import com.bottari.domain.usecase.item.UnCheckBottariItemUseCase
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.mapper.BottariMapper.toUiModel
import com.bottari.presentation.model.ChecklistItemUiModel
import com.bottari.presentation.util.debounce
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

class ChecklistViewModel(
    stateHandle: SavedStateHandle,
    private val fetchChecklistUseCase: FetchChecklistUseCase,
    private val checkBottariItemUseCase: CheckBottariItemUseCase,
    private val unCheckBottariItemUseCase: UnCheckBottariItemUseCase,
) : BaseViewModel<ChecklistUiState, ChecklistUiEvent>(ChecklistUiState()) {
    private val bottariId: Long = stateHandle[KEY_BOTTARI_ID] ?: error(ERROR_REQUIRE_BOTTARI_ID)
    private val pendingCheckStatusMap = mutableMapOf<Long, ChecklistItemUiModel>()

    private val debouncedCheck: (List<ChecklistItemUiModel>) -> Unit =
        debounce(
            timeMillis = DEBOUNCE_DELAY,
            coroutineScope = viewModelScope,
        ) { items -> performCheck(items) }

    init {
        fetchChecklist()
    }

    fun fetchChecklist() {
        updateState { copy(isLoading = true) }

        launch {
            fetchChecklistUseCase(bottariId)
                .onSuccess { items ->
                    setChecklist(items)
                }.onFailure {
                    emitEvent(ChecklistUiEvent.FetchChecklistFailure)
                }

            updateState { copy(isLoading = false) }
        }
    }

    fun resetSwipeState() {
        updateState { copy(swipedItemIds = emptySet()) }
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

    private fun setChecklist(items: List<ChecklistItem>) {
        val itemUiModels = items.map { it.toUiModel() }
        updateState { copy(bottariItems = itemUiModels, originalBottariItems = itemUiModels) }
    }

    private fun performCheck(items: List<ChecklistItemUiModel>) {
        launch {
            val originalItems = currentState.originalBottariItems
            val itemsToUpdate = mutableListOf<ChecklistItemUiModel>()

            val jobs =
                items.mapNotNull { pendingItem ->
                    val originalItem = originalItems.find { it.id == pendingItem.id }
                    if (originalItem == null || originalItem.isChecked == pendingItem.isChecked) {
                        return@mapNotNull null
                    }
                    itemsToUpdate.add(pendingItem)
                    async { processItemCheck(pendingItem) }
                }
            jobs.awaitAll()
            pendingCheckStatusMap.clear()
        }
    }

    private suspend fun processItemCheck(item: ChecklistItemUiModel) {
        executeCheckUseCase(item)
            .onSuccess {
                updateOriginalItem(item)
            }.onFailure {
                emitEvent(ChecklistUiEvent.CheckItemFailure)
            }
    }

    private fun updateOriginalItem(updatedItem: ChecklistItemUiModel) {
        val currentOriginals = currentState.originalBottariItems.toMutableList()
        val index = currentOriginals.indexOfFirst { it.id == updatedItem.id }
        if (index != -1) {
            currentOriginals[index] = updatedItem
        }

        updateState { copy(originalBottariItems = currentOriginals) }
    }

    private suspend fun executeCheckUseCase(item: ChecklistItemUiModel) =
        if (item.isChecked) {
            checkBottariItemUseCase(item.id)
        } else {
            unCheckBottariItemUseCase(item.id)
        }

    private fun recordPendingCheckStatus(item: ChecklistItemUiModel) {
        pendingCheckStatusMap[item.id] = item
    }

    companion object {
        private const val KEY_BOTTARI_ID = "KEY_BOTTARI_ID"
        private const val ERROR_REQUIRE_BOTTARI_ID = "[ERROR] 보따리 ID가 존재하지 않습니다."
        private const val DEBOUNCE_DELAY = 250L

        fun Factory(bottariId: Long): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val stateHandle = createSavedStateHandle()
                    stateHandle[KEY_BOTTARI_ID] = bottariId
                    ChecklistViewModel(
                        stateHandle,
                        UseCaseProvider.fetchChecklistUseCase,
                        UseCaseProvider.checkBottariItemUseCase,
                        UseCaseProvider.unCheckBottariItemUseCase,
                    )
                }
            }
    }
}
