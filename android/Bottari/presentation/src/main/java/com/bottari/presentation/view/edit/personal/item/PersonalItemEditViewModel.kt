package com.bottari.presentation.view.edit.personal.item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.bottari.presentation.base.UiState
import com.bottari.presentation.extension.takeSuccess
import com.bottari.presentation.model.ItemUiModel

class PersonalItemEditViewModel(
    stateHandle: SavedStateHandle,
) : ViewModel() {

    private val _bottariName: MutableLiveData<UiState<String>> = MutableLiveData<UiState<String>>()
    val bottariName: LiveData<UiState<String>> get() = _bottariName

    private val _items = MutableLiveData<UiState<List<ItemUiModel>>>(UiState.Loading)
    val items: LiveData<UiState<List<ItemUiModel>>> = _items

    init {
        val bottariId = stateHandle.get<Long>(EXTRAS_BOTTARI_ID)
        requireNotNull(bottariId) { "bottariId must not be null" }

        fetchItems(bottariId)
    }

    fun addItem(itemName: String) {
        if (itemName.isBlank()) return

        val currentList = _items.value?.takeSuccess() ?: return
        val updatedList = currentList + createItem(itemName)
        _items.value = UiState.Success(updatedList)
    }

    fun deleteItem(itemId: Long) {
        val currentList = _items.value?.takeSuccess() ?: return
        val updatedList = currentList.filterNot { it.id == itemId }
        _items.value = UiState.Success(updatedList)
    }

    private fun fetchItems(bottariId: Long) {
        if (bottariId == INVALID_ID) {
            _items.value = UiState.Failure("유효하지 않은 보따리 ID입니다.")
            return
        }
        _bottariName.value = UiState.Success("출근 보따리")
        _items.value = UiState.Success(dummyItems)
    }

    private fun createItem(name: String): ItemUiModel {
        val currentList = _items.value?.takeSuccess().orEmpty()
        val maxId = currentList.maxOfOrNull { it.id } ?: 0L
        return ItemUiModel(id = maxId + 1, isChecked = false, name = name)
    }

    companion object {
        private const val INVALID_ID = -1L
        private const val EXTRAS_BOTTARI_ID = "EXTRAS_BOTTARI_ID"

        fun Factory(bottariId: Long): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras,
                ): T {
                    val handle = extras.createSavedStateHandle().apply {
                        this[EXTRAS_BOTTARI_ID] = bottariId
                    }
                    return PersonalItemEditViewModel(handle) as T
                }
            }

        private val dummyItems = listOf(
            "우유", "계란", "식빵", "세제",
        ).mapIndexed { index, name ->
            ItemUiModel(id = index.toLong(), isChecked = index % 2 == 0, name = name)
        }
    }
}
