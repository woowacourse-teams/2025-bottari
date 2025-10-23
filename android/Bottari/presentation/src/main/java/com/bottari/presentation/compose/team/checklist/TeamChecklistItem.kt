package com.bottari.presentation.compose.team.checklist

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.presentation.compose.common.theme.BottariTheme
import com.bottari.presentation.compose.personal.checklist.BottariCheckBox
import com.bottari.presentation.model.bottari.PersonalChecklistItemUiModel
import com.bottari.presentation.model.bottari.team.ChecklistItemUiModel

@Composable
fun TeamChecklistItem(
    bottariItem: ChecklistItemUiModel,
    onClick: () -> Unit,
) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(
                    color = BottariTheme.colors.white,
                    shape = RoundedCornerShape(12.dp),
                )
                .border(
                    width = 2.dp,
                    color = BottariTheme.colors.gray200,
                    shape = RoundedCornerShape(12.dp),
                )
                .clip(RoundedCornerShape(12.dp))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(bounded = true, color = BottariTheme.colors.primary),
                    onClick = { onClick() },
                )
                .padding(BottariTheme.spacing.spaceMedium)
                .semantics(mergeDescendants = true) {
                    contentDescription = bottariItem.name
                    stateDescription = if (bottariItem.isChecked) "완료" else "미완료"
                },
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier =
                    Modifier
                        .size(5.dp)
                        .clip(shape = CircleShape)
                        .background(BottariTheme.colors.black),
            )
            Spacer(modifier = Modifier.width(BottariTheme.spacing.spaceMedium))
            Text(
                text = bottariItem.name,
                style = BottariTheme.typography.semiBold16.toTextStyle(),
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(BottariTheme.spacing.spaceMedium))
            BottariCheckBox(bottariItem.isChecked)
        }
    }
}

@Preview
@Composable
private fun TeamChecklistItemPreview() {
    TeamChecklistItem(
        bottariItem = PersonalChecklistItemUiModel(1, "테스트", true),
        onClick = {},
    )
}
