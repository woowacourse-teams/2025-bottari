package com.bottari.presentation.compose.home.team

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.bottari.presentation.R
import com.bottari.presentation.common.extension.formatWithPattern
import com.bottari.presentation.compose.common.component.BottariBox
import com.bottari.presentation.compose.common.modifier.dropShadow
import com.bottari.presentation.compose.common.theme.BottariTheme
import com.bottari.presentation.model.alarm.AlarmTypeUiModel
import com.bottari.presentation.model.alarm.AlarmUiModel
import com.bottari.presentation.model.alarm.RepeatDayUiModel
import com.bottari.presentation.model.bottari.MyBottariUiModel
import com.bottari.presentation.model.bottari.personal.BottariUiModel
import com.bottari.presentation.model.bottari.team.TeamBottariUiModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.TextStyle
import java.util.Locale

@Preview
@Composable
fun TeamBottariScreenPreview() {
    BottariItem(
        bottari =
            TeamBottariUiModel(
                id = 1,
                title = "미리보기보따리",
                totalQuantity = 10,
                checkedQuantity = 7,
                memberCount = 4,
                alarm =
                    AlarmUiModel(
                        type = AlarmTypeUiModel.REPEAT,
                        isActive = true,
                        time = LocalTime.now(),
                        date = LocalDate.now(),
                        repeatDays = DayOfWeek.entries.map { RepeatDayUiModel(it, true) },
                    ),
            ),
    )
}

@Composable
fun BottariItem(
    bottari: MyBottariUiModel,
    modifier: Modifier = Modifier,
) {
    // 1. 팝업 메뉴의 표시 여부를 관리하는 상태 변수 추가
    var showMenu by remember { mutableStateOf(false) }

    BottariBox(modifier = modifier, contentPadding = PaddingValues(13.dp)) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    Modifier
                        .size(8.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .background(
                            chooseBottariStateColor(bottari.checkedQuantity, bottari.totalQuantity),
                        ),
                )
                BottariTypeText(bottari)
                Spacer(modifier = Modifier.weight(1f))
                DateText(
                    alarmUiModel = bottari.alarm,
                )

                // 2. 이미지를 Box로 감싸서 위치 기준(앵커)을 만들고, 팝업을 포함시킴
                Box {
                    Image(
                        painter = painterResource(R.drawable.ic_more_horizontal),
                        contentDescription = "더보기 메뉴",
                        modifier =
                            Modifier
                                .rotate(90f)
                                .clickable { showMenu = true }, // 3. 클릭 시 상태를 true로 변경
                    )

                    // 4. showMenu가 true일 때 Popup을 표시
                    if (showMenu) {
                        Popup(
                            alignment = Alignment.TopEnd, // 앵커의 우측 하단에 위치
                            onDismissRequest = { showMenu = false }, // 바깥 영역 클릭 시 닫기
                        ) {
                            // 팝업으로 보여줄 커스텀 메뉴 UI
                            MoreMenuPopup(
                                onEdit = {
                                    showMenu = false
                                },
                                onDelete = {
                                    showMenu = false
                                },
                            )
                        }
                    }
                }
            }
            Text(
                bottari.title,
                modifier = Modifier.padding(start = 13.dp, top = 20.dp, bottom = 10.dp),
                style = BottariTheme.typography.semiBold24.toTextStyle(),
            )
            Row(
                Modifier.padding(start = 13.dp, end = 13.dp, bottom = 7.dp),
                verticalAlignment = Alignment.Bottom,
            ) {
                BottariCheckIndicator(
                    Modifier
                        .weight(1f)
                        .size(4.dp),
                    bottari.checkedQuantity,
                    bottari.totalQuantity,
                )
                Text(
                    modifier = Modifier.padding(start = 13.dp, end = 5.dp),
                    text = "${bottari.checkedQuantity}/${bottari.totalQuantity}",
                    style = BottariTheme.typography.medium14.toTextStyle(),
                )
            }
        }
    }
}

