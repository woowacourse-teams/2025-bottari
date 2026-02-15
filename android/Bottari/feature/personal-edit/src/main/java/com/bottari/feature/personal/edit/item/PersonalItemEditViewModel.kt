package com.bottari.feature.personal.edit.item

import androidx.compose.runtime.Stable
import androidx.lifecycle.viewModelScope
import com.bottari.core.domain.usecase.item.DeleteItemUseCase
import com.bottari.core.domain.usecase.item.FetchItemsUseCase
import com.bottari.core.domain.usecase.item.SaveItemUseCase
import com.bottari.core.ui.base.BaseViewModel
import com.bottari.core.ui.model.bottari.PersonalChecklistItemUiModel
import com.bottari.logger.BottariLogger
import com.bottari.logger.model.UiEventType
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Stable
@HiltViewModel(assistedFactory = PersonalItemEditViewModel.Factory::class)
class PersonalItemEditViewModel @AssistedInject constructor(
    @Assisted private val bottariId: Long,
    private val fetchItemsUseCase: FetchItemsUseCase,
    private val saveItemUseCase: SaveItemUseCase,
    private val deleteItemUseCase: DeleteItemUseCase,
) : BaseViewModel<PersonalItemEditUiState, PersonalItemEditUiEvent>(
        PersonalItemEditUiState(bottariId = bottariId),
    ) {
    init {
        fetchItems()
    }

    fun deleteItem(itemId: Long) {
        launch {
            deleteItemUseCase(itemId)
                .onSuccess {
                    updateState { copy(items = currentState.items.filterNot { it.id == itemId }) }
                }.onFailure { emitEvent(PersonalItemEditUiEvent.DeleteItemFailure) }
        }
    }

    fun saveItem() {
        val itemName = currentState.itemName.trim()
        if (currentState.isSavable.not()) return
        launch {
            saveItemUseCase(
                bottariId = currentState.bottariId,
                itemName = itemName,
            ).onSuccess {
                updateState { copy(itemName = "") }
                logSaveChanges()
            }.onFailure {
                emitEvent(PersonalItemEditUiEvent.SaveBottariItemFailure)
            }
        }
    }

    fun updateItemName(itemName: String) {
        updateState { copy(itemName = itemName) }
    }

    private fun fetchItems() {
        updateState { copy(isLoading = true) }
        fetchItemsUseCase(currentState.bottariId)
            .onEach { items ->
                val itemUiModels = items.map(PersonalChecklistItemUiModel::fromDomain)
                updateState {
                    if (!isFetched) {
                        copy(
                            isLoading = false,
                            items = itemUiModels,
                            isFetched = true,
                        )
                    } else {
                        copy(items = itemUiModels)
                    }
                }
            }.catch {
                emitEvent(PersonalItemEditUiEvent.FetchBottariItemsFailure)
                updateState { copy(isLoading = false) }
            }.launchIn(viewModelScope)
    }

    private fun generateNewItemUiModel(name: String): PersonalChecklistItemUiModel =
        PersonalChecklistItemUiModel(
            id = nextGeneratedItemId(),
            name = name,
            isChecked = false,
        )

    private fun nextGeneratedItemId(): Long = (currentState.items.maxOfOrNull { item -> item.id } ?: DEFAULT_ITEM_ID) + ITEM_ID_INCREMENT

    private fun logSaveChanges() {
        BottariLogger.ui(
            UiEventType.PERSONAL_BOTTARI_ITEM_EDIT,
            mapOf(
                "bottari_id" to currentState.bottariId.toString(),
                "items" to currentState.items.toString(),
            ),
        )
    }

    @AssistedFactory
    interface Factory {
        fun create(bottariId: Long): PersonalItemEditViewModel
    }

    companion object {
        private const val DEFAULT_ITEM_ID = 0L
        private const val ITEM_ID_INCREMENT = 1L
    }
}
