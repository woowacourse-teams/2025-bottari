package com.bottari.presentation.view.edit.personal.item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.usecase.item.SaveBottariItemsUseCase
import com.bottari.presentation.extension.requireValue
import com.bottari.presentation.extension.update
import com.bottari.presentation.model.BottariItemUiModel
import kotlinx.coroutines.launch

class PersonalItemEditViewModel(
    stateHandle: SavedStateHandle,
    private val saveBottariItemsUseCase: SaveBottariItemsUseCase,
) : ViewModel() {
    private val _uiState =
        MutableLiveData(
            PersonalItemEditUiState(
                bottariId = stateHandle[KEY_BOTTARI_ID] ?: error(ERROR_REQUIRE_SSAID),
                title = stateHandle[KEY_BOTTARI_TITLE] ?: "",
                items = stateHandle[KEY_BOTTARI_ITEMS] ?: emptyList(),
            ),
        )
    val uiState: LiveData<PersonalItemEditUiState> get() = _uiState

    private val _uiEvent = MutableLiveData<PersonalItemEditUiEvent>()
    val uiEvent: LiveData<PersonalItemEditUiEvent> get() = _uiEvent

    private val ssaid: String = stateHandle[KEY_SSAID] ?: error(ERROR_REQUIRE_SSAID)

    private val newItemNames = mutableSetOf<String>()
    private val pendingDeleteItems = mutableSetOf<BottariItemUiModel>()
    private val initialItemIds: List<Long> = _uiState.requireValue().items.map { it.id }

    private val currentItemList: List<BottariItemUiModel>
        get() = _uiState.requireValue().items

    fun addNewItemIfNeeded(itemName: String) {
        if (itemName.isBlank() || isDuplicateItem(itemName)) return
        if (restoreDeletedItem(itemName)) return

        val newItem = generateNewItemUiModel(itemName)
        newItemNames.add(itemName)
        _uiState.update { copy(items = currentItemList + newItem) }
    }

    fun markItemAsDeleted(itemId: Long) {
        val target = currentItemList.find { it.id == itemId } ?: return
        _uiState.update { copy(items = currentItemList.filterNot { it.id == itemId }) }

        if (initialItemIds.contains(itemId)) {
            pendingDeleteItems.add(target)
            return
        }

        newItemNames.remove(target.name)
        pendingDeleteItems.remove(target)
    }

    fun saveChanges() {
        _uiState.update { copy(isLoading = true) }

        viewModelScope.launch {
            saveBottariItemsUseCase(
                ssaid = ssaid,
                bottariId = _uiState.requireValue().bottariId,
                deleteItemIds = pendingDeleteItems.map { it.id },
                createItemNames = newItemNames.toList(),
            ).onSuccess {
                _uiEvent.value = PersonalItemEditUiEvent.SaveBottariItemsSuccess
            }.onFailure {
                _uiEvent.value = PersonalItemEditUiEvent.SaveBottariItemsFailure
            }

            _uiState.update { copy(isLoading = false) }
        }
    }

    private fun isDuplicateItem(name: String): Boolean = currentItemList.any { it.name == name }

    private fun restoreDeletedItem(name: String): Boolean {
        val restored = pendingDeleteItems.firstOrNull { it.name == name } ?: return false
        pendingDeleteItems.remove(restored)
        _uiState.update { copy(items = currentItemList + restored) }
        return true
    }

    private fun generateNewItemUiModel(name: String): BottariItemUiModel =
        BottariItemUiModel(
            id = nextGeneratedItemId(),
            isChecked = false,
            name = name,
        )

    private fun nextGeneratedItemId(): Long = (currentItemList.maxOfOrNull { it.id } ?: DEFAULT_ITEM_ID) + ITEM_ID_INCREMENT

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
