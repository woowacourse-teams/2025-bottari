package com.bottari.presentation.view.edit.personal.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.bottari.presentation.base.UiState
import com.bottari.presentation.model.AlarmUiModel
import com.bottari.presentation.model.BottariItemUiModel
import com.bottari.presentation.model.BottariUiModel

class PersonalBottariEditViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _bottari = MutableLiveData<UiState<BottariUiModel>>()
    val bottari: LiveData<UiState<BottariUiModel>> = _bottari

    private val _items = MutableLiveData<UiState<List<BottariItemUiModel>>>()
    val items: LiveData<UiState<List<BottariItemUiModel>>> = _items

    private val _alarms = MutableLiveData<UiState<List<AlarmUiModel>>>()
    val alarms: LiveData<UiState<List<AlarmUiModel>>> = _alarms

    init {
        val id =
            savedStateHandle.get<Long>(EXTRA_BOTTARI_ID)
                ?: error("bottariId가 없습니다.")
        fetchBottariById(id)
        fetchItemsById(id)
        fetchAlarmById(id)
    }

    private fun fetchBottariById(id: Long) {
        _bottari.value = UiState.Success(dummyBottari)
    }

    private fun fetchItemsById(id: Long) {
        _items.value = UiState.Success(dummyChecklist)
    }

    private fun fetchAlarmById(id: Long) {
        _alarms.value = UiState.Success(listOf())
    }

    companion object {
        private const val EXTRA_BOTTARI_ID = "EXTRA_BOTTARI_ID"

        fun Factory(bottariId: Long): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras,
                ): T {
                    val handle = extras.createSavedStateHandle()
                    handle[EXTRA_BOTTARI_ID] = bottariId
                    return PersonalBottariEditViewModel(handle) as T
                }
            }
    }
}

private val dummyBottari = BottariUiModel(1, "나의 보따리", 0, 0, null)

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
