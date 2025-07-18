package com.bottari.presentation.view.home.bottari

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bottari.presentation.base.UiState
import com.bottari.presentation.model.AlarmTypeUiModel
import com.bottari.presentation.model.BottariUiModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

class BottariViewModel : ViewModel() {
    private val _bottaries: MutableLiveData<UiState<List<BottariUiModel>>> =
        MutableLiveData(UiState.Loading)
    val bottaries: LiveData<UiState<List<BottariUiModel>>> get() = _bottaries

    init {
        fetchBottaries()
    }

    private fun fetchBottaries() {
        val uiState = UiState.Success(dummyBottariItems)
        _bottaries.value = uiState
    }
}

private val dummyBottariItems =
    listOf(
        BottariUiModel(
            id = 0L,
            title = "아침 약 챙기기",
            totalQuantity = 7,
            checkedQuantity = 3,
            alarmTypeUiModel =
                AlarmTypeUiModel.EveryDayRepeat(
                    time = LocalTime.of(8, 0),
                ),
        ),
        BottariUiModel(
            id = 1L,
            title = "점심 도시락 싸기",
            totalQuantity = 5,
            checkedQuantity = 5,
            alarmTypeUiModel =
                AlarmTypeUiModel.EveryWeekRepeat(
                    days =
                        listOf(
                            DayOfWeek.MONDAY.value,
                            DayOfWeek.TUESDAY.value,
                            DayOfWeek.WEDNESDAY.value,
                            DayOfWeek.THURSDAY.value,
                            DayOfWeek.FRIDAY.value,
                        ),
                    time = LocalTime.of(7, 30),
                ),
        ),
        BottariUiModel(
            id = 2L,
            title = "저녁 운동하기",
            totalQuantity = 3,
            checkedQuantity = 1,
            alarmTypeUiModel =
                AlarmTypeUiModel.EveryDayRepeat(
                    time = LocalTime.of(19, 0),
                ),
        ),
        BottariUiModel(
            id = 3L,
            title = "주간 보고서 제출",
            totalQuantity = 1,
            checkedQuantity = 0,
            alarmTypeUiModel =
                AlarmTypeUiModel.EveryWeekRepeat(
                    days = listOf(DayOfWeek.FRIDAY.value),
                    time = LocalTime.of(17, 0),
                ),
        ),
        BottariUiModel(
            id = 4L,
            title = "책 읽기",
            totalQuantity = 10,
            checkedQuantity = 8,
            alarmTypeUiModel =
                AlarmTypeUiModel.EveryDayRepeat(
                    time = LocalTime.of(21, 30),
                ),
        ),
        BottariUiModel(
            id = 5L,
            title = "식료품 구매",
            totalQuantity = 6,
            checkedQuantity = 3,
            alarmTypeUiModel =
                AlarmTypeUiModel.EveryWeekRepeat(
                    days = listOf(DayOfWeek.SATURDAY.value),
                    time = LocalTime.of(11, 0),
                ),
        ),
        BottariUiModel(
            id = 6L,
            title = "개발 공부",
            totalQuantity = 20,
            checkedQuantity = 15,
            alarmTypeUiModel =
                AlarmTypeUiModel.EveryDayRepeat(
                    time = LocalTime.of(20, 0),
                ),
        ),
        BottariUiModel(
            id = 7L,
            title = "빨래하기",
            totalQuantity = 3,
            checkedQuantity = 3,
            alarmTypeUiModel =
                AlarmTypeUiModel.EveryWeekRepeat(
                    days = listOf(DayOfWeek.SUNDAY.value),
                    time = LocalTime.of(9, 0),
                ),
        ),
        BottariUiModel(
            id = 8L,
            title = "블로그 글 작성",
            totalQuantity = 1,
            checkedQuantity = 0,
            alarmTypeUiModel =
                AlarmTypeUiModel.EveryWeekRepeat(
                    days = listOf(DayOfWeek.WEDNESDAY.value),
                    time = LocalTime.of(15, 0),
                ),
        ),
        BottariUiModel(
            id = 9L,
            title = "영양제 챙겨 먹기",
            totalQuantity = 2,
            checkedQuantity = 1,
            alarmTypeUiModel =
                AlarmTypeUiModel.EveryDayRepeat(
                    time = LocalTime.of(9, 0),
                ),
        ),
        BottariUiModel(
            id = 10L,
            title = "강아지 산책",
            totalQuantity = 2,
            checkedQuantity = 2,
            alarmTypeUiModel =
                AlarmTypeUiModel.EveryDayRepeat(
                    time = LocalTime.of(7, 0),
                ),
        ),
        BottariUiModel(
            id = 11L,
            title = "가계부 작성",
            totalQuantity = 1,
            checkedQuantity = 0,
            alarmTypeUiModel =
                AlarmTypeUiModel.EveryDayRepeat(
                    time = LocalTime.of(22, 0),
                ),
        ),
        BottariUiModel(
            id = 12L,
            title = "정기 모임",
            totalQuantity = 1,
            checkedQuantity = 0,
            alarmTypeUiModel =
                AlarmTypeUiModel.EveryWeekRepeat(
                    days = listOf(DayOfWeek.TUESDAY.value),
                    time = LocalTime.of(18, 30),
                ),
        ),
        BottariUiModel(
            id = 13L,
            title = "새로운 레시피 시도",
            totalQuantity = 1,
            checkedQuantity = 1,
            alarmTypeUiModel =
                AlarmTypeUiModel.NonRepeat(
                    date = LocalDate.now(),
                    time = LocalTime.now(),
                ),
        ),
    )
