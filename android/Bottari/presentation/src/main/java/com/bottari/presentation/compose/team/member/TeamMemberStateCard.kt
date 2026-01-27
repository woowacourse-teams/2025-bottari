package com.bottari.presentation.compose.team.member

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.component.BottariBox
import com.bottari.presentation.compose.common.component.BottariCheckIndicator
import com.bottari.presentation.compose.common.theme.BottariTheme
import com.bottari.presentation.model.bottari.team.member.TeamMemberStatusUiModel
import dummyMembers

@Composable
fun TeamMemberStateCard(
    memberStatus: TeamMemberStatusUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BottariBox(
        modifier =
            modifier.clickable(
                onClick = onClick,
                indication =
                    ripple(
                        bounded = true,
                        color = BottariTheme.colors.primary,
                    ),
                interactionSource = remember { MutableInteractionSource() },
            ),
        contentPadding = PaddingValues(21.dp),
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = memberStatus.member.nickname,
                    style = BottariTheme.typography.bold18.toTextStyle(),
                    color = BottariTheme.colors.black,
                )
                Spacer(modifier = Modifier.weight(1f))
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text =
                            stringResource(
                                R.string.team_checklist_current_items_status_progress_text,
                                memberStatus.checkedItemsCount,
                                memberStatus.totalItemsCount,
                            ),
                        style = BottariTheme.typography.semiBold16.toTextStyle(),
                        color = BottariTheme.colors.black,
                    )
                    Text(
                        text =
                            stringResource(
                                R.string.team_checklist_current_item_status_percent_text,
                                memberStatus.checkedProgress,
                            ),
                        style = BottariTheme.typography.regular14.toTextStyle(),
                        color = BottariTheme.colors.black,
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            BottariCheckIndicator(
                checkedQuantity = memberStatus.checkedItemsCount,
                totalQuantity = memberStatus.totalItemsCount,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(8.dp),
            )
        }
    }
}

@Preview
@Composable
private fun TeamProductStateCardPreview() {
    TeamMemberStateCard(
        memberStatus = dummyMembers[0],
        onClick = {},
    )
}
