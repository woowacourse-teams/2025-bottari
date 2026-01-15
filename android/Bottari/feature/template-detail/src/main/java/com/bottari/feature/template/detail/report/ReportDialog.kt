package com.bottari.feature.template.detail.report

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bottari.bottari.designsystem.component.BottariButton
import com.bottari.bottari.designsystem.component.BottariIconButton
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.core.ui.R as UIR

@Composable
fun ReportDialog(
    templateId: Long,
    onDismiss: () -> Unit,
    onResult: (messageRes: Int) -> Unit,
    viewModel: ReportViewModel =
        hiltViewModel<ReportViewModel, ReportViewModel.Factory> {
            it.create(templateId)
        },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle(ReportUiState())

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                ReportUiEvent.ReportTemplateSuccess -> {
                    onResult(UIR.string.common_report_dialog_submit_success_text)
                    onDismiss()
                }

                ReportUiEvent.ReportTemplateFailure -> {
                    onResult(UIR.string.common_report_dialog_submit_failure_text)
                    onDismiss()
                }
            }
        }
    }

    ReportDialog(
        uiState = uiState,
        onDismiss = onDismiss,
        onOptionClick = viewModel::updateSelectedReason,
        onReportClick = viewModel::reportTemplate,
    )
}

@Composable
private fun ReportDialog(
    uiState: ReportUiState,
    onDismiss: () -> Unit,
    onOptionClick: (String) -> Unit,
    onReportClick: () -> Unit,
) {
    Dialog(
        onDismissRequest = {},
        properties =
            DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
            ),
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(0.9f),
            shape = BottariTheme.shapes.radiusMedium,
            color = BottariTheme.colors.white,
            shadowElevation = 2.dp,
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(BottariTheme.spacing.spaceMedium),
                    verticalArrangement = Arrangement.spacedBy(BottariTheme.spacing.spaceXSmall),
                ) {
                    Text(
                        text = stringResource(UIR.string.common_report_dialog_title_text),
                        style = BottariTheme.typography.semiBold20.toTextStyle(),
                        color = BottariTheme.colors.black,
                    )
                    Text(
                        text = stringResource(UIR.string.common_report_dialog_description_text),
                        style = BottariTheme.typography.regular14.toTextStyle(),
                        color = BottariTheme.colors.gray700,
                    )

                    listOf(
                        UIR.string.common_report_dialog_spam_or_ad_radio_button_text,
                        UIR.string.common_report_dialog_inappropriate_content_radio_button_text,
                        UIR.string.common_report_dialog_false_information_radio_button_text,
                        UIR.string.common_report_dialog_privacy_exposure_radio_button_text,
                    ).forEach { resId ->
                        val reason = stringResource(resId)
                        ReportReasonOption(
                            text = reason,
                            selected = uiState.reason == reason,
                            onClick = { onOptionClick(reason) },
                        )
                    }

                    BottariButton(
                        text = stringResource(UIR.string.common_report_dialog_report_button_text),
                        onClick = onReportClick,
                        enabled = uiState.isButtonEnabled && !uiState.isLoading,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                BottariIconButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.TopEnd),
                ) {
                    Icon(
                        painter = painterResource(UIR.drawable.ic_close),
                        contentDescription = stringResource(UIR.string.common_close_btn_description),
                    )
                }
            }
        }
    }
}

@Composable
private fun ReportReasonOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = BottariTheme.spacing.space2xSmall),
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors =
                RadioButtonDefaults.colors(
                    selectedColor = BottariTheme.colors.primary.copy(alpha = 0.8f),
                    unselectedColor = BottariTheme.colors.gray200,
                ),
        )
        Text(
            text = text,
            style = BottariTheme.typography.regular14.toTextStyle(),
            color = BottariTheme.colors.black,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ReportDialogPreview() {
    var uiState by remember { mutableStateOf(ReportUiState()) }

    BottariTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            ReportDialog(
                uiState = uiState,
                onDismiss = {},
                onOptionClick = { uiState = uiState.copy(reason = it) },
                onReportClick = {},
            )
        }
    }
}
