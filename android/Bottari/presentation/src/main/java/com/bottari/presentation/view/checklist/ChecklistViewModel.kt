package com.bottari.presentation.view.checklist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.usecase.item.CheckBottariItemUseCase
import com.bottari.domain.usecase.item.FetchChecklistUseCase
import com.bottari.domain.usecase.item.UnCheckBottariItemUseCase
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.common.extension.update
import com.bottari.presentation.mapper.BottariMapper.toUiModel
import com.bottari.presentation.model.BottariItemUiModel
import com.bottari.presentation.util.debounce
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class ChecklistViewModel(
    stateHandle: SavedStateHandle,
    private val fetchChecklistUseCase: FetchChecklistUseCase,
    private val checkBottariItemUseCase: CheckBottariItemUseCase,
    private val unCheckBottariItemUseCase: UnCheckBottariItemUseCase,
) : BaseViewModel<ChecklistUiState, ChecklistUiEvent>(ChecklistUiState()) {
    private val ssaid: String = stateHandle[KEY_SSAID] ?: error(ERROR_REQUIRE_SSAID)
    private val bottariId: Long = stateHandle[KEY_BOTTARI_ID] ?: error(ERROR_REQUIRE_BOTTARI_ID)
    private val pendingCheckStatusMap = mutableMapOf<Long, BottariItemUiModel>()

    private val debouncedCheck: (List<BottariItemUiModel>) -> Unit =
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
            fetchChecklistUseCase(ssaid, bottariId)
                .onSuccess { items ->
                    val itemUiModels = items.map { it.toUiModel() }
                    updateState { copy(bottariItems = itemUiModels) }
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

    private fun performCheck(items: List<BottariItemUiModel>) {
        launch {
            val jobs =
                items.map { item ->
                    async { processItemCheck(item) }
                }
            jobs.awaitAll()
            pendingCheckStatusMap.clear()
        }
    }

    private suspend fun processItemCheck(item: BottariItemUiModel) {
        val result =
            if (item.isChecked) {
                checkBottariItemUseCase(ssaid, item.id)
            } else {
                unCheckBottariItemUseCase(ssaid, item.id)
            }
        result.onFailure {
            emitEvent(ChecklistUiEvent.CheckItemFailure)
        }
    }

    private fun recordPendingCheckStatus(item: BottariItemUiModel) {
        pendingCheckStatusMap[item.id] = item
    }

    companion object {
        private const val KEY_SSAID = "KEY_SSAID"
        private const val KEY_BOTTARI_ID = "KEY_BOTTARI_ID"
        private const val ERROR_REQUIRE_SSAID = "[ERROR] SSAID가 존재하지 않습니다."
        private const val ERROR_REQUIRE_BOTTARI_ID = "[ERROR] 보따리 ID가 존재하지 않습니다."
        private const val DEBOUNCE_DELAY = 250L

        fun Factory(
            ssaid: String,
            bottariId: Long,
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val stateHandle = createSavedStateHandle()
                    stateHandle[KEY_SSAID] = ssaid
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
