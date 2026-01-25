package com.bottari.feature.team.edit.main

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.core.ui.component.BottariTabBar
import com.bottari.core.ui.provider.LocalSnackbarHostState
import com.bottari.feature.team.edit.assigned.TeamAssignedScreen
import com.bottari.feature.team.edit.component.TeamEditTopbar
import com.bottari.feature.team.edit.member.MemberEditScreen
import com.bottari.feature.team.edit.personal.TeamPersonalScreen
import com.bottari.feature.team.edit.shared.TeamSharedScreen

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TeamBottariEditScreen(
    bottariId: Long,
    viewModel: TeamBottariEditViewModel =
        hiltViewModel<TeamBottariEditViewModel, TeamBottariEditViewModel.Factory> {
            it.create(bottariId)
        },
) {
    val snackbarHostState = LocalSnackbarHostState.current
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
                TeamBottariEditUiEvent.FetchTeamBottariDetailFailure -> {
                    snackbarHostState.showSnackbar("보따리 불러오기에 실패했습니다")
                }

                TeamBottariEditUiEvent.ToggleAlarmStateFailure -> {
                    snackbarHostState.showSnackbar("알람 불러오기에 실패했습니다")
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
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

        if (isMemberScreen) {
            MemberEditScreen(
                bottariId = bottariId,
                bottariTitle = uiState.bottariTitle,
                snackbarHostState = snackbarHostState,
                modifier =
                    Modifier
                        .padding(horizontal = BottariTheme.spacing.spaceMedium)
                        .fillMaxSize(),
            )
            return@Column
        }

        BottariTabBar(
            pageTitles = pageTitles,
            pagerState = pagerState,
        ) { page ->
            when (page) {
                0 -> {
                    TeamSharedScreen(
                        bottariId = bottariId,
                        snackbarHostState = snackbarHostState,
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .padding(bottom = BottariTheme.spacing.spaceMedium)
                                .imePadding()
                                .then(if (WindowInsets.isImeVisible) Modifier else Modifier.navigationBarsPadding()),
                    )
                }

                1 -> {
                    TeamAssignedScreen(
                        bottariId = bottariId,
                        snackbarHostState = snackbarHostState,
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .padding(horizontal = BottariTheme.spacing.spaceMedium)
                                .padding(top = BottariTheme.spacing.spaceSmall)
                                .then(Modifier.navigationBarsPadding()),
                    )
                }

                2 -> {
                    TeamPersonalScreen(
                        bottariId = bottariId,
                        snackbarHostState = snackbarHostState,
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .padding(bottom = BottariTheme.spacing.spaceMedium)
                                .imePadding()
                                .then(if (WindowInsets.isImeVisible) Modifier else Modifier.navigationBarsPadding()),
                    )
                }
            }
        }
    }
}
