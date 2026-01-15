package com.bottari.feature.team.edit.member

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bottari.bottari.designsystem.component.BottariCard
import com.bottari.bottari.designsystem.component.BottariCircularLoader
import com.bottari.bottari.designsystem.component.BottariIconButton
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.common.util.DeeplinkHelper.createDeeplink
import com.bottari.core.ui.R
import com.bottari.core.ui.model.bottari.team.member.TeamMemberUiModel
import kotlinx.coroutines.flow.collectLatest
import com.bottari.core.ui.R as PresentationR

@Composable
fun MemberEditScreen(
    bottariId: Long,
    bottariTitle: String,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    viewModel: TeamManagementViewModel =
        hiltViewModel<TeamManagementViewModel, TeamManagementViewModel.Factory> {
            it.create(bottariId)
        },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(viewModel) {
        viewModel.uiEvent.collectLatest { uiEvent ->
            when (uiEvent) {
                TeamManagementUiEvent.FetchTeamMembersFailure -> {
                    snackbarHostState.showSnackbar("팀 멤버 불러오기에 실패했습니다")
                }
            }
        }
    }

    MemberEditContent(
        bottariTitle = bottariTitle,
        uiState = uiState,
        onShareClick = {
            shareInvite(
                context = context,
                inviteCode = uiState.inviteCode,
                bottariTitle = bottariTitle,
            )
        },
        modifier = modifier,
    )
}

@Composable
private fun MemberEditContent(
    bottariTitle: String,
    uiState: TeamManagementUiState,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column {
            ShareInviteItem(onShareClick = onShareClick)

            Spacer(Modifier.height(BottariTheme.spacing.spaceLarge))

            TeamBottariMemberList(
                members = uiState.members,
                teamMemberHeadCount = uiState.teamMemberHeadCount,
                maxHeadCount = uiState.maxHeadCount,
            )
        }

        if (uiState.isLoading) {
            BottariCircularLoader()
        }
    }
}

@Composable
private fun ShareInviteItem(onShareClick: () -> Unit) {
    BottariCard(
        contentPadding =
            PaddingValues(
                top = BottariTheme.spacing.space2xSmall,
                bottom = BottariTheme.spacing.space2xSmall,
                start = BottariTheme.spacing.spaceSmall,
                end = BottariTheme.spacing.space2xSmall,
            ),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.team_management_add_member_text),
                style = BottariTheme.typography.bold18.toTextStyle(),
                modifier = Modifier.weight(1f),
            )
            BottariIconButton(onClick = onShareClick) {
                Icon(
                    painter = painterResource(id = PresentationR.drawable.ic_share),
                    contentDescription = stringResource(R.string.team_btn_share_link_description),
                )
            }
        }
    }
}

@Composable
private fun TeamBottariMemberList(
    members: List<TeamMemberUiModel>,
    teamMemberHeadCount: Int,
    maxHeadCount: Int = 10,
) {
    BottariCard {
        Column {
            TeamBottariMemberListTitle(
                teamMemberHeadCount = teamMemberHeadCount,
                maxHeadCount = maxHeadCount,
            )

            Spacer(Modifier.height(BottariTheme.spacing.spaceSmall))
            HorizontalDivider(
                color = BottariTheme.colors.gray400,
                thickness = 1.dp,
            )
            Spacer(Modifier.height(BottariTheme.spacing.spaceSmall))

            TeamBottariMemberItems(members = members)
        }
    }
}

@Composable
private fun TeamBottariMemberListTitle(
    teamMemberHeadCount: Int,
    maxHeadCount: Int,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.team_management_member_title_text),
            style = BottariTheme.typography.bold18.toTextStyle(),
        )
        Spacer(Modifier.weight(1f))
        Text(
            text = "$teamMemberHeadCount/$maxHeadCount",
            style = BottariTheme.typography.bold18.toTextStyle(),
        )
    }
}

@Composable
private fun TeamBottariMemberItems(members: List<TeamMemberUiModel>) {
    LazyColumn {
        itemsIndexed(items = members) { index, member ->
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = member.nickname,
                    style = BottariTheme.typography.bold18.toTextStyle(),
                    modifier = Modifier.fillMaxWidth(),
                )

                if (index < members.size - 1) {
                    Spacer(modifier = Modifier.height(BottariTheme.spacing.spaceSmall))
                    HorizontalDivider(
                        color = BottariTheme.colors.gray400,
                        thickness = 1.dp,
                    )
                    Spacer(modifier = Modifier.height(BottariTheme.spacing.spaceSmall))
                }
            }
        }
    }
}

private fun shareInvite(
    context: Context,
    inviteCode: String,
    bottariTitle: String,
) {
    val shareMessage =
        generateShareMessage(
            context = context,
            inviteCode = inviteCode,
            bottariTitle = bottariTitle,
        )

    val sendIntent =
        Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, shareMessage)
            type = "text/plain"
        }

    context.startActivity(Intent.createChooser(sendIntent, null))
}

private fun generateShareMessage(
    context: Context,
    inviteCode: String,
    bottariTitle: String,
): String =
    context.getString(
        R.string.team_management_share_template_text,
        bottariTitle,
        inviteCode,
        createDeeplink(inviteCode),
    )

@Preview(showBackground = true)
@Composable
private fun MemberEditScreenPreview() {
    MemberEditContent(
        bottariTitle = "테스트보따리",
        uiState =
            TeamManagementUiState(
                members =
                    listOf(
                        TeamMemberUiModel(id = 1, nickname = "테스트", isHost = true),
                        TeamMemberUiModel(id = 2, nickname = "테스트2", isHost = false),
                        TeamMemberUiModel(id = 3, nickname = "테스트3", isHost = false),
                    ),
                teamMemberHeadCount = 3,
                maxHeadCount = 10,
            ),
        onShareClick = {},
        modifier = Modifier.padding(BottariTheme.spacing.spaceMedium),
    )
}
