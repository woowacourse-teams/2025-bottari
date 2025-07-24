package com.bottari.presentation.view.edit.personal.item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.presentation.base.UiState
import com.bottari.presentation.extension.takeSuccess
import com.bottari.presentation.model.BottariDetailUiModel
import com.bottari.presentation.model.BottariItemUiModel

class PersonalItemEditViewModel(
    stateHandle: SavedStateHandle,
) : ViewModel() {
    private var bottariId: Long? = null

    private val _bottariName: MutableLiveData<String> = MutableLiveData<String>()
    val bottariName: LiveData<String> get() = _bottariName

    private val _items = MutableLiveData<UiState<List<BottariItemUiModel>>>(UiState.Loading)
    val items: LiveData<UiState<List<BottariItemUiModel>>> = _items

    init {
        val bottariDetail = stateHandle.get<BottariDetailUiModel>(EXTRA_BOTTARI_DETAIL) ?: error("")
        setupData(bottariDetail)
    }

    fun addItem(itemName: String) {
        if (itemName.isBlank()) return

        val currentList = _items.value?.takeSuccess() ?: return
        if (currentList.any { item -> item.name == itemName }) return

        val updatedList = currentList + createItem(itemName)
        _items.value = UiState.Success(updatedList)
    }

    fun deleteItem(itemId: Long) {
        val currentList = _items.value?.takeSuccess() ?: return
        val updatedList = currentList.filterNot { it.id == itemId }
        _items.value = UiState.Success(updatedList)
    }

    private fun setupData(bottariDetail: BottariDetailUiModel) {
        bottariId = bottariDetail.id
        _bottariName.value = bottariDetail.title
        _items.value = UiState.Success(bottariDetail.items)
    }

    private fun createItem(name: String): BottariItemUiModel {
        val currentList = _items.value?.takeSuccess().orEmpty()
        val maxId = currentList.maxOfOrNull { it.id } ?: 0L
        return BottariItemUiModel(id = maxId + 1, isChecked = false, name = name)
    }

    companion object {
        private const val EXTRA_BOTTARI_DETAIL = "EXTRA_BOTTARI_DETAIL"
        private const val ERROR_REQUIRE_BOTTARI_ID = "보따리 ID가 없습니다"

        fun Factory(bottariDetail: BottariDetailUiModel): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val stateHandle = createSavedStateHandle()
                    stateHandle[EXTRA_BOTTARI_DETAIL] = bottariDetail
                    PersonalItemEditViewModel(stateHandle)
                }
            }
    }
}
