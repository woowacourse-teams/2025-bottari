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
import com.bottari.presentation.model.BottariItemUiModel

class PersonalItemEditViewModel(
    stateHandle: SavedStateHandle,
    private val saveBottariItemsUseCase: SaveBottariItemsUseCase,
) : BaseViewModel<PersonalItemEditUiState, PersonalItemEditUiEvent>(
        PersonalItemEditUiState(
            bottariId = stateHandle[KEY_BOTTARI_ID] ?: error(ERROR_REQUIRE_SSAID),
            title = stateHandle[KEY_BOTTARI_TITLE] ?: "",
            items = stateHandle[KEY_BOTTARI_ITEMS] ?: emptyList(),
        ),
    ) {
    private val ssaid: String = stateHandle[KEY_SSAID] ?: error(ERROR_REQUIRE_SSAID)

    private val newItemNames = mutableSetOf<String>()
    private val pendingDeleteItems = mutableSetOf<BottariItemUiModel>()

    fun addNewItemIfNeeded(itemName: String) {
        if (itemName.isBlank() || isDuplicateItem(itemName)) return
        if (restoreDeletedItem(itemName)) return

        val newItem = generateNewItemUiModel(itemName)
        newItemNames.add(itemName)
        updateState { copy(items = currentState.items + newItem) }
    }

    fun markItemAsDeleted(itemId: Long) {
        val target = currentState.items.find { it.id == itemId } ?: return
        updateState { copy(items = currentState.items.filterNot { it.id == itemId }) }

        if (currentState.initialItemIds.contains(itemId)) {
            pendingDeleteItems.add(target)
            return
        }

        newItemNames.remove(target.name)
        pendingDeleteItems.remove(target)
    }

    fun saveChanges() {
        updateState { copy(isLoading = true) }

        launch {
            saveBottariItemsUseCase(
                ssaid = ssaid,
                bottariId = currentState.bottariId,
                deleteItemIds = pendingDeleteItems.map { it.id },
                createItemNames = newItemNames.toList(),
            ).onSuccess {
                BottariLogger.ui(
                    UiEventType.PERSONAL_BOTTARI_ITEM_EDIT,
                    mapOf(
                        "bottari_id" to currentState.bottariId.toString(),
                        "old_items" to currentState.initialItems.toString(),
                        "new_items" to currentState.items.toString(),
                    ),
                )
                emitEvent(PersonalItemEditUiEvent.SaveBottariItemsSuccess)
            }.onFailure {
                emitEvent(PersonalItemEditUiEvent.SaveBottariItemsFailure)
            }

            updateState { copy(isLoading = false) }
        }
    }

    private fun isDuplicateItem(name: String): Boolean = currentState.items.any { it.name == name }

    private fun restoreDeletedItem(name: String): Boolean {
        val restored = pendingDeleteItems.firstOrNull { it.name == name } ?: return false
        pendingDeleteItems.remove(restored)
        updateState { copy(items = currentState.items + restored) }
        return true
    }

    private fun generateNewItemUiModel(name: String): BottariItemUiModel =
        BottariItemUiModel(
            id = nextGeneratedItemId(),
            isChecked = false,
            name = name,
        )

    private fun nextGeneratedItemId(): Long = (currentState.items.maxOfOrNull { it.id } ?: DEFAULT_ITEM_ID) + ITEM_ID_INCREMENT

    companion object {
        private const val KEY_SSAID = "KEY_SSAID"
        private const val KEY_BOTTARI_ID = "KEY_BOTTARI_ID"
        private const val KEY_BOTTARI_TITLE = "KEY_BOTTARI_TITLE"
        private const val KEY_BOTTARI_ITEMS = "KEY_BOTTARI_ITEMS"

        private const val ERROR_REQUIRE_SSAID = "[ERROR] SSAID 없습니다"

        private const val DEFAULT_ITEM_ID = 0L
        private const val ITEM_ID_INCREMENT = 1

        fun Factory(
            ssaid: String,
            bottariId: Long,
            title: String,
            items: List<BottariItemUiModel>,
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val stateHandle = createSavedStateHandle()
                    stateHandle[KEY_SSAID] = ssaid
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
