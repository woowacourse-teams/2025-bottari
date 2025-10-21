package com.bottari.presentation.compose.team

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.component.BottariTabBar
import com.bottari.presentation.compose.common.theme.LocalBottariBgColor
import com.bottari.presentation.compose.personal.ChecklistTopBar
import com.bottari.presentation.compose.team.checklist.TeamBottariChecklistScreen
import com.bottari.presentation.compose.team.item.TeamItemStateScreen
import com.bottari.presentation.compose.team.member.TeamMemberStateScreen

@Composable
fun TeamBottariScreen(
    bottariTitle: String,
    notificationFlag: Boolean,
) {
    val backPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val snackbarHostState = remember { SnackbarHostState() }
    var isSwipeScreen by remember { mutableStateOf(notificationFlag) }
    Scaffold(
        topBar = {
            ChecklistTopBar(
                title = bottariTitle,
                onBackClick = {},
                onSwipeClick = {},
                onResetClick = {},
                isResetIconVisible = true,
                isSwipeIconVisible = true,
            )
        },
        containerColor = LocalBottariBgColor.current,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->

        val pageTitles =
            listOf(
                stringResource(R.string.team_checklist_tap_checklist_text),
                stringResource(R.string.team_checklist_tap_team_current_text),
                stringResource(R.string.team_checklist_tap_member_checklist_text),
            )

        BottariTabBar(
            pageTitles = pageTitles,
            pagerState = rememberPagerState(initialPage = 0) { pageTitles.size },
        ) { page ->
            when (page) {
                0 ->
                    TeamBottariChecklistScreen(
                        snackbarHostState = snackbarHostState,
                        modifier = Modifier.padding(innerPadding),
                    )

                1 ->
                    TeamItemStateScreen(
                        snackbarHostState = snackbarHostState,
                        modifier = Modifier.padding(innerPadding),
                    )

                2 -> {
                    TeamMemberStateScreen(
                        snackbarHostState = snackbarHostState,
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}
