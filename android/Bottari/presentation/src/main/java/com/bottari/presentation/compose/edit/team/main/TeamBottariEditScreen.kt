package com.bottari.presentation.compose.edit.team.main

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bottari.presentation.compose.common.component.BottariTabBar
import com.bottari.presentation.compose.common.theme.BottariTheme
import com.bottari.presentation.compose.common.theme.LocalBottariBgColor
import com.bottari.presentation.compose.edit.team.assigned.TeamAssignedScreen
import com.bottari.presentation.compose.edit.team.component.TeamEditTopbar
import com.bottari.presentation.compose.edit.team.member.MemberEditScreen
import com.bottari.presentation.compose.edit.team.personal.TeamPersonalScreen
import com.bottari.presentation.compose.edit.team.shared.TeamSharedScreen

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TeamBottariEditScreen(viewModel: TeamBottariEditViewModel = viewModel()) {
    val snackbarHostState = remember { SnackbarHostState() }
    val backPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    var isMemberScreen by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    val pageTitles =
        listOf(
            "공통",
            "담당",
            "개인",
        )
    val pagerState = rememberPagerState(initialPage = 0) { pageTitles.size }

    BackHandler(enabled = isMemberScreen) {
        isMemberScreen = false
    }

    LaunchedEffect(pagerState.currentPage) {
        focusManager.clearFocus()
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                TeamBottariEditUiEvent.FetchTeamBottariDetailFailure ->
                    snackbarHostState.showSnackbar("보따리 불러오기에 실패했습니다")

                TeamBottariEditUiEvent.ToggleAlarmStateFailure ->
                    snackbarHostState.showSnackbar("알람 불러오기에 실패했습니다")
            }
        }
    }

    Scaffold(
        topBar = {
            TeamEditTopbar(
                title = uiState.bottariTitle,
                isMemberScreen = isMemberScreen,
                onBackClick = {
                    if (isMemberScreen) {
                        isMemberScreen = false
                        return@TeamEditTopbar
                    }
                    backPressedDispatcher?.onBackPressed()
                },
                onMemberClick = { isMemberScreen = true },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = LocalBottariBgColor.current,
    ) { paddingValues ->
        if (isMemberScreen) {
            MemberEditScreen(
                bottariTitle = uiState.bottariTitle,
                snackbarHostState = snackbarHostState,
                modifier =
                    Modifier
                        .padding(paddingValues)
                        .padding(horizontal = BottariTheme.spacing.spaceMedium)
                        .fillMaxSize(),
            )
            return@Scaffold
        }
        Column(
            modifier =
                Modifier
                    .padding(top = paddingValues.calculateTopPadding())
                    .fillMaxSize(),
        ) {
            BottariTabBar(
                pageTitles = pageTitles,
                pagerState = pagerState,
            ) { page ->
                when (page) {
                    0 ->
                        TeamSharedScreen(
                            snackbarHostState = snackbarHostState,
                            modifier =
                                Modifier
                                    .fillMaxSize()
                                    .padding(
                                        start = paddingValues.calculateStartPadding(LocalLayoutDirection.current),
                                        end = paddingValues.calculateEndPadding(LocalLayoutDirection.current),
                                        bottom = BottariTheme.spacing.spaceMedium,
                                    ).imePadding()
                                    .then(if (WindowInsets.isImeVisible) Modifier else Modifier.navigationBarsPadding()),
                        )

                    1 ->
                        TeamAssignedScreen(
                            snackbarHostState = snackbarHostState,
                            modifier =
                                Modifier
                                    .fillMaxSize()
                                    .padding(
                                        start = paddingValues.calculateStartPadding(LocalLayoutDirection.current),
                                        end = paddingValues.calculateEndPadding(LocalLayoutDirection.current),
                                    ).padding(horizontal = BottariTheme.spacing.spaceMedium)
                                    .padding(top = BottariTheme.spacing.spaceSmall)
                                    .then(Modifier.navigationBarsPadding()),
                        )

                    2 ->
                        TeamPersonalScreen(
                            snackbarHostState = snackbarHostState,
                            modifier =
                                Modifier
                                    .fillMaxSize()
                                    .padding(
                                        start = paddingValues.calculateStartPadding(LocalLayoutDirection.current),
                                        end = paddingValues.calculateEndPadding(LocalLayoutDirection.current),
                                        bottom = BottariTheme.spacing.spaceMedium,
                                    ).imePadding()
                                    .then(if (WindowInsets.isImeVisible) Modifier else Modifier.navigationBarsPadding()),
                        )
                }
            }
        }
    }
}
