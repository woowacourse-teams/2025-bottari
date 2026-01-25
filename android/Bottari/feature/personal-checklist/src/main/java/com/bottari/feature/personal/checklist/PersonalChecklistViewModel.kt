package com.bottari.feature.personal.checklist

import androidx.compose.runtime.Stable
import androidx.lifecycle.viewModelScope
import com.bottari.core.domain.model.tooltip.TooltipType
import com.bottari.core.domain.usecase.item.FetchItemsUseCase
import com.bottari.core.domain.usecase.item.ResetItemsCheckStateUseCase
import com.bottari.core.domain.usecase.item.UpdateItemCheckStateUseCase
import com.bottari.core.domain.usecase.tooltip.FetchTooltipStatusUseCase
import com.bottari.core.domain.usecase.tooltip.UpdateTooltipStatusUseCase
import com.bottari.core.ui.base.FlowBaseViewModel
import com.bottari.core.ui.model.bottari.PersonalChecklistItemUiModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Stable
@HiltViewModel(assistedFactory = PersonalChecklistViewModel.Factory::class)
class PersonalChecklistViewModel @AssistedInject constructor(
    @Assisted private val bottariId: Long,
    private val fetchItemsUseCase: FetchItemsUseCase,
    private val fetchTooltipStatusUseCase: FetchTooltipStatusUseCase,
    private val updateTooltipStatusUseCase: UpdateTooltipStatusUseCase,
    private val updateItemCheckStateUseCase: UpdateItemCheckStateUseCase,
    private val resetItemsCheckStateUseCase: ResetItemsCheckStateUseCase,
) : FlowBaseViewModel<PersonalChecklistUiState, PersonalChecklistUiEvent>(PersonalChecklistUiState()) {
    init {
        fetchChecklist()
        checkIfTooltipWasDismissed()
    }

    private fun fetchChecklist() {
        updateState { copy(isLoading = true) }
        fetchItemsUseCase(bottariId)
            .onEach { items ->
                val itemUiModels = items.map(PersonalChecklistItemUiModel::fromDomain)
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
                    val updatedInitialItems =
                        currentState.initialItems.map { if (it.id == itemId) newItem else it }
                    updateState { copy(initialItems = updatedInitialItems) }
                }.onFailure {
                    val revertedItems =
                        currentState.bottariItems.map { if (it.id == itemId) originalItem else it }
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

    @AssistedFactory
    interface Factory {
        fun create(bottariId: Long): PersonalChecklistViewModel
    }
}
