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
import com.bottari.presentation.model.BottariItemUiModel

class ChecklistViewModel(
    stateHandle: SavedStateHandle,
) : ViewModel() {
    private val _bottariTitle: MutableLiveData<UiState<String>> = MutableLiveData()
    val bottariTitle: LiveData<UiState<String>> = _bottariTitle

    private val _checklist: MutableLiveData<UiState<List<BottariItemUiModel>>> =
        MutableLiveData(UiState.Loading)
    val checklist: LiveData<UiState<List<BottariItemUiModel>>> = _checklist

    private val _nonChecklist: MutableLiveData<List<BottariItemUiModel>> = MutableLiveData(emptyList())
    val nonChecklist: LiveData<List<BottariItemUiModel>> = _nonChecklist

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
        BottariItemUiModel(id = 0, isChecked = true, name = "우유"),
        BottariItemUiModel(id = 1, isChecked = false, name = "계란"),
        BottariItemUiModel(id = 2, isChecked = true, name = "식빵"),
        BottariItemUiModel(id = 3, isChecked = false, name = "세제"),
        BottariItemUiModel(id = 4, isChecked = true, name = "샴푸"),
        BottariItemUiModel(id = 5, isChecked = false, name = "물티슈"),
        BottariItemUiModel(id = 6, isChecked = true, name = "칫솔"),
        BottariItemUiModel(id = 7, isChecked = false, name = "치약"),
        BottariItemUiModel(id = 8, isChecked = true, name = "라면"),
        BottariItemUiModel(id = 9, isChecked = false, name = "과자"),
        BottariItemUiModel(id = 10, isChecked = true, name = "커피"),
        BottariItemUiModel(id = 11, isChecked = false, name = "쌀"),
        BottariItemUiModel(id = 12, isChecked = true, name = "김치"),
        BottariItemUiModel(id = 13, isChecked = false, name = "휴지"),
        BottariItemUiModel(id = 14, isChecked = true, name = "세탁세제"),
        BottariItemUiModel(id = 15, isChecked = false, name = "청소기 필터"),
        BottariItemUiModel(id = 16, isChecked = true, name = "텀블러"),
        BottariItemUiModel(id = 17, isChecked = false, name = "포스트잇"),
        BottariItemUiModel(id = 18, isChecked = true, name = "USB 케이블"),
        BottariItemUiModel(id = 19, isChecked = false, name = "보조 배터리"),
    )
