package com.bottari.presentation.compose.team.member

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.component.BottariBox
import com.bottari.presentation.compose.common.component.BottariCheckIndicator
import com.bottari.presentation.compose.common.theme.BottariTheme
import com.bottari.presentation.model.bottari.PersonalChecklistItemUiModel
import com.bottari.presentation.model.bottari.team.member.TeamMemberStatusUiModel
import com.bottari.presentation.model.bottari.team.member.TeamMemberUiModel

@Composable
fun TeamMemberStateCard(
    memberStatus: TeamMemberStatusUiModel,
    onClick : () -> Unit,
    modifier: Modifier = Modifier,
) {

    BottariBox(modifier = modifier.clickable(onClick = onClick), contentPadding = PaddingValues(21.dp)) {
        Column {
            Row (verticalAlignment = Alignment.CenterVertically){

                Text(
                    text = memberStatus.member.nickname,
                    style = BottariTheme.typography.semiBold20.toTextStyle(),
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
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
            )
        }
    }
}

@Preview
@Composable
private fun TeamProductStateCardPreview(){
    TeamMemberStateCard(
        memberStatus = dummyData[0],
        onClick = {}
    )
}


val dummyData = List(10) { index ->
    val memberId = index + 1
    val isHost = index == 0
    val isMe = index == 1
    val checkedCount = (5..15).random()
    val totalCount = checkedCount + (2..7).random()

    TeamMemberStatusUiModel(
        member = TeamMemberUiModel(
            id = memberId.toLong(),
            nickname = "멤버 $memberId",
            isHost = isHost
        ),
        totalItemsCount = totalCount,
        checkedItemsCount = checkedCount,
        sharedItems = listOf(
            PersonalChecklistItemUiModel(
                id = (100 + index).toLong(),
                name = "공유 아이템 ${index + 1}",
                isChecked = false
            )
        ),
        assignedItems = listOf(
            PersonalChecklistItemUiModel(
                id = (200 + index).toLong(),
                name = "담당 아이템 ${index + 1}",
                isChecked = true
            )
        ),
        isMe = isMe,
        isExpanded = false
    )
}