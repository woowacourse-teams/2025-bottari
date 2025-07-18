package com.bottari.presentation.view.checklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.map
import androidx.lifecycle.viewmodel.CreationExtras
import com.bottari.presentation.base.UiState
import com.bottari.presentation.extension.takeSuccess
import com.bottari.presentation.model.ItemUiModel

class ChecklistViewModel(
    stateHandle: SavedStateHandle,
) : ViewModel() {
    private val _bottariTitle: MutableLiveData<UiState<String>> = MutableLiveData()
    val bottariTitle: LiveData<UiState<String>> = _bottariTitle

    private val _checklist: MutableLiveData<UiState<List<ItemUiModel>>> =
        MutableLiveData(UiState.Loading)
    val checklist: LiveData<UiState<List<ItemUiModel>>> = _checklist

    private val _nonChecklist: MutableLiveData<List<ItemUiModel>> = MutableLiveData(emptyList())
    val nonChecklist: LiveData<List<ItemUiModel>> = _nonChecklist

    val checkedQuantity: LiveData<Int> =
        _checklist.map { item ->
            item.takeSuccess().orEmpty().count { it.isChecked }
        }

    val isAllChecked: LiveData<Boolean> =
        _checklist.map { item ->
            item.takeSuccess().orEmpty().all { it.isChecked }
        }

    init {
        val bottariId = stateHandle.get<Long>(EXTRAS_BOTTARI_ID)
        bottariId?.let { fetchBottari(it) }
    }

    fun checkItem(itemId: Long) {
        val currentList = _checklist.value?.takeSuccess() ?: return
        val updatedList =
            currentList.map { item ->
                if (item.id != itemId) return@map item
                item.copy(isChecked = item.isChecked.not())
            }

        _checklist.value = UiState.Success(updatedList)
    }

    fun filterNonChecklist() {
        val currentList = _checklist.value?.takeSuccess() ?: return
        val updatedList = currentList.filter { !it.isChecked }
        _nonChecklist.value = updatedList
    }

    private fun fetchBottari(bottariId: Long) {
        _bottariTitle.value = UiState.Success("테스트 보따리")
        _checklist.value = UiState.Success(dummyChecklist)
    }

    companion object {
        private const val EXTRAS_BOTTARI_ID = "EXTRAS_BOTTARI_ID"

        fun Factory(bottariId: Long): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras,
                ): T {
                    val handle = extras.createSavedStateHandle()
                    handle[EXTRAS_BOTTARI_ID] = bottariId

                    return ChecklistViewModel(handle) as T
                }
            }
    }
}

private val dummyChecklist =
    listOf(
        ItemUiModel(id = 0, isChecked = true, name = "우유"),
        ItemUiModel(id = 1, isChecked = false, name = "계란"),
        ItemUiModel(id = 2, isChecked = true, name = "식빵"),
        ItemUiModel(id = 3, isChecked = false, name = "세제"),
        ItemUiModel(id = 4, isChecked = true, name = "샴푸"),
        ItemUiModel(id = 5, isChecked = false, name = "물티슈"),
        ItemUiModel(id = 6, isChecked = true, name = "칫솔"),
        ItemUiModel(id = 7, isChecked = false, name = "치약"),
        ItemUiModel(id = 8, isChecked = true, name = "라면"),
        ItemUiModel(id = 9, isChecked = false, name = "과자"),
        ItemUiModel(id = 10, isChecked = true, name = "커피"),
        ItemUiModel(id = 11, isChecked = false, name = "쌀"),
        ItemUiModel(id = 12, isChecked = true, name = "김치"),
        ItemUiModel(id = 13, isChecked = false, name = "휴지"),
        ItemUiModel(id = 14, isChecked = true, name = "세탁세제"),
        ItemUiModel(id = 15, isChecked = false, name = "청소기 필터"),
        ItemUiModel(id = 16, isChecked = true, name = "텀블러"),
        ItemUiModel(id = 17, isChecked = false, name = "포스트잇"),
        ItemUiModel(id = 18, isChecked = true, name = "USB 케이블"),
        ItemUiModel(id = 19, isChecked = false, name = "보조 배터리"),
    )
