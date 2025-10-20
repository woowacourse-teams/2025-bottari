package com.bottari.presentation.compose.personal

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.component.IndeterminateCircularIndicator
import com.bottari.presentation.compose.common.theme.BottariTheme
import com.bottari.presentation.compose.common.theme.LocalBottariBgColor
import com.bottari.presentation.compose.personal.checklist.PersonalChecklistScreen
import com.bottari.presentation.compose.personal.swipe.SwipeScreen
import com.bottari.presentation.model.bottari.ChecklistItemUiModel

@Composable
fun PersonalBottariScreen(
    bottariTitle: String,
    notificationFlag: Boolean,
    viewModel: PersonalChecklistViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val uiEvent by viewModel.uiEvent.collectAsStateWithLifecycle(null)

    val backPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val snackbarHostState = remember { SnackbarHostState() }
    var isSwipeScreen by remember { mutableStateOf(notificationFlag) }

    val checklistFetchFailureText = stringResource(R.string.checklist_fetch_failure_text)
    val checklistResetCheckStateFailureText = stringResource(R.string.checklist_reset_failure_text)
    LaunchedEffect(uiEvent) {
        when (uiEvent ?: return@LaunchedEffect) {
            PersonalChecklistUiEvent.FetchChecklistFailure -> {
                snackbarHostState.showSnackbar(message = checklistFetchFailureText)
            }

            PersonalChecklistUiEvent.ResetCheckStateFailure -> {
                snackbarHostState.showSnackbar(message = checklistResetCheckStateFailureText)
            }
        }
    }

    PersonalBottariScreen(
        uiState = uiState,
        bottariTitle = bottariTitle,
        isSwipeScreen = isSwipeScreen,
        snackbarHostState = snackbarHostState,
        onBackClick = onBackClick@{
            if (isSwipeScreen) {
                isSwipeScreen = false
                return@onBackClick
            }
            backPressedDispatcher?.onBackPressed()
        },
        onSwipeClick = { isSwipeScreen = true },
        onResetClick = viewModel::resetItemsCheckState,
        onCloseToolTip = viewModel::closeTooltip,
        onClickItem = viewModel::toggleItemChecked,
        onSwipeRight = viewModel::toggleItemChecked,
        onClickCompleteButton = { isSwipeScreen = false },
    )
}

@Composable
private fun PersonalBottariScreen(
    uiState: PersonalChecklistUiState,
    bottariTitle: String,
    isSwipeScreen: Boolean,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onSwipeClick: () -> Unit,
    onResetClick: () -> Unit,
    onCloseToolTip: () -> Unit,
    onClickItem: (Long) -> Unit,
    onSwipeRight: (Long) -> Unit,
    onClickCompleteButton: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BackHandler(enabled = isSwipeScreen, onBack = onBackClick)

    Scaffold(
        modifier = modifier,
        topBar = {
            ChecklistTopBar(
                title = bottariTitle,
                onBackClick = onBackClick,
                onSwipeClick = onSwipeClick,
                onResetClick = onResetClick,
                isResetIconVisible = (!isSwipeScreen && uiState.isAnyChecked),
                isSwipeIconVisible = (!isSwipeScreen && !uiState.isCompleted),
                modifier = Modifier.padding(horizontal = BottariTheme.spacing.spaceMedium),
            )
        },
        containerColor = LocalBottariBgColor.current,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->
        if (uiState.isLoading) {
            IndeterminateCircularIndicator()
            return@Scaffold
        }
        if (!isSwipeScreen) {
            PersonalChecklistScreen(
                isToolTipClosed = uiState.isTooltipClosed,
                onCloseToolTip = onCloseToolTip,
                checklistItems = uiState.bottariItems,
                onClickItem = onClickItem,
                totalQuantity = uiState.totalQuantity,
                checkedQuantity = uiState.checkedQuantity,
                modifier = Modifier.padding(innerPadding),
            )
        } else {
            SwipeScreen(
                items = uiState.nonCheckedItems,
                checkedQuantity = uiState.checkedQuantity,
                totalQuantity = uiState.totalQuantity,
                isComplete = uiState.isCompleted,
                onLeftSwipe = {},
                onRightSwipe = onSwipeRight,
                onClickCompleteButton = onClickCompleteButton,
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PersonalBottariScreenPreview() {
    BottariTheme {
        val previewItems =
            listOf(
                ChecklistItemUiModel(1, "양말", false),
                ChecklistItemUiModel(2, "충전기", true),
                ChecklistItemUiModel(3, "여권", true),
                ChecklistItemUiModel(4, "세면도구", false),
            )
        PersonalBottariScreen(
            uiState =
                PersonalChecklistUiState(
                    bottariItems = previewItems,
                    initialItems = previewItems,
                    isLoading = false,
                    isTooltipClosed = true,
                ),
            bottariTitle = "미리보기 타이틀",
            isSwipeScreen = false,
            snackbarHostState = remember { SnackbarHostState() },
            onBackClick = {},
            onSwipeClick = {},
            onResetClick = {},
            onCloseToolTip = {},
            onClickItem = {},
            onSwipeRight = {},
            onClickCompleteButton = {},
        )
    }
}
