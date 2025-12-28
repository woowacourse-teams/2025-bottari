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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.bottari.designsystem.component.BottariCard
import com.bottari.bottari.designsystem.component.BottariIconButton
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.presentation.R

@Composable
fun TeamAssignedEditItem(
    title: String,
    assignedMembers: List<Pair<String, Int>>,
    onClickEdit: () -> Unit,
    onClickDelete: () -> Unit,
) {
    BottariCard(
        contentPadding =
            PaddingValues(
                top = BottariTheme.spacing.spaceXSmall,
                bottom = BottariTheme.spacing.spaceMedium,
                start = BottariTheme.spacing.spaceMedium,
                end = BottariTheme.spacing.spaceXSmall,
            ),
    ) {
        Column {
            ItemHeader(
                title = title,
                onClickEdit = onClickEdit,
                onClickDelete = onClickDelete,
            )
            AssignedMembersList(
                assignedMembers = assignedMembers,
                modifier = Modifier.padding(end = BottariTheme.spacing.spaceXSmall),
            )
        }
    }
}

@Composable
private fun ItemHeader(
    title: String,
    onClickEdit: () -> Unit,
    onClickDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = BottariTheme.typography.medium20.toTextStyle(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )
        BottariIconButton(onClick = onClickEdit) {
            Icon(painter = painterResource(R.drawable.ic_pen), contentDescription = "수정 버튼")
        }
        BottariIconButton(onClick = onClickDelete) {
            Icon(
                painter = painterResource(R.drawable.ic_delete),
                contentDescription = "삭제 버튼",
            )
        }
    }
}

@Composable
private fun AssignedMembersList(
    assignedMembers: List<Pair<String, Int>>,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(BottariTheme.spacing.spaceSmall),
    ) {
        assignedMembers.forEach { (name, colorIndex) ->
            AssignedMemberChip(
                name = name,
                colorIndex = colorIndex,
            )
        }
    }
}

@Composable
private fun AssignedMemberChip(
    name: String,
    colorIndex: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val color =
            BottariTheme.colors.memberColors.getOrNull(colorIndex) ?: BottariTheme.colors.primary
        Box(
            modifier =
                Modifier
                    .clip(CircleShape)
                    .background(color)
                    .size(6.dp),
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = name,
            style = BottariTheme.typography.regular14.toTextStyle(),
        )
    }
}

@Preview
@Composable
private fun TeamAssignedEditItemPreview() {
    TeamAssignedEditItem(
        title = "이이이이이이이이이이이이이이이이이이이이이이잉름",
        assignedMembers = listOf(Pair("이름", 1), Pair("이름이 긴 멤버", 2), Pair("멤버 3", 3)),
        onClickDelete = {},
        onClickEdit = {},
    )
}
