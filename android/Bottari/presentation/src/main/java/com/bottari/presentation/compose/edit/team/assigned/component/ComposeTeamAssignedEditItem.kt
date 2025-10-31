package com.bottari.presentation.compose.edit.team.assigned.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.component.BottariBox
import com.bottari.presentation.compose.common.theme.BottariTheme

@Composable
fun ComposeTeamAssignedEditItem(
    title: String,
    members: List<String>,
    onClickEdit: () -> Unit,
    onClickDelete: () -> Unit,
) {
    BottariBox(
        contentPadding =
            PaddingValues(
                top = BottariTheme.spacing.spaceXSmall,
                bottom = BottariTheme.spacing.spaceMedium,
                start = BottariTheme.spacing.spaceMedium,
                end = BottariTheme.spacing.spaceXSmall,
            ),
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = title,
                    style = BottariTheme.typography.medium20.toTextStyle(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                )
                IconButton(onClick = onClickEdit) {
                    Icon(painter = painterResource(R.drawable.ic_pen), contentDescription = "수정 버튼")
                }
                IconButton(onClick = onClickDelete) {
                    Icon(
                        painter = painterResource(R.drawable.ic_delete),
                        contentDescription = "수정 버튼",
                    )
                }
            }
            FlowRow(
                modifier = Modifier.padding(end = BottariTheme.spacing.spaceXSmall),
                horizontalArrangement = Arrangement.spacedBy(BottariTheme.spacing.spaceSmall),
            ) {
                members.forEachIndexed { index, member ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier =
                                Modifier
                                    .clip(CircleShape)
                                    .background(BottariTheme.colors.primary)
                                    .size(6.dp),
                        )
                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = member,
                            style = BottariTheme.typography.regular14.toTextStyle(),
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ComposeTeamAssignedEditItemPreview() {
    ComposeTeamAssignedEditItem(
        title = "이이이이이이이이이이이이이이이이이이이이이이잉름",
        members =
            listOf(
                "시아1",
                "시아2",
                "시아3",
                "시아4",
                "시아5",
                "시아6",
                "시아7",
                "시아8",
                "시아9",
                "시아10",
                "시아11",
                "시아12",
                "시아13",
                "시아14",
                "시아15",
            ),
        onClickDelete = {},
        onClickEdit = {},
    )
}
