package com.bottari.presentation.compose.team.checklist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.component.BottariBox
import com.bottari.presentation.compose.common.component.ChecklistToolTip
import com.bottari.presentation.compose.common.theme.BottariTheme
import com.bottari.presentation.compose.personal.ChecklistProgressHeader
import com.bottari.presentation.compose.personal.checklist.PersonalChecklistItem
import com.bottari.presentation.model.bottari.personal.BottariItemTypeUiModel
import com.bottari.presentation.model.bottari.team.TeamChecklistItemUiModel
import kotlin.random.Random

@Composable
fun TeamChecklistScreen(
    uiState: ComposeTeamChecklistUiState,
    isToolTipClosed: Boolean,
    onCloseToolTip: () -> Unit,
    onClickSection: (BottariItemTypeUiModel) -> Unit,
    onClickItem: (Long, BottariItemTypeUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.Top) {
        item {
            if (!isToolTipClosed) {
                ChecklistToolTip(
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_shared),
                            contentDescription = null,
                            tint = BottariTheme.colors.white,
                        )
                    },
                    title = "팀 보따리",
                    text = "공통은 모두가, 담당은 지정된 사람이,\n개인은 나만 볼 수 있는 체크리스트예요.",
                    closeAction = onCloseToolTip,
                )
                Spacer(Modifier.height(BottariTheme.spacing.spaceMedium))
            }
        }

        item {
            ChecklistProgressHeader(uiState.checkedQuantity, uiState.totalQuantity)
            Spacer(Modifier.height(BottariTheme.spacing.spaceMedium))
        }

        teamChecklistNodes(
            nodes = uiState.sections,
            items = uiState.bottariItems,
            toggleExpanded = onClickSection,
            onClickItem = onClickItem,
        )
    }
}

fun LazyListScope.teamChecklistNodes(
    nodes: Map<BottariItemTypeUiModel, Boolean>,
    items: List<TeamChecklistItemUiModel>,
    toggleExpanded: (BottariItemTypeUiModel) -> Unit,
    onClickItem: (Long, BottariItemTypeUiModel) -> Unit,
) {
    nodes.forEach { node ->
        val selectedItems = items.filter { item -> item.type == node.key }
        item {
            TeamChecklistNode(
                node = node,
                items = selectedItems,
                toggleExpanded = toggleExpanded,
                onClickItem = onClickItem,
            )
            Spacer(Modifier.height(BottariTheme.spacing.spaceMedium))
        }
    }
}

@Composable
fun TeamChecklistNode(
    node: Map.Entry<BottariItemTypeUiModel, Boolean>,
    items: List<TeamChecklistItemUiModel>,
    toggleExpanded: (BottariItemTypeUiModel) -> Unit,
    onClickItem: (Long, BottariItemTypeUiModel) -> Unit,
) {
    val sectionType = node.key
    val isOpened = node.value

    BottariBox(modifier = Modifier.fillMaxWidth()) {
        Column {
            SectionHeader(
                section = sectionType,
                isOpened = isOpened,
                itemCount = items.size,
                onToggle = toggleExpanded,
            )
            if (isOpened) {
                Spacer(Modifier.height(height = BottariTheme.spacing.spaceMedium))
                HorizontalDivider(color = BottariTheme.colors.gray400)

                Spacer(Modifier.height(height = BottariTheme.spacing.spaceMedium))

                items.forEach { bottariItem ->
                    PersonalChecklistItem(
                        bottariItem = bottariItem,
                        onClick = { onClickItem(bottariItem.id, bottariItem.type) },
                    )
                    Spacer(Modifier.height(height = BottariTheme.spacing.spaceSmall))
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(
    section: BottariItemTypeUiModel,
    isOpened: Boolean,
    itemCount: Int,
    onToggle: (BottariItemTypeUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    val painter =
        when (section) {
            is BottariItemTypeUiModel.ASSIGNED -> painterResource(R.drawable.ic_assigned)
            BottariItemTypeUiModel.PERSONAL -> painterResource(R.drawable.ic_personal)
            BottariItemTypeUiModel.SHARED -> painterResource(R.drawable.ic_shared)
        }

    val color =
        when (section) {
            is BottariItemTypeUiModel.ASSIGNED -> BottariTheme.colors.productTypeAssigned
            BottariItemTypeUiModel.PERSONAL -> BottariTheme.colors.productTypePersonal
            BottariItemTypeUiModel.SHARED -> BottariTheme.colors.productTypeShared
        }

    val title =
        when (section) {
            is BottariItemTypeUiModel.ASSIGNED -> stringResource(R.string.bottari_item_type_assigned_text)
            BottariItemTypeUiModel.PERSONAL -> stringResource(R.string.bottari_item_type_personal_text)
            BottariItemTypeUiModel.SHARED -> stringResource(R.string.bottari_item_type_shared_text)
        }
    Row(
        modifier = modifier.clickable(onClick = { onToggle(section) }),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painter,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(24.dp),
        )
        Spacer(Modifier.width(BottariTheme.spacing.spaceSmall))
        Column {
            Text(
                text = title,
                style = BottariTheme.typography.bold18.toTextStyle(),
                color = BottariTheme.colors.black,
            )
            Text(
                text = stringResource(R.string.team_checklist_current_items_count_text, itemCount),
                style = BottariTheme.typography.regular14.toTextStyle(),
                color = BottariTheme.colors.gray700,
            )
        }
        Spacer(modifier.weight(1f))
        if (isOpened) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_up),
                contentDescription = null,
                tint = BottariTheme.colors.gray400,
            )
            return@Row
        }
        Icon(
            painter = painterResource(R.drawable.ic_arrow_up),
            contentDescription = null,
            modifier = Modifier.rotate(180f),
            tint = BottariTheme.colors.gray400,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TeamChecklistScreenPreview() {
    TeamChecklistScreen(
        uiState = dummyUiState,
        isToolTipClosed = false,
        onCloseToolTip = {},
        onClickSection = {},
        onClickItem = { _, _ -> },
    )
}

@Preview(showBackground = true)
@Composable
private fun SectionHeaderPreview() {
    SectionHeader(
        section = BottariItemTypeUiModel.PERSONAL,
        onToggle = {},
        isOpened = false,
        itemCount = 4,
        modifier = Modifier.fillMaxWidth(),
    )
}

private fun createDummyProductList(
    count: Int,
    type: BottariItemTypeUiModel,
    idStartIndex: Long = 0,
): List<TeamChecklistItemUiModel> =
    List(count) { index ->
        TeamChecklistItemUiModel(
            id = idStartIndex + index,
            name = "더미 아이템 ${idStartIndex + index + 1}",
            isChecked = Random.nextBoolean(),
            type = type,
        )
    }

private val dummyUiState =
    ComposeTeamChecklistUiState(
        isLoading = false,
        bottariItems =
            createDummyProductList(
                10,
                BottariItemTypeUiModel.SHARED,
            ) + createDummyProductList(10, BottariItemTypeUiModel.PERSONAL) +
                createDummyProductList(10, BottariItemTypeUiModel.ASSIGNED()),
        sections =
            mapOf(
                BottariItemTypeUiModel.SHARED to true,
                BottariItemTypeUiModel.ASSIGNED() to true,
                BottariItemTypeUiModel.PERSONAL to true,
            ),
    )
