package com.bottari.presentation.compose.edit.team.member

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bottari.core.designsystem.component.BottariBox
import com.bottari.core.designsystem.component.IndeterminateCircularIndicator
import com.bottari.core.designsystem.theme.BottariTheme
import com.bottari.presentation.R
import com.bottari.presentation.model.bottari.team.member.TeamMemberUiModel
import com.bottari.presentation.util.DeeplinkHelper.createDeeplink

@Composable
fun MemberEditScreen(
    bottariTitle: String,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    viewModel: TeamManagementViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                TeamManagementUiEvent.FetchTeamMembersFailure -> snackbarHostState.showSnackbar("팀 멤버 불러오기에 실패했습니다")
            }
        }
    }

    MemberEditScreen(
        bottariTitle = bottariTitle,
        uiState = uiState,
        modifier = modifier,
    )
}

@Composable
private fun MemberEditScreen(
    bottariTitle: String,
    uiState: TeamManagementUiState,
    modifier: Modifier = Modifier,
) {
    var isOpenShareInvite by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(isOpenShareInvite) {
        if (isOpenShareInvite) {
            shareInvite(
                context = context,
                inviteCode = uiState.inviteCode,
                bottariTitle = bottariTitle,
            )
            isOpenShareInvite = false
        }
    }
    Box {
        Column(modifier = modifier.fillMaxSize()) {
            ShareInviteItem(onShareClick = { isOpenShareInvite = true })

            Spacer(Modifier.height(BottariTheme.spacing.spaceLarge))

            TeamBottariMemberList(
                members = uiState.members,
                teamMemberHeadCount = uiState.teamMemberHeadCount,
                maxHeadCount = uiState.maxHeadCount,
            )
        }
        if (uiState.isLoading) IndeterminateCircularIndicator()
    }
}

@Composable
private fun ShareInviteItem(onShareClick: () -> Unit) {
    BottariBox(
        contentPadding =
            PaddingValues(
                horizontal = BottariTheme.spacing.spaceSmall,
                vertical = BottariTheme.spacing.space2xSmall,
            ),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.team_management_add_member_text),
                style = BottariTheme.typography.bold18.toTextStyle(),
            )
            Spacer(Modifier.weight(1f))
            IconButton(onShareClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_share),
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
    BottariBox(
        modifier = Modifier.fillMaxWidth(),
        contentPadding =
            PaddingValues(
                bottom = 0.dp,
                top = BottariTheme.spacing.spaceMedium,
                start = BottariTheme.spacing.spaceMedium,
                end = BottariTheme.spacing.spaceMedium,
            ),
    ) {
        Column {
            Row {
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
            Spacer(Modifier.height(BottariTheme.spacing.spaceSmall))
            HorizontalDivider(
                color = BottariTheme.colors.gray500,
                thickness = 1.dp,
            )
            members.forEachIndexed { index, member ->
                Text(
                    text = member.nickname,
                    style = BottariTheme.typography.bold18.toTextStyle(),
                    modifier = Modifier.padding(vertical = BottariTheme.spacing.spaceMedium),
                )
                if (index < members.size - 1) {
                    HorizontalDivider(
                        color = BottariTheme.colors.gray500,
                        thickness = 1.dp,
                    )
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
    val sendIntent: Intent =
        Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareMessage)
            type = "text/plain"
        }

    val shareIntent = Intent.createChooser(sendIntent, null)
    context.startActivity(shareIntent)
}

private fun generateShareMessage(
    context: Context,
    inviteCode: String,
    bottariTitle: String,
): String {
    val inviteLink = createDeeplink(inviteCode)
    return context.getString(
        R.string.team_management_share_template_text,
        bottariTitle,
        inviteCode,
        inviteLink,
    )
}

@Preview(showBackground = true)
@Composable
private fun MemberEditScreenPreview() {
    MemberEditScreen(
        bottariTitle = "테스트보따리",
        uiState =
            TeamManagementUiState(
                members =
                    listOf(
                        TeamMemberUiModel(
                            id = 1,
                            nickname = "테스트",
                            isHost = true,
                        ),
                        TeamMemberUiModel(
                            id = 2,
                            nickname = "테스트2",
                            isHost = false,
                        ),
                        TeamMemberUiModel(
                            id = 3,
                            nickname = "테스트3",
                            isHost = false,
                        ),
                    ),
                teamMemberHeadCount = 3,
                maxHeadCount = 10,
            ),
        modifier = Modifier.padding(BottariTheme.spacing.spaceMedium),
    )
}
