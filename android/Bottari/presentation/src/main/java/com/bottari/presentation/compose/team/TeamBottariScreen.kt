package com.bottari.presentation.compose.team

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.component.BottariTabBar
import com.bottari.presentation.compose.common.component.IndeterminateCircularIndicator
import com.bottari.presentation.compose.common.theme.BottariTheme
import com.bottari.presentation.compose.common.theme.LocalBottariBgColor
import com.bottari.presentation.compose.personal.ChecklistTopBar
import com.bottari.presentation.compose.personal.swipe.SwipeScreen
import com.bottari.presentation.compose.team.checklist.ComposeTeamChecklistUiEvent
import com.bottari.presentation.compose.team.checklist.ComposeTeamChecklistViewModel
import com.bottari.presentation.compose.team.checklist.TeamChecklistScreen
import com.bottari.presentation.compose.team.item.TeamItemStateScreen
import com.bottari.presentation.compose.team.member.TeamMemberStateScreen
import com.bottari.presentation.model.bottari.team.TeamChecklistItemUiModel

@Composable
fun TeamBottariScreen(
    bottariTitle: String,
    notificationFlag: Boolean,
    navigateBack: () -> Unit,
    viewModel: ComposeTeamChecklistViewModel = viewModel(),
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var isSwipeScreen by rememberSaveable { mutableStateOf(notificationFlag) }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
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
                ComposeTeamChecklistUiEvent.CheckItemFailure ->
                    snackbarHostState.showSnackbar("아이템 체크에 실패했습니다")

                ComposeTeamChecklistUiEvent.FetchChecklistFailure ->
                    snackbarHostState.showSnackbar("보따리를 불러오지 못했습니다")
            }
        }
    }

    Scaffold(
        topBar = {
            ChecklistTopBar(
                title = bottariTitle,
                onBackClick = { if (isSwipeScreen) isSwipeScreen = false else navigateBack() },
                onSwipeClick = { isSwipeScreen = !isSwipeScreen },
                onResetClick = {},
                isResetIconVisible = false,
                isSwipeIconVisible = !uiState.isAllChecked && !isSwipeScreen && (pagerState.currentPage == 0),
            )
        },
        containerColor = LocalBottariBgColor.current,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->
        when {
            uiState.isInitialLoading -> IndeterminateCircularIndicator()

            isSwipeScreen -> {
                SwipeScreen(
                    items = uiState.nonCheckedItems,
                    checkedQuantity = uiState.checkedQuantity,
                    totalQuantity = uiState.totalQuantity,
                    isComplete = uiState.isAllChecked,
                    onLeftSwipe = {},
                    onRightSwipe = { item ->
                        val teamItem = item as TeamChecklistItemUiModel
                        viewModel.toggleItemChecked(teamItem.id, teamItem.type)
                    },
                    onClickCompleteButton = { isSwipeScreen = false },
                    modifier =
                        Modifier
                            .padding(innerPadding)
                            .padding(BottariTheme.spacing.spaceMedium),
                )
            }

            else -> {
                Column(modifier = Modifier.padding(innerPadding)) {
                    BottariTabBar(
                        pageTitles = pageTitles,
                        pagerState = pagerState,
                    ) { page ->
                        when (page) {
                            0 ->
                                TeamChecklistScreen(
                                    uiState = uiState,
                                    onClickSection = viewModel::toggleTypeExpanded,
                                    onCloseToolTip = viewModel::closeTooltip,
                                    isToolTipClosed = uiState.isTooltipClosed,
                                    onClickItem = viewModel::toggleItemChecked,
                                    modifier =
                                        Modifier
                                            .fillMaxSize()
                                            .padding(BottariTheme.spacing.spaceMedium),
                                )

                            1 ->
                                TeamItemStateScreen(
                                    snackbarHostState = snackbarHostState,
                                    checkedState = uiState.bottariItems,
                                )

                            2 ->
                                TeamMemberStateScreen(
                                    snackbarHostState = snackbarHostState,
                                    checkedState = uiState.bottariItems,
                                )
                        }
                    }
                }
            }
        }
    }
}
