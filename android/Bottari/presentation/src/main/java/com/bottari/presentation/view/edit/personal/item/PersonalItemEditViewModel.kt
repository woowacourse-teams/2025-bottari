package com.bottari.presentation.view.edit.personal.item

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.bottari.domain.usecase.item.DeleteItemUseCase
import com.bottari.domain.usecase.item.FetchItemsUseCase
import com.bottari.domain.usecase.item.SaveItemUseCase
import com.bottari.logger.BottariLogger
import com.bottari.logger.model.UiEventType
import com.bottari.presentation.common.base.FlowBaseViewModel
import com.bottari.presentation.model.bottari.PersonalChecklistItemUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PersonalItemEditViewModel @Inject constructor(
    stateHandle: SavedStateHandle,
    private val fetchItemsUseCase: FetchItemsUseCase,
    private val saveItemUseCase: SaveItemUseCase,
    private val deleteItemUseCase: DeleteItemUseCase,
) : FlowBaseViewModel<PersonalItemEditUiState, PersonalItemEditUiEvent>(
        PersonalItemEditUiState(
            bottariId = stateHandle[KEY_BOTTARI_ID] ?: error(ERROR_REQUIRE_BOTTARI_ID),
            title = stateHandle[KEY_BOTTARI_TITLE] ?: "",
        ),
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

    fun saveItem(itemName: String) {
        if (itemName.isBlank() || isDuplicateItem(itemName)) return
        launch {
            saveItemUseCase(
                bottariId = currentState.bottariId,
                itemName = itemName,
            ).onSuccess {
                val newItem = generateNewItemUiModel(itemName)
                updateState { copy(items = currentState.items + newItem) }
                logSaveChanges()
            }.onFailure {
                emitEvent(PersonalItemEditUiEvent.SaveBottariItemFailure)
            }
        }
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

    private fun isDuplicateItem(name: String): Boolean = currentState.items.any { item -> item.name == name }

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

    companion object {
        const val KEY_BOTTARI_ID = "KEY_BOTTARI_ID"
        const val KEY_BOTTARI_TITLE = "KEY_BOTTARI_TITLE"
        private const val ERROR_REQUIRE_BOTTARI_ID = "[ERROR] 보따리 ID가 없습니다"
        private const val DEFAULT_ITEM_ID = 0L
        private const val ITEM_ID_INCREMENT = 1L
    }
}
