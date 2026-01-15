package com.bottari.feature.personal.checklist

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bottari.bottari.designsystem.component.BottariCircularLoader
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.core.ui.R
import com.bottari.core.ui.extension.showSnackbar
import com.bottari.core.ui.model.bottari.PersonalChecklistItemUiModel
import com.bottari.core.ui.model.bottari.team.ChecklistItemUiModel
import com.bottari.feature.personal.checklist.checklist.PersonalChecklistScreen
import com.bottari.feature.personal.checklist.component.ChecklistTopBar
import com.bottari.feature.personal.checklist.component.PersonalChecklistEmptyView
import com.bottari.feature.personal.checklist.swipe.SwipeScreen

@Composable
fun PersonalBottariScreen(
    snackbarState: SnackbarHostState,
    bottariId: Long,
    bottariTitle: String,
    notificationFlag: Boolean,
    navigateToBack: () -> Unit,
    navigateToEdit: () -> Unit,
    viewModel: PersonalChecklistViewModel =
        hiltViewModel<PersonalChecklistViewModel, PersonalChecklistViewModel.Factory> {
            it.create(bottariId)
        },
) {
    val context = LocalContext.current
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isSwipeScreen by remember { mutableStateOf(notificationFlag) }

    BackHandler(enabled = isSwipeScreen) {
        if (isSwipeScreen) {
            isSwipeScreen = false
            return@BackHandler
        }
        navigateToBack()
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                PersonalChecklistUiEvent.FetchChecklistFailure -> {
                    showSnackbar(
                        snackbarState = snackbarState,
                        message = context.getString(R.string.checklist_fetch_failure_text),
                    )
                }

                PersonalChecklistUiEvent.ResetCheckStateFailure -> {
                    showSnackbar(
                        snackbarState = snackbarState,
                        message = context.getString(R.string.checklist_reset_failure_text),
                    )
                }
            }
        }
    }

    PersonalBottariScreen(
        uiState = uiState,
        bottariTitle = bottariTitle,
        isSwipeScreen = isSwipeScreen,
        onBackClick = { backDispatcher?.onBackPressed() },
        onSwipeClick = { isSwipeScreen = true },
        onResetClick = viewModel::resetItemsCheckState,
        onCloseToolTip = viewModel::closeTooltip,
        onClickItem = viewModel::toggleItemChecked,
        onSwipeRight = { item -> viewModel.toggleItemChecked(item.id) },
        onClickCompleteButton = { isSwipeScreen = false },
        navigateToEdit = navigateToEdit,
    )
}

@Composable
private fun PersonalBottariScreen(
    uiState: PersonalChecklistUiState,
    bottariTitle: String,
    isSwipeScreen: Boolean,
    onBackClick: () -> Unit,
    onSwipeClick: () -> Unit,
    onResetClick: () -> Unit,
    onCloseToolTip: () -> Unit,
    onClickItem: (Long) -> Unit,
    onSwipeRight: (ChecklistItemUiModel) -> Unit,
    onClickCompleteButton: () -> Unit,
    navigateToEdit: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        ChecklistTopBar(
            title = bottariTitle,
            onBackClick = onBackClick,
            onSwipeClick = onSwipeClick,
            onResetClick = onResetClick,
            isResetIconVisible = (!isSwipeScreen && uiState.isAnyChecked),
            isSwipeIconVisible = (!isSwipeScreen && !uiState.isCompleted),
        )

        if (uiState.isItemsEmpty) {
            PersonalChecklistEmptyView(
                onClickEdit = navigateToEdit,
                modifier = Modifier.fillMaxSize(),
            )
        }

        if (!isSwipeScreen) {
            PersonalChecklistScreen(
                isToolTipClosed = uiState.isTooltipClosed,
                onCloseToolTip = onCloseToolTip,
                checklistItems = uiState.bottariItems,
                onClickItem = onClickItem,
                totalQuantity = uiState.totalQuantity,
                checkedQuantity = uiState.checkedQuantity,
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
                modifier = Modifier.padding(horizontal = BottariTheme.spacing.spaceMedium),
            )
        }
    }

    if (uiState.isLoading) BottariCircularLoader()
}

@Preview(showBackground = true)
@Composable
private fun PersonalBottariScreenPreview() {
    BottariTheme {
        val previewItems =
            listOf(
                PersonalChecklistItemUiModel(1, "양말", false),
                PersonalChecklistItemUiModel(2, "충전기", true),
                PersonalChecklistItemUiModel(3, "여권", true),
                PersonalChecklistItemUiModel(4, "세면도구", false),
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
            onBackClick = {},
            onSwipeClick = {},
            onResetClick = {},
            onCloseToolTip = {},
            onClickItem = {},
            onSwipeRight = {},
            onClickCompleteButton = {},
            navigateToEdit = {},
        )
    }
}
