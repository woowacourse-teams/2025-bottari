package com.bottari.feature.personal.checklist.checklist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.bottari.designsystem.component.BottariCard
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.core.ui.model.bottari.PersonalChecklistItemUiModel
import com.bottari.core.ui.model.bottari.team.ChecklistItemUiModel

@Composable
fun PersonalChecklistItem(
    bottariItem: ChecklistItemUiModel,
    onClick: () -> Unit,
) {
    BottariCard(
        modifier =
            Modifier
                .fillMaxWidth()
                .semantics(mergeDescendants = true) {
                    contentDescription = bottariItem.name
                    stateDescription = if (bottariItem.isChecked) "완료" else "미완료"
                },
        onClick = onClick,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier =
                    Modifier
                        .size(5.dp)
                        .clip(shape = BottariTheme.shapes.circle)
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
private fun PersonalChecklistItemPreview() {
    var item by remember { mutableStateOf(PersonalChecklistItemUiModel(1, "테스트", true)) }
    PersonalChecklistItem(
        bottariItem = item,
        onClick = { item = item.copy(isChecked = !item.isChecked) },
    )
}
