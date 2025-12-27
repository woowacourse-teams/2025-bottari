package com.bottari.presentation.compose.edit.personal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.bottari.designsystem.theme.LocalBottariBgColor
import com.bottari.core.ui.component.BottariTabBar
import com.bottari.presentation.R
import com.bottari.presentation.compose.edit.personal.alarm.AlarmEditScreen
import com.bottari.presentation.compose.edit.personal.component.PersonalBottariEditTopAppBar
import com.bottari.presentation.compose.edit.personal.item.PersonalItemEditScreen
import com.bottari.presentation.compose.edit.personal.rename.BottariRenameDialog

@Composable
fun PersonalBottariEditScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PersonalBottariEditViewModel = viewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    var showDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                PersonalBottariEditUiEvent.CreateTemplateFailure -> {
                    snackbarHostState.showSnackbar(
                        context.getString(R.string.bottari_edit_create_template_failure_text),
                    )
                }

                PersonalBottariEditUiEvent.CreateTemplateSuccess -> {
                    snackbarHostState.showSnackbar(
                        context.getString(R.string.bottari_edit_create_template_success_text),
                    )
                }

                PersonalBottariEditUiEvent.FindBottariFailure -> {
                    snackbarHostState.showSnackbar(
                        context.getString(
                            R.string.bottari_edit_fetch_failure_text,
                        ),
                    )
                }

                PersonalBottariEditUiEvent.ToggleAlarmStateFailure -> {
                    snackbarHostState.showSnackbar(
                        context.getString(R.string.bottari_edit_toggle_alarm_state_failure_text),
                    )
                }
            }
        }
    }

    if (showDialog) {
        BottariRenameDialog(
            bottariTitle = uiState.value.bottariTitle,
            onDismissRequest = { showDialog = false },
            snackbarHostState = snackbarHostState,
        )
    }

    PersonalBottariEditScreen(
        bottariId = uiState.value.bottariId,
        bottariTitle = uiState.value.bottariTitle,
        snackbarHostState = snackbarHostState,
        onBackClick = onBackClick,
        onCreateTemplateClick = viewModel::createBottariTemplate,
        onBottariRenameClick = { showDialog = true },
        modifier = modifier,
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PersonalBottariEditScreen(
    bottariId: Long,
    bottariTitle: String,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onCreateTemplateClick: () -> Unit,
    onBottariRenameClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            PersonalBottariEditTopAppBar(
                bottariTitle = bottariTitle,
                onBackClick = onBackClick,
                onBottariRenameClick = onBottariRenameClick,
                onCreateTemplateClick = onCreateTemplateClick,
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = LocalBottariBgColor.current,
    ) { paddingValues ->
        PersonalBottariEditPager(
            bottariId = bottariId,
            bottariTitle = bottariTitle,
            snackbarHostState = snackbarHostState,
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(
                        start = paddingValues.calculateStartPadding(LocalLayoutDirection.current),
                        end = paddingValues.calculateStartPadding(LocalLayoutDirection.current),
                        top = paddingValues.calculateTopPadding(),
                        bottom = BottariTheme.spacing.spaceMedium,
                    ).imePadding()
                    .then(if (WindowInsets.isImeVisible) Modifier else Modifier.navigationBarsPadding()),
        )
    }
}

@Composable
private fun PersonalBottariEditPager(
    bottariId: Long,
    bottariTitle: String,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    val pageTitles =
        listOf(
            stringResource(R.string.bottari_edit_item_text),
            stringResource(R.string.bottari_edit_alarm_text),
        )
    val pagerState = rememberPagerState { pageTitles.size }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(pagerState.isScrollInProgress) {
        if (pagerState.isScrollInProgress) {
            keyboardController?.hide()
            focusManager.clearFocus()
        }
    }

    Column(modifier = modifier) {
        BottariTabBar(
            pageTitles = pageTitles,
            pagerState = pagerState,
        ) { page ->
            when (page) {
                0 -> {
                    PersonalItemEditScreen(snackbarHostState = snackbarHostState)
                }

                1 -> {
                    AlarmEditScreen(
                        bottariId = bottariId,
                        bottariTitle = bottariTitle,
                        snackbarHostState = snackbarHostState,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PersonalBottariEditScreenPreview() {
    BottariTheme {
        PersonalBottariEditScreen(
            bottariId = 0,
            bottariTitle = "보따리",
            snackbarHostState = remember { SnackbarHostState() },
            onBackClick = {},
            onCreateTemplateClick = {},
            onBottariRenameClick = {},
        )
    }
}
