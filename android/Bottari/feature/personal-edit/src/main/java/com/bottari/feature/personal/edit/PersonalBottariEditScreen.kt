package com.bottari.feature.personal.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.core.ui.R
import com.bottari.core.ui.component.BottariTabBar
import com.bottari.core.ui.provider.LocalSnackbarHostState
import com.bottari.feature.personal.edit.alarm.AlarmEditScreen
import com.bottari.feature.personal.edit.component.PersonalBottariEditTopAppBar
import com.bottari.feature.personal.edit.item.PersonalItemEditScreen
import com.bottari.feature.personal.edit.rename.BottariRenameDialog

@Composable
fun PersonalBottariEditScreen(
    bottariId: Long,
    onBackClick: () -> Unit,
    viewModel: PersonalBottariEditViewModel =
        hiltViewModel<PersonalBottariEditViewModel, PersonalBottariEditViewModel.Factory> {
            it.create(bottariId)
        },
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = LocalSnackbarHostState.current
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
            bottariId = uiState.value.bottariId,
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
) {
    Column(modifier = Modifier.fillMaxSize()) {
        PersonalBottariEditTopAppBar(
            bottariTitle = bottariTitle,
            onBackClick = onBackClick,
            onBottariRenameClick = onBottariRenameClick,
            onCreateTemplateClick = onCreateTemplateClick,
        )
        PersonalBottariEditPager(
            bottariId = bottariId,
            bottariTitle = bottariTitle,
            snackbarHostState = snackbarHostState,
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(bottom = BottariTheme.spacing.spaceMedium)
                    .imePadding(),
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
                    PersonalItemEditScreen(
                        bottariId = bottariId,
                        snackbarHostState = snackbarHostState,
                    )
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
