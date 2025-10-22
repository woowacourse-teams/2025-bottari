package com.bottari.presentation.compose.edit.personal.alarm

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.component.BottariBox
import com.bottari.presentation.compose.common.component.IndeterminateCircularIndicator
import com.bottari.presentation.compose.common.theme.BottariTheme
import com.bottari.presentation.compose.common.theme.LocalBottariBgColor
import com.bottari.presentation.compose.edit.personal.alarm.component.DatePickerModal
import com.bottari.presentation.compose.edit.personal.alarm.component.DateSelector
import com.bottari.presentation.compose.edit.personal.alarm.component.RepeatDaySelector
import com.bottari.presentation.model.alarm.AlarmUiModel
import com.bottari.presentation.model.alarm.RepeatDayUiModel
import com.bottari.presentation.view.edit.alarm.AlarmEditViewModel
import com.bottari.presentation.view.edit.alarm.AlarmUiEvent
import com.bottari.presentation.view.edit.alarm.AlarmUiState
import com.commandiron.wheel_picker_compose.WheelTimePicker
import com.commandiron.wheel_picker_compose.core.WheelPickerDefaults
import java.time.LocalTime

@Composable
fun AlarmEditScreen(
    bottariId: Long,
    bottariTitle: String,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    viewModel: AlarmEditViewModel = viewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val uiEvent = viewModel.uiEvent.collectAsStateWithLifecycle(null)
    var showDatePickerDialog by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.setBottariInfo(bottariId, bottariTitle)
    }

    LaunchedEffect(uiEvent.value) {
        when (uiEvent.value ?: return@LaunchedEffect) {
            AlarmUiEvent.FetchAlarmFailure ->
                snackbarHostState.showSnackbar(
                    context.getString(R.string.alarm_edit_fetch_failure_text),
                )

            AlarmUiEvent.SaveAlarmFailure ->
                snackbarHostState.showSnackbar(
                    context.getString(R.string.alarm_edit_save_failure_text),
                )

            AlarmUiEvent.SaveAlarmSuccess ->
                snackbarHostState.showSnackbar(
                    context.getString(R.string.alarm_edit_save_success_text),
                )
        }
    }

    if (showDatePickerDialog) {
        DatePickerModal(
            selectedDate = uiState.value.alarm.date,
            onDateChange = { date ->
                viewModel.updateAlarmDate(date)
                showDatePickerDialog = false
            },
            onDismiss = { showDatePickerDialog = false },
        )
    }

    AlarmEditScreen(
        state = uiState.value,
        onSwitchAlarmActivate = viewModel::updateAlarmActivate,
        onTimeChange = viewModel::updateAlarmTime,
        onCalendarClick = { showDatePickerDialog = true },
        onRepeatDaysChange = viewModel::updateRepeatDays,
        modifier = modifier.fillMaxSize(),
    )
}

@Composable
private fun AlarmEditScreen(
    state: AlarmUiState,
    onSwitchAlarmActivate: (Boolean) -> Unit,
    onTimeChange: (LocalTime) -> Unit,
    onCalendarClick: () -> Unit,
    onRepeatDaysChange: (RepeatDayUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    val alarm = state.alarm

    Column(
        modifier =
            modifier.padding(
                horizontal = BottariTheme.spacing.spaceLarge,
                vertical = BottariTheme.spacing.spaceMedium,
            ),
        verticalArrangement = Arrangement.Top,
    ) {
        if (state.isLoading) {
            IndeterminateCircularIndicator()
            return@Column
        }

        BottariBox(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(BottariTheme.spacing.spaceXLarge),
        ) {
            Column {
                AlarmEditHeader(
                    isActive = alarm.isActive,
                    onSwitchAlarmActivate = onSwitchAlarmActivate,
                    modifier = Modifier.fillMaxWidth(),
                )

                if (alarm.isActive) {
                    AlarmEditBody(
                        alarm = alarm,
                        onTimeChange = onTimeChange,
                        onCalendarClick = onCalendarClick,
                        onRepeatDaysChange = onRepeatDaysChange,
                        modifier =
                            Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = BottariTheme.spacing.spaceMedium),
                    )
                }
            }
        }
    }
}

@Composable
private fun AlarmEditHeader(
    isActive: Boolean,
    onSwitchAlarmActivate: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .size(48.dp)
                    .background(
                        shape = CircleShape,
                        color = BottariTheme.colors.primary.copy(alpha = 0.1f),
                    ),
        ) {
            Icon(
                imageVector = Icons.Default.Alarm,
                contentDescription = "알람",
                modifier =
                    Modifier
                        .size(20.dp)
                        .align(Alignment.Center),
                tint = BottariTheme.colors.primary,
            )
        }

        Text(
            text = "알람",
            modifier =
                Modifier
                    .padding(start = BottariTheme.spacing.spaceSmall)
                    .weight(1f),
            style = BottariTheme.typography.semiBold18.toTextStyle(),
        )

        Switch(
            checked = isActive,
            onCheckedChange = onSwitchAlarmActivate,
            colors =
                SwitchDefaults.colors(
                    checkedThumbColor = BottariTheme.colors.white,
                    uncheckedThumbColor = BottariTheme.colors.white,
                    checkedTrackColor = BottariTheme.colors.primary,
                    uncheckedTrackColor = BottariTheme.colors.gray200,
                    checkedBorderColor = BottariTheme.colors.transparent,
                    uncheckedBorderColor = BottariTheme.colors.transparent,
                ),
        )
    }
}

@Composable
private fun AlarmEditBody(
    alarm: AlarmUiModel,
    onTimeChange: (LocalTime) -> Unit,
    onCalendarClick: () -> Unit,
    onRepeatDaysChange: (RepeatDayUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        WheelTimePicker(
            startTime = alarm.time,
            size = DpSize(width = 200.dp, height = 250.dp),
            rowCount = 5,
            textStyle = BottariTheme.typography.regular24.toTextStyle(),
            textColor = BottariTheme.colors.black,
            selectorProperties =
                WheelPickerDefaults.selectorProperties(
                    enabled = true,
                    shape = RoundedCornerShape(50),
                    color = BottariTheme.colors.primary.copy(alpha = 0.1f),
                    border = BorderStroke(width = 0.dp, color = BottariTheme.colors.transparent),
                ),
            onSnappedTime = onTimeChange,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "알람 시간",
            color = BottariTheme.colors.gray700,
            style = BottariTheme.typography.regular16.toTextStyle(),
        )

        Spacer(modifier = Modifier.height(16.dp))

        DateSelector(
            alarm = alarm,
            onCalendarClick = onCalendarClick,
        )

        Spacer(modifier = Modifier.height(16.dp))

        RepeatDaySelector(
            alarm = alarm,
            onRepeatDaysChange = onRepeatDaysChange,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = BottariTheme.spacing.space2xSmall),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AlarmEditScreenPreview() {
    var alarmState by remember { mutableStateOf(AlarmUiState()) }

    BottariTheme {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(
                        color = LocalBottariBgColor.current,
                        shape = RoundedCornerShape(0.dp),
                    ),
        ) {
            AlarmEditScreen(
                state = alarmState,
                onSwitchAlarmActivate = {},
                onTimeChange = {},
                onCalendarClick = {},
                onRepeatDaysChange = {},
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
