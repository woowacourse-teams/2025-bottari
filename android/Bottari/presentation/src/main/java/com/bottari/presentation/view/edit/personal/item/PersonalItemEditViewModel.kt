package com.bottari.presentation.view.edit.personal.item

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.usecase.BottariItemUseCaseProvider
import com.bottari.domain.usecase.item.DeleteItemUseCase
import com.bottari.domain.usecase.item.FetchItemsUseCase
import com.bottari.domain.usecase.item.SaveItemsUseCase
import com.bottari.logger.BottariLogger
import com.bottari.logger.model.UiEventType
import com.bottari.presentation.common.base.FlowBaseViewModel
import com.bottari.presentation.model.bottari.ChecklistItemUiModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class PersonalItemEditViewModel(
    stateHandle: SavedStateHandle,
    private val fetchItemsUseCase: FetchItemsUseCase,
    private val saveItemsUseCase: SaveItemsUseCase,
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

    fun addNewItemIfNeeded(itemName: String) {
        if (itemName.isBlank() || isDuplicateItem(itemName)) return

        val itemToRestore =
            currentState.initialItems.firstOrNull { originalItem ->
                originalItem.name == itemName && currentState.items.none { item -> item.id == originalItem.id }
            }

        if (itemToRestore != null) {
            restoreItem(itemToRestore)
            return
        }

        val newItem = generateNewItemUiModel(itemName)
        updateState { copy(items = currentState.items + newItem) }
    }

    fun deleteItem(itemId: Long) {
        launch {
            deleteItemUseCase(itemId)
                .onSuccess {
                    updateState { copy(items = currentState.items.filterNot { it.id == itemId }) }
                }.onFailure { emitEvent(PersonalItemEditUiEvent.DeleteItemFailure) }
        }
    }

    fun saveItems() {
        launch {
            saveItemsUseCase(
                bottariId = currentState.bottariId,
                items = currentState.items.map { item -> item.name },
            ).onSuccess {
                logSaveChanges()
                emitEvent(PersonalItemEditUiEvent.SaveBottariItemsSuccess)
            }.onFailure {
                emitEvent(PersonalItemEditUiEvent.SaveBottariItemsFailure)
            }
            updateState { copy(isLoading = false) }
        }
    }

    private fun fetchItems() {
        updateState { copy(isLoading = true) }
        fetchItemsUseCase(currentState.bottariId)
            .onEach { items ->
                val itemUiModels = items.map(ChecklistItemUiModel::fromDomain)
                updateState {
                    if (!isFetched) {
                        copy(
                            isLoading = false,
                            initialItems = itemUiModels,
                            items = itemUiModels,
                            isFetched = true,
                        )
                    } else {
                        copy(initialItems = itemUiModels)
                    }
                }
            }.catch {
                emitEvent(PersonalItemEditUiEvent.FetchBottariItemsFailure)
                updateState { copy(isLoading = false) }
            }.launchIn(viewModelScope)
    }

    private fun restoreItem(itemToRestore: ChecklistItemUiModel) {
        val restoredList = currentState.items + itemToRestore

        val originalOrderMap =
            currentState.initialItems
                .withIndex()
                .associate { (index, item) -> item.id to index }

        val sortedList =
            restoredList.sortedWith(
                compareBy { item -> originalOrderMap[item.id] ?: Int.MAX_VALUE },
            )
        updateState { copy(items = sortedList) }
    }

    private fun isDuplicateItem(name: String): Boolean = currentState.items.any { item -> item.name == name }

    private fun generateNewItemUiModel(name: String): ChecklistItemUiModel =
        ChecklistItemUiModel(
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
                "old_items" to currentState.initialItems.toString(),
                "new_items" to currentState.items.toString(),
            ),
        )
    }

    companion object {
        private const val KEY_BOTTARI_ID = "KEY_BOTTARI_ID"
        private const val KEY_BOTTARI_TITLE = "KEY_BOTTARI_TITLE"
        private const val ERROR_REQUIRE_BOTTARI_ID = "[ERROR] 보따리 ID가 없습니다"
        private const val DEFAULT_ITEM_ID = 0L
        private const val ITEM_ID_INCREMENT = 1L

        fun Factory(
            bottariId: Long,
            title: String,
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val stateHandle = createSavedStateHandle()
                    stateHandle[KEY_BOTTARI_ID] = bottariId
                    stateHandle[KEY_BOTTARI_TITLE] = title

                    PersonalItemEditViewModel(
                        stateHandle = stateHandle,
                        fetchItemsUseCase = BottariItemUseCaseProvider.fetchItemsUseCase,
                        saveItemsUseCase = BottariItemUseCaseProvider.saveItemsUseCase,
                        deleteItemUseCase = BottariItemUseCaseProvider.deleteItemUseCase,
                    )
                }
            }
    }
}
