package com.bottari.feature.personal.edit.alarm

import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bottari.bottari.designsystem.component.BottariCard
import com.bottari.bottari.designsystem.component.BottariCircularLoader
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.core.ui.R
import com.bottari.core.ui.model.alarm.AlarmUiModel
import com.bottari.core.ui.model.alarm.RepeatDayUiModel
import com.bottari.feature.personal.edit.alarm.component.DatePickerModal
import com.bottari.feature.personal.edit.alarm.component.DateSelector
import com.bottari.feature.personal.edit.alarm.component.PermissionSettingDialog
import com.bottari.feature.personal.edit.alarm.component.RepeatDaySelector
import com.commandiron.wheel_picker_compose.WheelTimePicker
import com.commandiron.wheel_picker_compose.core.WheelPickerDefaults
import kotlinx.coroutines.launch
import java.time.LocalTime

@Composable
fun AlarmEditScreen(
    bottariId: Long,
    bottariTitle: String,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    viewModel: AlarmEditViewModel =
        hiltViewModel<AlarmEditViewModel, AlarmEditViewModel.Factory> {
            it.create(bottariId, bottariTitle)
        },
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val activity = LocalActivity.current ?: return
    var showDatePickerDialog by rememberSaveable { mutableStateOf(false) }
    var showRuntimePermissionSettingDialog by rememberSaveable { mutableStateOf(false) }
    var showSpecialPermissionSettingDialog by rememberSaveable { mutableStateOf(false) }

    val permissionLauncher =
        rememberPermissionLauncher(
            activity = activity,
            snackbarHostState = snackbarHostState,
            onGranted = { viewModel.updateAlarmActivate(true) },
            onRequireRuntimePermission = { showRuntimePermissionSettingDialog = true },
            onRequireSpecialPermission = { showSpecialPermissionSettingDialog = true },
        )

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                AlarmUiEvent.FetchAlarmFailure -> {
                    snackbarHostState.showSnackbar(
                        context.getString(R.string.alarm_edit_fetch_failure_text),
                    )
                }

                AlarmUiEvent.SaveAlarmFailure -> {
                    snackbarHostState.showSnackbar(
                        context.getString(R.string.alarm_edit_save_failure_text),
                    )
                }
            }
        }
    }

    if (showRuntimePermissionSettingDialog) {
        PermissionSettingDialog(
            onNavigateClick = { PermissionUtil.openAppSettings(context) },
            onDismiss = { showRuntimePermissionSettingDialog = false },
            title = "권한 안내",
            description = "알림을 받으려면 권한이 필요해요.\n설정 화면으로 이동하시겠어요?",
        )
    }

    if (showSpecialPermissionSettingDialog) {
        PermissionSettingDialog(
            onNavigateClick = { PermissionUtil.requestExactAlarmPermission(context) },
            onDismiss = { showSpecialPermissionSettingDialog = false },
            title = "특별 권한 안내",
            description = "알림을 설정하려면 알람 및 리마인더 권한이 필요해요.\n설정 화면으로 이동하시겠어요?",
        )
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
        onSwitchAlarmActivate = onSwitch@{ isActive ->
            if (isActive.not()) {
                viewModel.updateAlarmActivate(isActive)
                return@onSwitch
            }
            permissionLauncher.launch(PermissionUtil.requiredPermissions)
        },
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

    Box(
        modifier =
            modifier.padding(
                horizontal = BottariTheme.spacing.spaceLarge,
                vertical = BottariTheme.spacing.spaceMedium,
            ),
    ) {
        if (state.isLoading) {
            BottariCircularLoader()
            return@Box
        }

        BottariCard(modifier = Modifier.fillMaxWidth()) {
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
                        shape = BottariTheme.shapes.circle,
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
                    shape = BottariTheme.shapes.radiusLarge,
                    color = BottariTheme.colors.primary.copy(alpha = 0.1f),
                    border = BorderStroke(width = 0.dp, color = BottariTheme.colors.transparent),
                ),
            onSnappedTime = onTimeChange,
        )

        Spacer(modifier = Modifier.height(BottariTheme.spacing.spaceXSmall))

        Text(
            text = "알람 시간",
            color = BottariTheme.colors.gray700,
            style = BottariTheme.typography.regular16.toTextStyle(),
        )

        Spacer(modifier = Modifier.height(BottariTheme.spacing.spaceMedium))

        DateSelector(
            alarm = alarm,
            onCalendarClick = onCalendarClick,
        )

        Spacer(modifier = Modifier.height(BottariTheme.spacing.spaceXSmall))

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

@Composable
private fun rememberPermissionLauncher(
    activity: Activity,
    snackbarHostState: SnackbarHostState,
    onGranted: () -> Unit,
    onRequireRuntimePermission: () -> Unit,
    onRequireSpecialPermission: () -> Unit,
): ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>> {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    return rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        val allGranted = permissions.all { it.value }
        when {
            allGranted -> {
                if (PermissionUtil.hasExactAlarmPermission(context)) {
                    onGranted()
                } else {
                    onRequireSpecialPermission()
                }
            }

            PermissionUtil.isPermanentlyDenied(activity) -> {
                onRequireRuntimePermission()
            }

            else -> {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("권한 요청에 실패했어요")
                }
            }
        }
    }
}

private object PermissionUtil {
    val requiredPermissions: Array<String> by lazy {
        buildList {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }.toTypedArray()
    }

    fun hasExactAlarmPermission(context: Context): Boolean =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = context.getSystemService(AlarmManager::class.java)
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }

    fun requestExactAlarmPermission(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            navigateToSettings(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM, context)
        }
    }

    fun openAppSettings(context: Context) {
        navigateToSettings(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, context)
    }

    fun isPermanentlyDenied(activity: Activity): Boolean =
        requiredPermissions.any { permission ->
            ContextCompat.checkSelfPermission(
                activity,
                permission,
            ) != PackageManager.PERMISSION_GRANTED &&
                !activity.shouldShowRequestPermissionRationale(permission)
        }

    private fun navigateToSettings(
        settingFlag: String,
        context: Context,
    ) {
        val intent =
            Intent(settingFlag).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                data = "package:${context.packageName}".toUri()
            }
        context.startActivity(intent)
    }
}

@Preview(showBackground = true)
@Composable
private fun AlarmEditScreenPreview() {
    var alarmState by remember { mutableStateOf(AlarmUiState()) }

    BottariTheme {
        AlarmEditScreen(
            state = alarmState,
            onSwitchAlarmActivate = {
                alarmState = alarmState.copy(alarm = alarmState.alarm.copy(isActive = it))
            },
            onTimeChange = {},
            onCalendarClick = {},
            onRepeatDaysChange = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
