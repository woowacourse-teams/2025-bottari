package com.bottari.presentation.compose.personal.checklist

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.component.BottariBox
import com.bottari.presentation.compose.common.component.ChecklistToolTip
import com.bottari.presentation.compose.common.theme.BottariTheme
import com.bottari.presentation.compose.personal.ChecklistProgressHeader
import com.bottari.presentation.model.bottari.ChecklistItemUiModel

@Composable
fun PersonalChecklistScreen(
    isToolTipClosed: Boolean,
    onCloseToolTip: () -> Unit,
    checklistItems: List<ChecklistItemUiModel>,
    onClickItem: (Long) -> Unit,
    totalQuantity: Int,
    checkedQuantity: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .padding(horizontal = BottariTheme.spacing.spaceMedium)
            .padding(top = BottariTheme.spacing.spaceMedium),
        verticalArrangement =
            Arrangement.spacedBy(
                BottariTheme.spacing.spaceLarge,
            ),
    ) {
        if (!isToolTipClosed) {
            PersonalChecklistTooltip(onCloseToolTip)
        }
        ChecklistProgressHeader(checkedQuantity = checkedQuantity, totalQuantity = totalQuantity)
        PersonalChecklistLazyColumn(bottariItems = checklistItems, onClickItem = onClickItem)
    }
}

@Composable
fun PersonalChecklistTooltip(onCloseToolTip: () -> Unit) {
    val title = stringResource(R.string.checklist_tooltip_title_text)
    val text = stringResource(R.string.checklist_tooltip_description_text)
    ChecklistToolTip(
        title = title,
        text = text,
        icon = {
            Icon(
                painter = painterResource(R.drawable.ic_person),
                contentDescription = null,
                tint = BottariTheme.colors.white,
            )
        },
        closeAction = onCloseToolTip,
    )
}

@Composable
fun PersonalChecklistLazyColumn(
    bottariItems: List<ChecklistItemUiModel>,
    onClickItem: (Long) -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(BottariTheme.spacing.spaceXSmall),
    ) {
        items(
            items = bottariItems,
            key = { bottariItem -> bottariItem.id to bottariItem::class.java.simpleName },
        ) {
            PersonalChecklistItem(bottariItem = it, onClickItem = onClickItem)
        }
    }
}

@Composable
fun PersonalChecklistItem(
    bottariItem: ChecklistItemUiModel,
    onClickItem: (Long) -> Unit,
) {
    BottariBox(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(bounded = true, color = BottariTheme.colors.primary),
                    onClick = { onClickItem(bottariItem.id) },
                ),
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
            Text(bottariItem.name, style = BottariTheme.typography.medium20.toTextStyle())
            Spacer(modifier = Modifier.weight(1f))
            BottariCheckBox(bottariItem.isChecked)
        }
    }
}

@Composable
fun BottariCheckBox(isChecked: Boolean) {
    if (isChecked) {
        BottariCheckedBox()
    } else {
        BottariUncheckedBox()
    }
}

@Composable
private fun BottariUncheckedBox() {
    Box(
        modifier =
            Modifier
                .size(30.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(3.dp, BottariTheme.colors.gray400, RoundedCornerShape(8.dp)),
    )
}

@Composable
private fun BottariCheckedBox() {
    Box(
        modifier =
            Modifier
                .size(30.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(BottariTheme.colors.primary),
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_check),
            contentDescription = null,
            modifier = Modifier.padding(5.dp).fillMaxSize(),
            tint = Color.White,
        )
    }
}

@Preview
@Composable
private fun PersonalChecklistTooltipPreview() {
    PersonalChecklistTooltip({})
}

@Preview
@Composable
private fun PersonalChecklistItemPreview() {
    PersonalChecklistItem(
        bottariItem = ChecklistItemUiModel(1, "테스트", true),
        onClickItem = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun PersonalChecklistScreenPreview() {
    PersonalChecklistScreen(
        false,
        {},
        listOf(
            ChecklistItemUiModel(1, "테스트", false),
            ChecklistItemUiModel(2, "테스트", true),
            ChecklistItemUiModel(3, "테스트", true),
        ),
        {},
        7,
        3,
    )
}
