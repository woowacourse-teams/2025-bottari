package com.bottari.presentation.view.edit.personal.item

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.usecase.item.SaveBottariItemsUseCase
import com.bottari.logger.BottariLogger
import com.bottari.logger.model.UiEventType
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.model.BottariItemTypeUiModel
import com.bottari.presentation.model.BottariItemUiModel

class PersonalItemEditViewModel(
    stateHandle: SavedStateHandle,
    private val saveBottariItemsUseCase: SaveBottariItemsUseCase,
) : BaseViewModel<PersonalItemEditUiState, PersonalItemEditUiEvent>(
        PersonalItemEditUiState(
            bottariId = stateHandle[KEY_BOTTARI_ID] ?: error(ERROR_REQUIRE_BOTTARI_ID),
            title = stateHandle[KEY_BOTTARI_TITLE] ?: "",
            initialItems = stateHandle[KEY_BOTTARI_ITEMS] ?: emptyList(),
            items = stateHandle[KEY_BOTTARI_ITEMS] ?: emptyList(),
        ),
    ) {
    private data class ItemChanges(
        val deleteItemIds: List<Long>,
        val createItemNames: List<String>,
    )

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

    fun markItemAsDeleted(itemId: Long) {
        updateState { copy(items = currentState.items.filterNot { it.id == itemId }) }
    }

    fun saveChanges() {
        val changes = calculateItemChanges()

        if (changes.deleteItemIds.isEmpty() && changes.createItemNames.isEmpty()) {
            emitEvent(PersonalItemEditUiEvent.SaveBottariItemsSuccess)
            return
        }

        updateState { copy(isLoading = true) }
        executeSaveChanges(changes)
    }

    private fun calculateItemChanges(): ItemChanges {
        val initialItems = currentState.initialItems
        val finalItems = currentState.items

        val initialItemIds = initialItems.map { it.id }.toSet()
        val finalItemIds = finalItems.map { it.id }.toSet()
        val deleteItemIds = initialItemIds.filterNot { it in finalItemIds }
        val createItemNames =
            finalItems
                .filterNot { item -> item.id in initialItemIds }
                .map { item -> item.name }

        return ItemChanges(deleteItemIds, createItemNames)
    }

    private fun executeSaveChanges(changes: ItemChanges) {
        launch {
            saveBottariItemsUseCase(
                bottariId = currentState.bottariId,
                deleteItemIds = changes.deleteItemIds,
                createItemNames = changes.createItemNames,
            ).onSuccess {
                logSaveChanges()
                emitEvent(PersonalItemEditUiEvent.SaveBottariItemsSuccess)
            }.onFailure {
                emitEvent(PersonalItemEditUiEvent.SaveBottariItemsFailure)
            }
            updateState { copy(isLoading = false) }
        }
    }

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

    private fun restoreItem(itemToRestore: BottariItemUiModel) {
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

    private fun generateNewItemUiModel(name: String): BottariItemUiModel =
        BottariItemUiModel(
            id = nextGeneratedItemId(),
            name = name,
            type = BottariItemTypeUiModel.PERSONAL,
        )

    private fun nextGeneratedItemId(): Long = (currentState.items.maxOfOrNull { item -> item.id } ?: DEFAULT_ITEM_ID) + ITEM_ID_INCREMENT

    companion object {
        private const val KEY_BOTTARI_ID = "KEY_BOTTARI_ID"
        private const val KEY_BOTTARI_TITLE = "KEY_BOTTARI_TITLE"
        private const val KEY_BOTTARI_ITEMS = "KEY_BOTTARI_ITEMS"
        private const val ERROR_REQUIRE_BOTTARI_ID = "[ERROR] 보따리 ID가 없습니다"

        private const val DEFAULT_ITEM_ID = 0L
        private const val ITEM_ID_INCREMENT = 1L

        fun Factory(
            bottariId: Long,
            title: String,
            items: List<BottariItemUiModel>,
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val stateHandle = createSavedStateHandle()
                    stateHandle[KEY_BOTTARI_ID] = bottariId
                    stateHandle[KEY_BOTTARI_TITLE] = title
                    stateHandle[KEY_BOTTARI_ITEMS] = ArrayList(items)

                    PersonalItemEditViewModel(
                        stateHandle = stateHandle,
                        saveBottariItemsUseCase = UseCaseProvider.saveBottariItemsUseCase,
                    )
                }
            }
    }
}
