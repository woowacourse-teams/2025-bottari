package com.bottari.feature.team.checklist

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bottari.bottari.designsystem.component.BottariCircularLoader
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.core.ui.R
import com.bottari.core.ui.component.BottariTabBar
import com.bottari.core.ui.extension.showSnackbar
import com.bottari.core.ui.model.bottari.personal.BottariItemTypeUiModel
import com.bottari.core.ui.model.bottari.team.TeamChecklistItemUiModel
import com.bottari.feature.personal.checklist.component.ChecklistTopBar
import com.bottari.feature.personal.checklist.swipe.SwipeScreen
import com.bottari.feature.team.checklist.checklist.ComposeTeamChecklistUiEvent
import com.bottari.feature.team.checklist.checklist.ComposeTeamChecklistUiState
import com.bottari.feature.team.checklist.checklist.ComposeTeamChecklistViewModel
import com.bottari.feature.team.checklist.checklist.TeamChecklistScreen
import com.bottari.feature.team.checklist.item.TeamItemStateScreen
import com.bottari.feature.team.checklist.member.TeamMemberStateScreen

@Composable
fun TeamBottariScreen(
    snackbarState: SnackbarHostState,
    bottariId: Long,
    bottariTitle: String,
    notificationFlag: Boolean,
    navigateBack: () -> Unit,
    viewModel: ComposeTeamChecklistViewModel =
        hiltViewModel<ComposeTeamChecklistViewModel, ComposeTeamChecklistViewModel.Factory> {
            it.create(bottariId)
        },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isSwipeScreen by rememberSaveable { mutableStateOf(notificationFlag) }

    val pageTitles =
        listOf(
            stringResource(R.string.team_checklist_tap_checklist_text),
            stringResource(R.string.team_checklist_tap_team_current_text),
            stringResource(R.string.team_checklist_tap_member_checklist_text),
        )
    val pagerState = rememberPagerState(initialPage = 0) { pageTitles.size }
    BackHandler(enabled = isSwipeScreen, onBack = { isSwipeScreen = false })

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                ComposeTeamChecklistUiEvent.CheckItemFailure -> {
                    showSnackbar(
                        snackbarState = snackbarState,
                        message = "물건을 챙기지 못했어요",
                    )
                }

                ComposeTeamChecklistUiEvent.FetchChecklistFailure -> {
                    showSnackbar(
                        snackbarState = snackbarState,
                        message = "보따리를 불러오지 못했어요",
                    )
                }
            }
        }
    }

    TeamBottariScreen(
        snackbarState = snackbarState,
        bottariId = bottariId,
        uiState = uiState,
        pagerState = pagerState,
        pageTitles = pageTitles,
        bottariTitle = bottariTitle,
        isSwipeScreen = isSwipeScreen,
        onBackClick = navigateBack,
        onSwipeChange = { new: Boolean -> isSwipeScreen = new },
        onToggleCheckedChange = viewModel::toggleItemChecked,
        onClickSection = viewModel::toggleTypeExpanded,
        onCloseToolTip = viewModel::closeTooltip,
    )
}

@Composable
private fun TeamBottariScreen(
    snackbarState: SnackbarHostState,
    bottariId: Long,
    uiState: ComposeTeamChecklistUiState,
    pagerState: PagerState,
    pageTitles: List<String>,
    bottariTitle: String,
    isSwipeScreen: Boolean,
    onBackClick: () -> Unit,
    onSwipeChange: (Boolean) -> Unit,
    onToggleCheckedChange: (Long, BottariItemTypeUiModel) -> Unit,
    onClickSection: (BottariItemTypeUiModel) -> Unit,
    onCloseToolTip: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        ChecklistTopBar(
            title = bottariTitle,
            onBackClick = { if (isSwipeScreen) onSwipeChange(false) else onBackClick() },
            onSwipeClick = { onSwipeChange(!isSwipeScreen) },
            onResetClick = {},
            isResetIconVisible = false,
            isSwipeIconVisible = !uiState.isAllChecked && !isSwipeScreen && (pagerState.currentPage == 0),
        )

        when {
            uiState.isInitialLoading -> {
                BottariCircularLoader()
            }

            isSwipeScreen -> {
                SwipeScreen(
                    items = uiState.nonCheckedItems,
                    checkedQuantity = uiState.checkedQuantity,
                    totalQuantity = uiState.totalQuantity,
                    isComplete = uiState.isAllChecked,
                    onLeftSwipe = {},
                    onRightSwipe = { item ->
                        val teamItem = item as TeamChecklistItemUiModel
                        onToggleCheckedChange(teamItem.id, teamItem.type)
                    },
                    onClickCompleteButton = { onSwipeChange(false) },
                    modifier = Modifier.padding(BottariTheme.spacing.spaceMedium),
                )
            }

            else -> {
                Column {
                    BottariTabBar(
                        pageTitles = pageTitles,
                        pagerState = pagerState,
                    ) { page ->
                        when (page) {
                            0 -> {
                                TeamChecklistScreen(
                                    uiState = uiState,
                                    onClickSection = onClickSection,
                                    onCloseToolTip = onCloseToolTip,
                                    isToolTipClosed = uiState.isTooltipClosed,
                                    onClickItem = onToggleCheckedChange,
                                    modifier =
                                        Modifier
                                            .fillMaxSize()
                                            .padding(BottariTheme.spacing.spaceMedium),
                                )
                            }

                            1 -> {
                                TeamItemStateScreen(
                                    bottariId = bottariId,
                                    snackbarHostState = snackbarState,
                                    checkedState = uiState.bottariItems,
                                )
                            }

                            2 -> {
                                TeamMemberStateScreen(
                                    bottariId = bottariId,
                                    snackbarHostState = snackbarState,
                                    checkedState = uiState.bottariItems,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
