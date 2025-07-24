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
import com.bottari.presentation.base.UiState
import com.bottari.presentation.model.BottariDetailUiModel
import com.bottari.presentation.model.BottariItemUiModel
import kotlinx.coroutines.launch

class PersonalItemEditViewModel(
    stateHandle: SavedStateHandle,
    private val saveBottariItemsUseCase: SaveBottariItemsUseCase,
) : ViewModel() {
    private val ssaid: String =
        stateHandle.get<String>(EXTRA_BOTTARI_ID) ?: error(ERROR_REQUIRE_SSAID)
    private val bottariDetail: BottariDetailUiModel =
        stateHandle.get(EXTRA_BOTTARI_DETAIL) ?: error(ERROR_REQUIRE_BOTTARI_DATA)

    private val bottariId: Long = bottariDetail.id
    private val createItemNames = mutableSetOf<String>()
    private val deleteItems = mutableSetOf<BottariItemUiModel>()

    private val _saveState = MutableLiveData<UiState<Unit>>()
    val saveState: LiveData<UiState<Unit>> get() = _saveState

    private val _bottariName = MutableLiveData(bottariDetail.title)
    val bottariName: LiveData<String> get() = _bottariName

    private val _items = MutableLiveData<List<BottariItemUiModel>>(bottariDetail.items)
    val items: LiveData<List<BottariItemUiModel>> get() = _items

    private val currentList: List<BottariItemUiModel> get() = _items.value ?: emptyList()

    fun addItem(itemName: String) {
        if (itemName.isBlank() || isAlreadyExistItem(itemName)) return

        val restored = deleteItems.find { it.name == itemName }
        if (restored != null) {
            deleteItems.remove(restored)
            _items.value = currentList + restored
            return
        }

        _items.value = currentList + createNewItem(itemName)
        createItemNames.add(itemName)
    }

    fun deleteItem(itemId: Long) {
        val updated = currentList.filterNot { it.id == itemId }
        val deleted = currentList.find { it.id == itemId } ?: return

        _items.value = updated
        deleteItems.add(deleted)
    }

    fun saveItems() {
        _saveState.value = UiState.Loading

        viewModelScope.launch {
            val deleteItemIds = deleteItems.map { it.id }
            saveBottariItemsUseCase(ssaid, bottariId, deleteItemIds, createItemNames.toList())
                .onSuccess { _saveState.value = UiState.Success(Unit) }
                .onFailure { _saveState.value = UiState.Failure(it.message) }
        }
    }

    private fun isAlreadyExistItem(name: String): Boolean = currentList.any { it.name == name }

    private fun createNewItem(name: String): BottariItemUiModel =
        BottariItemUiModel(
            id = (currentList.maxOfOrNull { it.id } ?: DEFAULT_ITEM_ID) + ITEM_ID_INCREMENT,
            isChecked = false,
            name = name,
        )

    companion object {
        private const val EXTRA_BOTTARI_ID = "EXTRA_BOTTARI_ID"
        private const val EXTRA_BOTTARI_DETAIL = "EXTRA_BOTTARI_DETAIL"
        private const val ERROR_REQUIRE_SSAID = "SSAID 없습니다"
        private const val ERROR_REQUIRE_BOTTARI_DATA = "보따리가 없습니다"

        private const val DEFAULT_ITEM_ID = 0L
        private const val ITEM_ID_INCREMENT = 1

        fun Factory(
            ssaid: String,
            bottariDetail: BottariDetailUiModel,
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val stateHandle = createSavedStateHandle()
                    stateHandle[EXTRA_BOTTARI_ID] = ssaid
                    stateHandle[EXTRA_BOTTARI_DETAIL] = bottariDetail

                    PersonalItemEditViewModel(
                        stateHandle,
                        UseCaseProvider.saveBottariItemsUseCase,
                    )
                }
            }
    }
}