@Composable
private fun MoreMenuPopup(
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .clip(RoundedCornerShape(8.dp))
                .size(200.dp, 100.dp)
                .fillMaxWidth()
                .dropShadow(RoundedCornerShape(8.dp), Color.Black.copy(0.05f), 2.dp, 0.dp, 1.dp)
                .background(Color.White),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // 첫 번째 절반 영역 (왼쪽)
        Box(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxHeight() // 세로로 꽉 채워 클릭 영역을 넓힘
                    .clickable(onClick = onEdit),
            // 1. Box에 클릭 이벤트 적용
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_pen),
                contentDescription = "수정하기",
                // 2. Icon에서는 clickable 제거
            )
        }

        // 두 번째 절반 영역 (오른쪽)
        Box(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxHeight() // 세로로 꽉 채워 클릭 영역을 넓힘
                    .clickable(onClick = onDelete),
            // 1. Box에 클릭 이벤트 적용
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_delete),
                contentDescription = "삭제하기",
                // 2. Icon에서는 clickable 제거
            )
        }
    }
}

@Composable
private fun BottariTypeText(bottari: MyBottariUiModel) {
    val teamTypeText =
        when (bottari) {
            is TeamBottariUiModel -> "팀"
            is BottariUiModel -> "개인"
            else -> return
        }
    Text(text = teamTypeText, modifier = Modifier.padding(start = 8.dp))
}

@Composable
private fun chooseBottariStateColor(
    checkedQuantity: Int,
    totalQuantity: Int,
): Color {
    if (totalQuantity == 0) return colorResource(R.color.gray_400)
    if (checkedQuantity == totalQuantity) return colorResource(R.color.primary)
    return Color.Red
}

@Composable
fun BottariCheckIndicator(
    modifier: Modifier = Modifier,
    checkedQuantity: Int,
    totalQuantity: Int,
) {
    Box(modifier = modifier, contentAlignment = Alignment.CenterStart) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .clip(shape = RoundedCornerShape(16.dp))
                    .background(colorResource(R.color.gray_400)),
        )
        Box(
            modifier =
                Modifier
                    .fillMaxWidth(generateIndicatorSize(checkedQuantity, totalQuantity))
                    .fillMaxHeight()
                    .clip(shape = RoundedCornerShape(16.dp))
                    .background(chooseBottariStateColor(checkedQuantity, totalQuantity)),
        )
    }
}

private fun generateIndicatorSize(
    checkedQuantity: Int,
    totalQuantity: Int,
): Float {
    if (totalQuantity == 0) return 0F
    return (checkedQuantity.toFloat() / totalQuantity.toFloat())
}

@Composable
fun DateText(alarmUiModel: AlarmUiModel?) {
    if (alarmUiModel == null) return

    val dateFormat = stringResource(R.string.common_format_date_alarm)
    val timeFormat = stringResource(R.string.common_format_time_alarm)
    val separator = stringResource(R.string.common_separator_text)

    val text =
        when (alarmUiModel.type) {
            AlarmTypeUiModel.NON_REPEAT -> formatNonRepeat(alarmUiModel.date, alarmUiModel.time, dateFormat, timeFormat, separator)
            AlarmTypeUiModel.REPEAT -> {
                if (alarmUiModel.isRepeatEveryDay) {
                    formatEveryDayRepeat(alarmUiModel.time, timeFormat, separator)
                } else {
                    formatEveryWeekRepeat(alarmUiModel.time, alarmUiModel.repeatDays, timeFormat, separator)
                }
            }
        }
    Text(text)
}

@Composable
private fun formatNonRepeat(
    date: LocalDate,
    time: LocalTime,
    dateFormat: String,
    timeFormat: String,
    separator: String,
): String =
    buildString {
        append(date.formatWithPattern(dateFormat))
        append(separator)
        append(time.formatWithPattern(timeFormat))
    }

@Composable
private fun formatEveryDayRepeat(
    time: LocalTime,
    timeFormat: String,
    separator: String,
): String =
    buildString {
        append(time.formatWithPattern(timeFormat))
        append(separator)
        append(stringResource(R.string.bottari_item_alarm_repeat_everyday_text))
    }

@Composable
private fun formatEveryWeekRepeat(
    time: LocalTime,
    repeatDays: List<RepeatDayUiModel>,
    timeFormat: String,
    separator: String,
): String {
    val checkedDays = repeatDays.filter { it.isChecked }
    return buildString {
        append(time.formatWithPattern(timeFormat))
        append(separator)
        append(stringResource(R.string.bottari_item_alarm_repeat_everyweek_text))
        append(separator)
        append(
            checkedDays.joinToString { dayOfWeek ->
                dayOfWeek.dayOfWeek
                    .getDisplayName(TextStyle.SHORT, Locale.getDefault())
            },
        )
    }
}
