package com.bottari.presentation.compose.personal

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
import dagger.hilt.android.lifecycle.HiltViewModel
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

    init {
        fetchChecklist()
        checkIfTooltipWasDismissed()
    }

    private fun fetchChecklist() {
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
                }.onFailure { emitEvent(PersonalChecklistUiEvent.ResetCheckStateFailure) }
            updateState { copy(isLoading = false) }
        }
    }

    fun toggleItemChecked(itemId: Long) {
        val originalItem = currentState.bottariItems.find { it.id == itemId } ?: return
        val newItem = originalItem.copy(isChecked = !originalItem.isChecked)

        val optimisticItems = currentState.bottariItems.map { if (it.id == itemId) newItem else it }
        updateState { copy(bottariItems = optimisticItems) }

        launch {
            updateItemCheckStateUseCase(newItem.id, newItem.isChecked)
                .onSuccess {
                    val updatedInitialItems = currentState.initialItems.map { if (it.id == itemId) newItem else it }
                    updateState { copy(initialItems = updatedInitialItems) }
                }.onFailure {
                    val revertedItems = currentState.bottariItems.map { if (it.id == itemId) originalItem else it }
                    updateState { copy(bottariItems = revertedItems) }
                }
        }
    }

    fun closeTooltip() {
        launch {
            updateTooltipStatusUseCase(TooltipType.PERSONAL)
            updateState { copy(isTooltipClosed = true) }
        }
    }

    private fun checkIfTooltipWasDismissed() {
        fetchTooltipStatusUseCase(TooltipType.PERSONAL)
            .onEach { state ->
                updateState { copy(isTooltipClosed = state) }
            }.catch {
                emitEvent(PersonalChecklistUiEvent.FetchChecklistFailure)
            }.launchIn(viewModelScope)
    }

    companion object {
        private const val INVALID_BOTTARI_ID = -1L
        private const val KEY_BOTTARI_ID = "EXTRA_BOTTARI_ID"
    }
}
