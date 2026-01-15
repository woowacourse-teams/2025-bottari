package com.bottari.feature.personal.edit.alarm.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bottari.bottari.designsystem.component.BottariButton
import com.bottari.bottari.designsystem.theme.BottariTheme
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

@Composable
fun DatePickerModal(
    selectedDate: LocalDate,
    onDateChange: (LocalDate) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    now: LocalDateTime = LocalDateTime.now(ZoneId.systemDefault()),
) {
    val datePickerState =
        rememberDatePickerState(
            yearRange = IntRange(now.year, now.year),
            initialDisplayMode = DisplayMode.Picker,
            initialSelectedDateMillis = selectedDate.toUtcTimeMillis(),
            selectableDates =
                object : SelectableDates {
                    override fun isSelectableDate(utcTimeMillis: Long): Boolean = utcTimeMillis >= now.toLocalDate().toUtcTimeMillis()
                },
        )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            BottariButton(
                text = "확인",
                onClick = {
                    val selectedTimeMillis =
                        datePickerState.selectedDateMillis ?: System.currentTimeMillis()
                    onDateChange(selectedTimeMillis.toLocalDate())
                },
            )
        },
        modifier = modifier,
        dismissButton = {
            BottariButton(
                text = "취소",
                onClick = onDismiss,
            )
        },
        shape = BottariTheme.shapes.radiusLarge,
        colors = DatePickerDefaults.colors(containerColor = BottariTheme.colors.white),
    ) {
        DatePickerContent(
            datePickerState = datePickerState,
        )
    }
}

@Composable
private fun DatePickerContent(
    datePickerState: DatePickerState,
    modifier: Modifier = Modifier,
) {
    DatePicker(
        state = datePickerState,
        modifier = modifier,
        colors =
            DatePickerDefaults.colors(
                containerColor = BottariTheme.colors.white,
                selectedDayContentColor = BottariTheme.colors.white,
                selectedDayContainerColor = BottariTheme.colors.primary,
                todayDateBorderColor = BottariTheme.colors.primary,
                disabledDayContentColor = BottariTheme.colors.gray400,
            ),
        title = null,
        headline = null,
        showModeToggle = false,
    )
}

private fun LocalDate.toUtcTimeMillis(): Long =
    this
        .atStartOfDay()
        .toInstant(ZoneOffset.UTC)
        .toEpochMilli()

private fun Long.toLocalDate(): LocalDate = Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()

@Preview
@Composable
private fun DatePickerContentPreview() {
    val datePickerState =
        rememberDatePickerState(
            selectableDates =
                object : SelectableDates {
                    override fun isSelectableDate(utcTimeMillis: Long): Boolean = utcTimeMillis >= LocalDate.now().toUtcTimeMillis()
                },
        )

    BottariTheme {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(BottariTheme.spacing.spaceLarge),
            contentAlignment = Alignment.Center,
        ) {
            DatePickerContent(datePickerState = datePickerState)
        }
    }
}
