package com.bottari.presentation.view.edit.personal.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.bottari.presentation.base.UiState
import com.bottari.presentation.model.AlarmTypeUiModel
import com.bottari.presentation.model.BottariUiModel
import com.bottari.presentation.model.ItemUiModel
import java.time.LocalDate
import java.time.LocalTime

class PersonalBottariEditViewModel(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _bottari = MutableLiveData<UiState<BottariUiModel>>()
    val bottari: LiveData<UiState<BottariUiModel>> = _bottari

    private val _items = MutableLiveData<UiState<List<ItemUiModel>>>()
    val items: LiveData<UiState<List<ItemUiModel>>> = _items

    private val _alarms = MutableLiveData<UiState<List<AlarmTypeUiModel>>>()
    val alarms: LiveData<UiState<List<AlarmTypeUiModel>>> = _alarms

    init {
        val id =
            savedStateHandle.get<Long>(EXTRAS_BOTTARI_ID)
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
        _alarms.value = UiState.Success(dummyAlarm)
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
                    return PersonalBottariEditViewModel(handle) as T
                }
            }
    }
}

private val dummyBottari = BottariUiModel(1, "나의 보따리", 0, 0, null)

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

private val dummyAlarm =
    listOf(
        AlarmTypeUiModel.EveryDayRepeat(time = LocalTime.of(12, 0)),
        AlarmTypeUiModel.EveryWeekRepeat(days = listOf(1, 3, 4), time = LocalTime.of(12, 0)),
        AlarmTypeUiModel.NonRepeat(
            date = LocalDate.of(2024, 12, 4),
            time = LocalTime.of(12, 0),
        ),
    )
