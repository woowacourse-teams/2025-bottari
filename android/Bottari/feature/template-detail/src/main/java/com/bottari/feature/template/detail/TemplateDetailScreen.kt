package com.bottari.feature.template.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bottari.bottari.designsystem.component.BottariButton
import com.bottari.bottari.designsystem.component.BottariCard
import com.bottari.bottari.designsystem.component.BottariCircularLoader
import com.bottari.bottari.designsystem.component.BottariIconButton
import com.bottari.bottari.designsystem.component.BottariTopAppBar
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.core.ui.extension.showSnackbar
import com.bottari.core.ui.extension.topBottomFadingEdge
import com.bottari.core.ui.model.template.BottariTemplateItemUiModel
import com.bottari.feature.template.detail.report.ReportDialog
import kotlinx.coroutines.launch
import com.bottari.core.ui.R as PresentationR
import com.bottari.core.ui.R as UIR

@Composable
fun TemplateDetailScreen(
    snackbarState: SnackbarHostState,
    templateId: Long,
    isMyTemplate: Boolean,
    isBookmark: Boolean,
    navigateToBack: () -> Unit,
    navigateToPersonalEdit: (bottariId: Long) -> Unit,
    viewModel: TemplateDetailViewModel =
        hiltViewModel<TemplateDetailViewModel, TemplateDetailViewModel.Factory> {
            it.create(templateId, isBookmark)
        },
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isMenuExpanded by remember { mutableStateOf(false) }
    var isReportDialogVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                TemplateDetailUiEvent.FetchBottariDetailFailure -> {
                    showSnackbar(
                        snackbarState = snackbarState,
                        message = context.getString(PresentationR.string.template_detail_fetch_failure_text),
                    )
                }

                TemplateDetailUiEvent.TakeBottariTemplateFailure -> {
                    showSnackbar(
                        snackbarState = snackbarState,
                        message = context.getString(PresentationR.string.template_detail_take_failure_text),
                    )
                }

                is TemplateDetailUiEvent.TakeBottariTemplateSuccess -> {
                    navigateToPersonalEdit(event.bottariId)
                }
            }
        }
    }

    TemplateDetailScreen(
        uiState = uiState,
        isMenuVisible = !isBookmark && !isMyTemplate,
        isMenuExpanded = isMenuExpanded,
        onMenuExpandedChange = { new -> isMenuExpanded = new },
        onBackClick = navigateToBack,
        onTakeTemplateClick = viewModel::takeBottariTemplate,
        onReportClick = { isReportDialogVisible = true },
    )

    if (isReportDialogVisible) {
        ReportDialog(
            templateId = templateId,
            onDismiss = { isReportDialogVisible = false },
            onResult = { messageRes ->
                coroutineScope.launch {
                    showSnackbar(
                        snackbarState = snackbarState,
                        message = context.getString(messageRes),
                    )
                }
            },
        )
    }
}

@Composable
private fun TemplateDetailScreen(
    uiState: TemplateDetailUiState,
    isMenuVisible: Boolean,
    isMenuExpanded: Boolean,
    onMenuExpandedChange: (Boolean) -> Unit,
    onBackClick: () -> Unit,
    onTakeTemplateClick: () -> Unit,
    onReportClick: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (uiState.isLoading) BottariCircularLoader()

        Column(modifier = Modifier.fillMaxSize()) {
            TemplateDetailTopBar(
                title = uiState.title,
                isMenuVisible = isMenuVisible,
                isMenuExpanded = isMenuExpanded,
                onMenuExpandedChange = onMenuExpandedChange,
                onBackClick = onBackClick,
                onReportClick = onReportClick,
            )

            LazyColumn(
                contentPadding = PaddingValues(vertical = BottariTheme.spacing.spaceMedium),
                modifier =
                    Modifier
                        .weight(1f)
                        .topBottomFadingEdge(BottariTheme.colors.white),
            ) {
                items(uiState.items, key = { item -> item.id }) { item ->
                    TemplateDetailItem(item = item)
                }
            }

            BottariButton(
                text = stringResource(PresentationR.string.template_detail_template_btn_text),
                onClick = onTakeTemplateClick,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            start = BottariTheme.spacing.spaceXLarge,
                            end = BottariTheme.spacing.spaceXLarge,
                            bottom = BottariTheme.spacing.spaceLarge,
                        ),
            )
        }
    }
}

@Composable
private fun TemplateDetailTopBar(
    title: String,
    isMenuVisible: Boolean,
    isMenuExpanded: Boolean,
    onMenuExpandedChange: (Boolean) -> Unit,
    onBackClick: () -> Unit,
    onReportClick: () -> Unit,
) {
    BottariTopAppBar(
        title = title,
        navigationIcon = {
            BottariIconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(PresentationR.drawable.ic_previous),
                    contentDescription = stringResource(UIR.string.common_previous_btn_description),
                )
            }
        },
        actions = {
            if (isMenuVisible) {
                BottariIconButton(onClick = { onMenuExpandedChange(true) }) {
                    Icon(
                        painter = painterResource(PresentationR.drawable.ic_more),
                        contentDescription = stringResource(UIR.string.common_option_btn_description),
                    )
                }
                DropdownMenu(
                    expanded = isMenuExpanded,
                    onDismissRequest = { onMenuExpandedChange(false) },
                    containerColor = BottariTheme.colors.white,
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(text = stringResource(PresentationR.string.menu_template_report_title_text))
                        },
                        onClick = {
                            onReportClick()
                            onMenuExpandedChange(false)
                        },
                    )
                }
            }
        },
    )
}

@Composable
private fun TemplateDetailItem(
    item: BottariTemplateItemUiModel,
    modifier: Modifier = Modifier,
) {
    BottariCard(
        contentPadding = PaddingValues(BottariTheme.spacing.spaceSmall),
        modifier =
            modifier
                .fillMaxWidth()
                .padding(
                    start = BottariTheme.spacing.spaceLarge,
                    end = BottariTheme.spacing.spaceLarge,
                    top = BottariTheme.spacing.space2xSmall,
                    bottom = BottariTheme.spacing.spaceXSmall,
                ),
    ) {
        Text(
            text = item.name,
            style = BottariTheme.typography.medium16.toTextStyle(),
            color = BottariTheme.colors.black,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TemplateDetailScreenPreview() {
    BottariTheme {
        TemplateDetailScreen(
            uiState =
                TemplateDetailUiState(
                    title = "XXX 보따리",
                    items = List(20) { BottariTemplateItemUiModel(it.toLong(), "아이템 $it") },
                    author = "다이스",
                ),
            isMenuVisible = true,
            isMenuExpanded = false,
            onMenuExpandedChange = {},
            onBackClick = {},
            onTakeTemplateClick = {},
            onReportClick = {},
        )
    }
}
