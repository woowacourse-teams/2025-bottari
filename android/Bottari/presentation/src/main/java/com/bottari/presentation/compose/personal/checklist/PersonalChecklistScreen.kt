package com.bottari.presentation.compose.personal.checklist

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.component.ChecklistToolTip
import com.bottari.presentation.compose.common.theme.BottariTheme
import com.bottari.presentation.compose.personal.ChecklistProgressHeader
import com.bottari.presentation.model.bottari.PersonalChecklistItemUiModel

@Composable
fun PersonalChecklistScreen(
    isToolTipClosed: Boolean,
    onCloseToolTip: () -> Unit,
    checklistItems: List<PersonalChecklistItemUiModel>,
    onClickItem: (Long) -> Unit,
    totalQuantity: Int,
    checkedQuantity: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
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
    bottariItems: List<PersonalChecklistItemUiModel>,
    onClickItem: (Long) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(bottom = BottariTheme.spacing.spaceLarge),
        verticalArrangement = Arrangement.spacedBy(BottariTheme.spacing.spaceXSmall),
    ) {
        items(
            items = bottariItems,
            key = { bottariItem -> bottariItem.id to bottariItem::class.java.simpleName },
        ) {
            PersonalChecklistItem(bottariItem = it, onClick = { onClickItem(it.id) })
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
            modifier = Modifier
                .padding(5.dp)
                .fillMaxSize(),
            tint = Color.White,
        )
    }
}

@Preview
@Composable
private fun PersonalChecklistTooltipPreview() {
    PersonalChecklistTooltip {}
}

@Preview(showBackground = true)
@Composable
private fun PersonalChecklistScreenPreview() {
    PersonalChecklistScreen(
        false,
        {},
        listOf(
            PersonalChecklistItemUiModel(1, "테스트", false),
            PersonalChecklistItemUiModel(2, "테스트", true),
            PersonalChecklistItemUiModel(3, "테스트", true),
        ),
        {},
        7,
        3,
    )
}
