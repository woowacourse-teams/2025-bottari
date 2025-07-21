package com.bottari.presentation.view.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bottari.presentation.model.AlarmTypeUiModel
import com.bottari.presentation.model.BottariUiModel
import com.bottari.presentation.model.ItemUiModel
import java.time.LocalDate
import java.time.LocalTime

class PersonalBottariViewModel : ViewModel() {
    private val _bottari = MutableLiveData<BottariUiModel>()
    val bottari: LiveData<BottariUiModel> = _bottari

    private val _items = MutableLiveData<List<ItemUiModel>>()
    val items: LiveData<List<ItemUiModel>> = _items

    private val _alarms = MutableLiveData<List<AlarmTypeUiModel>>()
    val alarms: LiveData<List<AlarmTypeUiModel>> = _alarms

    fun fetchBottariById(id: Int) {
        _bottari.value = dummyBottariUiModel
    }

    fun fetchItemsById(id: Int) {
        _items.value = dummyChecklist
    }

    fun fetchAlarmById(id: Int) {
        _alarms.value = dummyAlarm
    }

    val dummyBottariUiModel =
        BottariUiModel(
            id = 1,
            title = "내가 만든 보따리",
            totalQuantity = 0,
            checkedQuantity = 0,
            alarmTypeUiModel = null,
        )

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
}
