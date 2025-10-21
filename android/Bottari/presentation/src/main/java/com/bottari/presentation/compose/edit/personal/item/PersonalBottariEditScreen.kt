package com.bottari.presentation.compose.edit.personal.item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.component.BottariTabBar
import com.bottari.presentation.compose.common.theme.BottariTheme
import com.bottari.presentation.compose.common.theme.LocalBottariBgColor
import com.bottari.presentation.compose.edit.personal.component.ComposeBottariRenameDialog
import com.bottari.presentation.compose.edit.personal.component.ItemEditContent
import com.bottari.presentation.compose.edit.personal.component.PersonalBottariEditTopAppBar
import com.bottari.presentation.view.edit.personal.main.PersonalBottariEditUiEvent
import com.bottari.presentation.view.edit.personal.main.PersonalBottariEditViewModel

@Composable
fun PersonalBottariEditScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PersonalBottariEditViewModel = viewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val uiEvent = viewModel.uiEvent.collectAsStateWithLifecycle(null)
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiEvent.value) {
        when (uiEvent.value ?: return@LaunchedEffect) {
            PersonalBottariEditUiEvent.CreateTemplateFailure ->
                snackbarHostState.showSnackbar(
                    context.getString(R.string.bottari_edit_create_template_failure_text),
                )

            PersonalBottariEditUiEvent.CreateTemplateSuccess ->
                snackbarHostState.showSnackbar(
                    context.getString(R.string.bottari_edit_create_template_success_text),
                )

            PersonalBottariEditUiEvent.FindBottariFailure ->
                snackbarHostState.showSnackbar(
                    context.getString(
                        R.string.bottari_edit_fetch_failure_text,
                    ),
                )

            PersonalBottariEditUiEvent.ToggleAlarmStateFailure ->
                snackbarHostState.showSnackbar(
                    context.getString(R.string.bottari_edit_toggle_alarm_state_failure_text),
                )
        }
    }

    if (uiState.value.showBottariRenameDialog) {
        ComposeBottariRenameDialog(
            bottariTitle = uiState.value.bottariTitle,
            onDismissRequest = { viewModel.changeBottariRenameDialogState(false) },
            snackbarHostState = snackbarHostState,
        )
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            PersonalBottariEditTopAppBar(
                bottariTitle = uiState.value.bottariTitle,
                onBackClick = onBackClick,
                onBottariRenameClick = { viewModel.changeBottariRenameDialogState(true) },
                onCreateTemplateClick = viewModel::createBottariTemplate,
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = LocalBottariBgColor.current,
    ) { paddingValues ->
        PersonalBottariEditPager(
            snackbarHostState = snackbarHostState,
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
        )
    }
}

@Composable
private fun PersonalBottariEditPager(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    val pageTitles =
        listOf(
            stringResource(R.string.bottari_edit_item_text),
            stringResource(R.string.bottari_edit_alarm_text),
        )
    val pagerState = rememberPagerState { pageTitles.size }

    Column(modifier = modifier) {
        BottariTabBar(
            pageTitles = pageTitles,
            pagerState = pagerState,
        ) { page ->
            when (page) {
                0 -> ItemEditContent(snackbarHostState = snackbarHostState)
                1 -> AlarmEditContent()
            }
        }
    }
}

@Composable
fun AlarmEditContent(modifier: Modifier = Modifier) {
}

@Preview
@Composable
private fun PersonalBottariEditScreenPreview() {
    BottariTheme {
        PersonalBottariEditScreen(onBackClick = {})
    }
}
