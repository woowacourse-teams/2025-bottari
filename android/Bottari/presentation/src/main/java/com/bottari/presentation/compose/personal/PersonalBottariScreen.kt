package com.bottari.presentation.compose.personal

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bottari.presentation.compose.common.theme.BottariTheme
import com.bottari.presentation.compose.common.theme.LocalBottariBgColor
import com.bottari.presentation.model.bottari.ChecklistItemUiModel

@Composable
fun PersonalBottariScreen(
    bottariId: Long,
    bottariTitle: String,
    viewModel: PersonalChecklistViewModel =
        viewModel(
            factory = PersonalChecklistViewModel.Factory(bottariId),
        ),
) {
    val backPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val uiEvent = viewModel.uiEvent.collectAsStateWithLifecycle(null)

    val context = LocalContext.current

    var isSwipeScreen by remember { mutableStateOf(false) }

    BackHandler(enabled = isSwipeScreen) {
        isSwipeScreen = false
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
            )
        },
        containerColor = LocalBottariBgColor.current,
    ) { innerPadding ->
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
        SwipeScreen()
    }
}

@Composable
private fun PersonalBottariScreen(
    bottariTitle: String,
    uiState: PersonalChecklistUiState,
) {
    val isSwipeScreen = false
    Scaffold(
        topBar = {
            ChecklistTopBar(
                title = bottariTitle,
                onBackClick = {},
                onSwipeClick = {},
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
        SwipeScreen()
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
