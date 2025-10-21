package com.bottari.presentation.compose.team.checklist

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.component.IndeterminateCircularIndicator
import com.bottari.presentation.compose.common.theme.BottariTheme
import com.bottari.presentation.compose.personal.swipe.SwipeScreen
import com.bottari.presentation.model.bottari.personal.BottariItemTypeUiModel
import com.bottari.presentation.model.bottari.team.TeamChecklistItemUiModel
import kotlin.random.Random

@Composable
fun TeamBottariChecklistScreen(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    viewModel: ComposeTeamChecklistViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val uiEvent by viewModel.uiEvent.collectAsStateWithLifecycle(null)

    var isSwipeScreen by remember { mutableStateOf(false) }

    val checklistFetchFailureText = stringResource(R.string.checklist_fetch_failure_text)
    val checklistResetCheckStateFailureText = stringResource(R.string.checklist_reset_failure_text)

    LaunchedEffect(uiEvent) {
        when (uiEvent ?: return@LaunchedEffect) {
            ComposeTeamChecklistUiEvent.FetchChecklistFailure -> {
                snackbarHostState.showSnackbar(message = checklistFetchFailureText)
            }

            ComposeTeamChecklistUiEvent.CheckItemFailure -> {
                snackbarHostState.showSnackbar(message = checklistResetCheckStateFailureText)
            }
        }
    }

    TeamBottariChecklistScreen(
        uiState = uiState,
        onClickSection = viewModel::toggleTypeExpanded,
        onCloseToolTip = viewModel::closeTooltip,
        onToggleItem = viewModel::toggleItemChecked,
        snackbarHostState = snackbarHostState,
        isSwipeScreen = isSwipeScreen,
        onBackClick = {},
        onSwipeClick = { isSwipeScreen = true },
        modifier = modifier,
    )
}

@Composable
private fun TeamBottariChecklistScreen(
    snackbarHostState: SnackbarHostState,
    isSwipeScreen: Boolean,
    uiState: ComposeTeamChecklistUiState,
    onBackClick: () -> Unit,
    onSwipeClick: () -> Unit,
    onClickSection: (BottariItemTypeUiModel) -> Unit,
    onCloseToolTip: () -> Unit,
    onToggleItem: (Long, BottariItemTypeUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize().padding(BottariTheme.spacing.spaceMedium), verticalArrangement = Arrangement.Top) {
        if (uiState.isLoading) {
            IndeterminateCircularIndicator()
            return
        }

        if (!isSwipeScreen) {
            TeamChecklistScreen(
                uiState = uiState,
                onCloseToolTip = onCloseToolTip,
                onClickSection = { type -> onClickSection(type) },
                onClickItem = onToggleItem,
            )
        } else {
            SwipeScreen(
                items = uiState.nonCheckedItems,
                checkedQuantity = uiState.checkedQuantity,
                totalQuantity = uiState.totalQuantity,
                isComplete = uiState.isAllChecked,
                onLeftSwipe = {},
                onRightSwipe = { item ->
                    if (item is TeamChecklistItemUiModel) {
                        onToggleItem(item.id, item.type)
                    }
                },
                onClickCompleteButton = onBackClick,
            )
        }
    }
}

@Preview
@Composable
private fun TeamBottariChecklistScreenPreview() {
    val snackbarHostState = remember { SnackbarHostState() }

    TeamBottariChecklistScreen(
        snackbarHostState = snackbarHostState,
        isSwipeScreen = false,
        uiState = dummyUiState,
        onBackClick = {},
        onSwipeClick = {},
        onClickSection = {},
        onCloseToolTip = {},
        onToggleItem = { _, _ -> },
    )
}

private fun createDummyProductList(
    count: Int,
    type: BottariItemTypeUiModel,
    idStartIndex: Long = 0,
): List<TeamChecklistItemUiModel> =
    List(count) { index ->
        TeamChecklistItemUiModel(
            id = idStartIndex + index,
            name = "더미 아이템 ${idStartIndex + index + 1}",
            isChecked = Random.nextBoolean(),
            type = type,
        )
    }

val dummyUiState =
    ComposeTeamChecklistUiState(
        isLoading = false,
        bottariItems =
            createDummyProductList(
                10,
                BottariItemTypeUiModel.SHARED,
            ) + createDummyProductList(10, BottariItemTypeUiModel.PERSONAL) +
                createDummyProductList(10, BottariItemTypeUiModel.ASSIGNED()),
        sections =
            mapOf(
                BottariItemTypeUiModel.SHARED to true,
                BottariItemTypeUiModel.ASSIGNED() to true,
                BottariItemTypeUiModel.PERSONAL to true,
            ),
    )
