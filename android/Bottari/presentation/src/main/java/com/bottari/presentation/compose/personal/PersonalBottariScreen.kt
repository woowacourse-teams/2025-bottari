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
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
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
    viewModel: PersonalChecklistViewModel =
        viewModel(),
) {
    val backPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val uiEvent = viewModel.uiEvent.collectAsStateWithLifecycle(null)

    val snackbarHostState = remember { SnackbarHostState() }

    var isSwipeScreen by remember { mutableStateOf(notificationFlag) }

    BackHandler(enabled = isSwipeScreen) {
        isSwipeScreen = false
    }

    LaunchedEffect(uiEvent.value) {
        when (val event = uiEvent.value ?: return@LaunchedEffect) {
            PersonalChecklistUiEvent.FetchChecklistFailure -> {}
            PersonalChecklistUiEvent.ResetCheckStateFailure -> {}
        }
    }

    Scaffold(
        topBar = {
            ChecklistTopBar(
                title = bottariTitle,
                onBackClick = {
                    if (isSwipeScreen) {
                        isSwipeScreen = false
                    } else {
                        backPressedDispatcher?.onBackPressed()
                    }
                },
                onSwipeClick = { isSwipeScreen = true },
                onResetClick = viewModel::resetItemsCheckState,
                isResetIconVisible = (!isSwipeScreen && uiState.value.isAnyChecked),
                isSwipeIconVisible = (!isSwipeScreen && !uiState.value.isCompleted),
                modifier = Modifier.padding(horizontal = BottariTheme.spacing.spaceMedium),
            )
        },
        containerColor = LocalBottariBgColor.current,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->
        if (uiState.value.isLoading) {
            IndeterminateCircularIndicator()
            return@Scaffold
        }
        if (!isSwipeScreen) {
            PersonalChecklistScreen(
                isToolTipClosed = uiState.value.isTooltipClosed,
                onCloseToolTip = viewModel::closeTooltip,
                checklistItems = uiState.value.bottariItems,
                onClickItem = viewModel::toggleItemChecked,
                totalQuantity = uiState.value.totalQuantity,
                checkedQuantity = uiState.value.checkedQuantity,
                modifier = Modifier.padding(innerPadding),
            )
            return@Scaffold
        }
        SwipeScreen(
            items = uiState.value.nonCheckedItems,
            checkedQuantity = uiState.value.checkedQuantity,
            totalQuantity = uiState.value.totalQuantity,
            isComplete = uiState.value.isCompleted,
            onLeftSwipe = {},
            onRightSwipe = viewModel::toggleItemChecked,
            onClickCompleteButton = {
                isSwipeScreen = false
            },
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
private fun PersonalBottariScreen(
    bottariTitle: String,
    uiState: PersonalChecklistUiState,
) {
    val isSwipeScreen = true
    Scaffold(
        topBar = {
            ChecklistTopBar(
                title = bottariTitle,
                onBackClick = {},
                onSwipeClick = {},
                onResetClick = {},
                isSwipeIconVisible = true,
                isResetIconVisible = true,
            )
        },
        containerColor = LocalBottariBgColor.current,
    ) { innerPadding ->
        if (!isSwipeScreen) {
            PersonalChecklistScreen(
                modifier = Modifier.padding(innerPadding),
                checklistItems = uiState.bottariItems,
                onClickItem = {},
                totalQuantity = uiState.totalQuantity,
                checkedQuantity = uiState.checkedQuantity,
                isToolTipClosed = uiState.isTooltipClosed,
                onCloseToolTip = {},
            )
            return@Scaffold
        }
        SwipeScreen(
            uiState.nonCheckedItems,
            3,
            7,
            false,
            {},
            {},
            {},
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Preview
@Composable
private fun PersonalBottariScreenPreview() {
    BottariTheme {
        PersonalBottariScreen(
            "테스트",
            PersonalChecklistUiState(
                bottariItems =
                    listOf(
                        ChecklistItemUiModel(1, "테스트", false),
                        ChecklistItemUiModel(2, "테스트", true),
                        ChecklistItemUiModel(3, "테스트", true),
                    ),
            ),
        )
    }
}
