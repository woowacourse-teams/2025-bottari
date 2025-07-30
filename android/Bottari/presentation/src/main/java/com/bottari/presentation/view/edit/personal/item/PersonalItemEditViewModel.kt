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
import com.bottari.presentation.extension.update
import com.bottari.presentation.model.BottariItemUiModel
import kotlinx.coroutines.launch

class PersonalItemEditViewModel(
    stateHandle: SavedStateHandle,
    private val saveBottariItemsUseCase: SaveBottariItemsUseCase,
) : ViewModel() {
    private val ssaid: String = stateHandle[EXTRA_SSAID] ?: error(ERROR_REQUIRE_SSAID)

    private val id: Long = stateHandle[EXTRA_BOTTARI_ID] ?: error(ERROR_REQUIRE_SSAID)
    private val title: String = stateHandle[EXTRA_BOTTARI_TITLE] ?: error(ERROR_REQUIRE_SSAID)
    private val initialItemIds: List<BottariItemUiModel> =
        (stateHandle[EXTRA_BOTTARI_ITEMS] ?: error(ERROR_REQUIRE_BOTTARI_DATA))

    private val _uiState: MutableLiveData<PersonalItemEditUiState> =
        MutableLiveData(PersonalItemEditUiState(id = id, title = title, items = initialItemIds))
    val uiState: LiveData<PersonalItemEditUiState> get() = _uiState

    private val _uiEvent: MutableLiveData<PersonalItemEditUiEvent> =
        MutableLiveData()
    val uiEvent: LiveData<PersonalItemEditUiEvent> get() = _uiEvent

    private val newItemNames = mutableSetOf<String>()
    private val pendingDeleteItems = mutableSetOf<BottariItemUiModel>()

    private val currentItemList: List<BottariItemUiModel>
        get() = _uiState.value?.items ?: emptyList()

    fun addNewItemIfNeeded(itemName: String) {
        if (itemName.isBlank() || isDuplicateItem(itemName)) return

        pendingDeleteItems.firstOrNull { it.name == itemName }?.let {
            pendingDeleteItems.remove(it)
            _uiState.update { copy(items = currentItemList + it) }
            return
        }

        val newItem = generateNewItemUiModel(itemName)
        newItemNames.add(itemName)
        _uiState.update { copy(items = currentItemList + newItem) }
    }

    fun markItemAsDeleted(itemId: Long) {
        val target = currentItemList.find { it.id == itemId } ?: return

        _uiState.update { copy(items = currentItemList.filterNot { it.id == itemId }) }

        if (initialItemIds.any { it.id == itemId }) {
            pendingDeleteItems.add(target)
        } else {
            newItemNames.remove(target.name)
            pendingDeleteItems.remove(target)
        }
    }

    fun saveChanges() {
        _uiState.update { copy(isLoading = true) }

        viewModelScope.launch {
            val deletedIds = pendingDeleteItems.map { it.id }

            saveBottariItemsUseCase(
                ssaid = ssaid,
                bottariId = id,
                deleteItemIds = deletedIds,
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

    private fun generateNewItemUiModel(name: String): BottariItemUiModel =
        BottariItemUiModel(
            id = nextGeneratedItemId(),
            isChecked = false,
            name = name,
        )

    private fun nextGeneratedItemId(): Long = (currentItemList.maxOfOrNull { it.id } ?: DEFAULT_ITEM_ID) + ITEM_ID_INCREMENT

    companion object {
        private const val EXTRA_SSAID = "EXTRA_SSAID"
        private const val EXTRA_BOTTARI_ID = "EXTRA_BOTTARI_ID"
        private const val EXTRA_BOTTARI_TITLE = "EXTRA_BOTTARI_TITLE"
        private const val EXTRA_BOTTARI_ITEMS = "EXTRA_BOTTARI_ITEMS"

        private const val ERROR_REQUIRE_SSAID = "SSAID 없습니다"
        private const val ERROR_REQUIRE_BOTTARI_DATA = "보따리가 없습니다"

        private const val DEFAULT_ITEM_ID = 0L
        private const val ITEM_ID_INCREMENT = 1

        fun Factory(
            ssaid: String,
            id: Long,
            title: String,
            items: List<BottariItemUiModel>,
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val stateHandle =
                        createSavedStateHandle().apply {
                            this[EXTRA_SSAID] = ssaid
                            this[EXTRA_BOTTARI_ID] = id
                            this[EXTRA_BOTTARI_TITLE] = title
                            this[EXTRA_BOTTARI_ITEMS] = ArrayList(items)
                        }

                    PersonalItemEditViewModel(
                        stateHandle = stateHandle,
                        saveBottariItemsUseCase = UseCaseProvider.saveBottariItemsUseCase,
                    )
                }
            }

        private inline fun <reified T> SavedStateHandle.getOrThrow(key: String): T =
            requireNotNull(this[key]) {
                if (key == EXTRA_SSAID) {
                    ERROR_REQUIRE_SSAID
                } else {
                    ERROR_REQUIRE_BOTTARI_DATA
                }
            }
    }
}
