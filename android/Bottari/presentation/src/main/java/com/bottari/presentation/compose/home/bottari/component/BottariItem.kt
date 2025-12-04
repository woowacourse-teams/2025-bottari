package com.bottari.presentation.compose.home.bottari.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.core.designsystem.component.BottariBox
import com.bottari.core.designsystem.component.BottariCheckIndicator
import com.bottari.core.designsystem.component.chooseBottariStateColor
import com.bottari.core.designsystem.theme.BottariTheme
import com.bottari.presentation.R
import com.bottari.presentation.common.extension.formatWithPattern
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
import com.bottari.core.designsystem.R as DsR

@Composable
fun BottariItem(
    bottari: MyBottariUiModel,
    isMenuShown: Boolean,
    onShowMenu: () -> Unit,
    onCloseMenu: () -> Unit,
    onBottariDelete: (Long) -> Unit,
    onBottariEdit: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    BottariBox(
        modifier = modifier,
        contentPadding =
            PaddingValues(
                start = BottariTheme.spacing.spaceMedium,
                bottom = BottariTheme.spacing.spaceSmall,
            ),
    ) {
        Column {
            BottariInfo(
                bottari = bottari,
                isMenuShown = isMenuShown,
                onShowMenu = onShowMenu,
                onCloseMenu = onCloseMenu,
                onBottariDelete = onBottariDelete,
                onBottariEdit = onBottariEdit,
            )
            Text(
                text = bottari.title,
                style = BottariTheme.typography.semiBold20.toTextStyle(),
            )
            Spacer(modifier = Modifier.height(BottariTheme.spacing.space2xSmall))
            BottariCheckInfo(
                checkedQuantity = bottari.checkedQuantity,
                totalQuantity = bottari.totalQuantity,
                format =
                    stringResource(
                        R.string.team_management_member_head_count,
                    ),
                modifier = Modifier.padding(end = BottariTheme.spacing.spaceMedium),
            )
            Spacer(modifier = Modifier.height(BottariTheme.spacing.spaceXSmall))
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun BottariInfo(
    bottari: MyBottariUiModel,
    isMenuShown: Boolean,
    onShowMenu: () -> Unit,
    onCloseMenu: () -> Unit,
    onBottariDelete: (Long) -> Unit,
    onBottariEdit: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        when (bottari) {
            is TeamBottariUiModel -> stringResource(R.string.team_bottari_text)
            is BottariUiModel -> stringResource(R.string.personal_bottari_text)
            else -> null
        }?.let { bottariTypeText ->
            BottariTypeLabel(
                checkedQuantity = bottari.checkedQuantity,
                totalQuantity = bottari.totalQuantity,
                bottariTypeText = bottariTypeText,
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        bottari.alarm?.let { alarm ->
            Text(
                text =
                    dateText(
                        alarmUiModel = alarm,
                        dateFormat = stringResource(DsR.string.common_format_date_alarm),
                        timeFormat = stringResource(DsR.string.common_format_time_alarm),
                        separator = stringResource(DsR.string.common_separator_text),
                        repeatEveryWeekText = stringResource(R.string.bottari_item_alarm_repeat_everyweek_text),
                        repeatEveryDayText = stringResource(R.string.bottari_item_alarm_repeat_everyday_text),
                    ),
                style = BottariTheme.typography.regular14.toTextStyle(),
                color = if (alarm.isActive) BottariTheme.colors.black else BottariTheme.colors.gray400,
            )
        }

        IconButton(
            onClick = { onShowMenu() },
            modifier = Modifier.size(48.dp),
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_more_horizontal),
                contentDescription = stringResource(R.string.bottari_btn_more_description),
                modifier =
                    Modifier
                        .size(24.dp)
                        .rotate(90f),
            )

            BottariMenuDropdown(
                expanded = isMenuShown,
                onDismissRequest = onCloseMenu,
                onBottariDelete = {
                    onBottariDelete(bottari.id)
                },
                onBottariEdit = {
                    onBottariEdit(bottari.id)
                },
            )
        }
    }
}

@Composable
private fun BottariTypeLabel(
    checkedQuantity: Int,
    totalQuantity: Int,
    bottariTypeText: String,
    modifier: Modifier = Modifier,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier =
                modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(
                        color =
                            chooseBottariStateColor(
                                checkedQuantity = checkedQuantity,
                                totalQuantity = totalQuantity,
                            ),
                    ),
        )

        Spacer(modifier = Modifier.width(BottariTheme.spacing.spaceXSmall))

        Text(
            text =
            bottariTypeText,
            style = BottariTheme.typography.medium14.toTextStyle(),
        )
    }
}

private fun dateText(
    alarmUiModel: AlarmUiModel,
    dateFormat: String,
    timeFormat: String,
    separator: String,
    repeatEveryWeekText: String,
    repeatEveryDayText: String,
): String {
    when (alarmUiModel.type) {
        AlarmTypeUiModel.NON_REPEAT -> {
            return buildString {
                append(alarmUiModel.date.formatWithPattern(dateFormat))
                append(separator)
                append(alarmUiModel.time.formatWithPattern(timeFormat))
            }
        }

        AlarmTypeUiModel.REPEAT -> {
            if (!alarmUiModel.isRepeatEveryDay) {
                val checkedDays = alarmUiModel.repeatDays.filter { it.isChecked }
                return buildString {
                    append(alarmUiModel.time.formatWithPattern(timeFormat))
                    append(separator)
                    append(repeatEveryWeekText)
                    append(separator)
                    append(
                        checkedDays.joinToString { dayOfWeek ->
                            dayOfWeek.dayOfWeek
                                .getDisplayName(TextStyle.SHORT, Locale.getDefault())
                        },
                    )
                }
            }

            return buildString {
                append(alarmUiModel.time.formatWithPattern(timeFormat))
                append(separator)
                append(repeatEveryDayText)
            }
        }
    }
}

@Composable
private fun BottariCheckInfo(
    checkedQuantity: Int,
    totalQuantity: Int,
    format: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom,
    ) {
        BottariCheckIndicator(
            checkedQuantity = checkedQuantity,
            totalQuantity = totalQuantity,
            modifier =
                Modifier
                    .weight(1f)
                    .height(BottariTheme.spacing.space2xSmall),
        )
        Spacer(modifier = Modifier.width(BottariTheme.spacing.space2xLarge))
        Text(
            text =
                format.format(
                    checkedQuantity,
                    totalQuantity,
                ),
            style = BottariTheme.typography.regular14.toTextStyle(),
        )
    }
}

@Preview
@Composable
private fun TeamBottariScreenPreview() {
    BottariItem(
        bottari =
            TeamBottariUiModel(
                id = 1,
                title = "미리보기보따리",
                totalQuantity = 10,
                checkedQuantity = 7,
                alarm =
                    AlarmUiModel(
                        type = AlarmTypeUiModel.REPEAT,
                        isActive = true,
                        time = LocalTime.now(),
                        date = LocalDate.now(),
                        repeatDays = DayOfWeek.entries.map { RepeatDayUiModel(it, true) },
                    ),
                memberCount = 4,
            ),
        isMenuShown = false,
        onShowMenu = {},
        onCloseMenu = {},
        onBottariDelete = {},
        onBottariEdit = {},
        modifier = Modifier,
    )
}
