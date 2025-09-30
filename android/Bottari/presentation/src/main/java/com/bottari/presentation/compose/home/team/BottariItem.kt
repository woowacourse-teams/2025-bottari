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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.presentation.R
import com.bottari.presentation.common.extension.formatWithPattern
import com.bottari.presentation.compose.common.component.BottariBox
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
        onPersonalBottariDelete = {},
        onTeamBottariDelete = {},
        onPersonalBottariEdit = { _, _ -> },
        onTeamBottariEdit = { _, _ -> },
    )
}

@Composable
fun BottariItem(
    bottari: MyBottariUiModel,
    onPersonalBottariDelete: (Long) -> Unit,
    onTeamBottariDelete: (Long) -> Unit,
    onPersonalBottariEdit: (Long, Boolean) -> Unit,
    onTeamBottariEdit: (Long, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showMenu by remember { mutableStateOf(false) }

    BottariBox(
        modifier = modifier,
        contentPadding = PaddingValues(BottariTheme.spacing.spaceSmall),
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(
                            chooseBottariStateColor(bottari.checkedQuantity, bottari.totalQuantity),
                        ),
                )
                BottariTypeText(bottari)
                Spacer(modifier = Modifier.weight(1f))
                DateText(
                    alarmUiModel = bottari.alarm,
                    modifier = Modifier.padding(end = BottariTheme.spacing.spaceMedium),
                )
                Box {
                    Image(
                        painter = painterResource(R.drawable.ic_more_horizontal),
                        contentDescription = "더보기 메뉴",
                        modifier =
                            Modifier
                                .rotate(90f)
                                .clickable { showMenu = true },
                    )

                    BottariMenuPopup(
                        bottari = bottari,
                        showMenu = showMenu,
                        onDismissRequest = { showMenu = false },
                        onPersonalBottariDelete = onPersonalBottariDelete,
                        onTeamBottariDelete = onTeamBottariDelete,
                        onPersonalBottariEdit = onPersonalBottariEdit,
                        onTeamBottariEdit = onTeamBottariEdit,
                    )
                }
            }
            Text(
                bottari.title,
                modifier =
                    Modifier.padding(
                        start = BottariTheme.spacing.spaceSmall,
                        top = BottariTheme.spacing.spaceLarge,
                        bottom = BottariTheme.spacing.spaceSmall,
                    ),
                style = BottariTheme.typography.semiBold24.toTextStyle(),
            )
            Row(
                Modifier.padding(
                    start = BottariTheme.spacing.spaceSmall,
                    end = BottariTheme.spacing.spaceSmall,
                    bottom = BottariTheme.spacing.spaceXSmall,
                ),
                verticalAlignment = Alignment.Bottom,
            ) {
                BottariCheckIndicator(
                    Modifier
                        .weight(1f)
                        .size(BottariTheme.spacing.space2xSmall),
                    bottari.checkedQuantity,
                    bottari.totalQuantity,
                )
                Text(
                    modifier =
                        Modifier.padding(
                            start = BottariTheme.spacing.spaceSmall,
                            end = BottariTheme.spacing.space2xSmall,
                        ),
                    text = "${bottari.checkedQuantity}/${bottari.totalQuantity}",
                    style = BottariTheme.typography.medium14.toTextStyle(),
                )
            }
        }
    }
}

@Composable
private fun BottariTypeText(bottari: MyBottariUiModel) {
    val teamTypeText =
        when (bottari) {
            is TeamBottariUiModel -> stringResource(R.string.team_bottari_text)
            is BottariUiModel -> stringResource(R.string.personal_bottari_text)
            else -> return
        }
    Text(text = teamTypeText, modifier = Modifier.padding(start = BottariTheme.spacing.spaceXSmall))
}

@Composable
private fun chooseBottariStateColor(
    checkedQuantity: Int,
    totalQuantity: Int,
): Color {
    if (checkedQuantity == 0) return BottariTheme.colors.gray400
    if (checkedQuantity == totalQuantity) return BottariTheme.colors.primary
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
                    .background(BottariTheme.colors.gray400),
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
fun DateText(
    alarmUiModel: AlarmUiModel?,
    modifier: Modifier = Modifier,
) {
    if (alarmUiModel == null) return

    val dateFormat = stringResource(R.string.common_format_date_alarm)
    val timeFormat = stringResource(R.string.common_format_time_alarm)
    val separator = stringResource(R.string.common_separator_text)

    val text =
        when (alarmUiModel.type) {
            AlarmTypeUiModel.NON_REPEAT ->
                formatNonRepeat(
                    alarmUiModel.date,
                    alarmUiModel.time,
                    dateFormat,
                    timeFormat,
                    separator,
                )

            AlarmTypeUiModel.REPEAT -> {
                if (alarmUiModel.isRepeatEveryDay) {
                    formatEveryDayRepeat(alarmUiModel.time, timeFormat, separator)
                } else {
                    formatEveryWeekRepeat(
                        alarmUiModel.time,
                        alarmUiModel.repeatDays,
                        timeFormat,
                        separator,
                    )
                }
            }
        }
    Text(text = text, modifier = modifier)
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
