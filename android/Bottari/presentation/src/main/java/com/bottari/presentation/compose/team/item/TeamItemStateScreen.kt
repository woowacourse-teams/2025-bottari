package com.bottari.presentation.compose.team.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.component.BottariBox
import com.bottari.presentation.compose.common.component.BottariCheckIndicator
import com.bottari.presentation.compose.common.theme.BottariTheme
import com.bottari.presentation.compose.team.TeamStateCard
import com.bottari.presentation.model.bottari.personal.BottariItemTypeUiModel
import com.bottari.presentation.model.bottari.team.TeamBottariUiModelStatus
import com.bottari.presentation.model.bottari.team.member.MemberCheckStatusUiModel
import kotlin.random.Random

@Composable
fun TeamItemStateScreen(
    modifier: Modifier = Modifier,
    viewModel: ComposeTeamBottariItemStatusViewModel = viewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val uiEvent = viewModel.uiEvent.collectAsStateWithLifecycle(initialValue = null)

    TeamItemStateScreen(uiState = uiState.value, modifier = modifier)
}

@Composable
fun TeamItemStateScreen(
    uiState: ComposeTeamBottariItemStatusUiState,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize().padding(BottariTheme.spacing.spaceMedium), verticalArrangement = Arrangement.Top) {
        Row {
            TeamStateCard(
                title =
                    stringResource(
                        R.string.team_checklist_current_items_status_percent_title,
                        uiState.checkedProgress,
                    ),
                value =
                    stringResource(
                        R.string.team_checklist_current_items_status_percent_text,
                        uiState.checkedProgress,
                    ),
                painter = painterResource(R.drawable.ic_progress),
                color = BottariTheme.colors.primary,
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(BottariTheme.spacing.spaceXSmall))
            TeamStateCard(
                title = stringResource(R.string.team_checklist_current_items_status_count_title),
                value = uiState.completedItems.toString(),
                painter = painterResource(R.drawable.ic_compleate),
                color = BottariTheme.colors.green,
                modifier = Modifier.weight(1f),
            )
        }
        Spacer(modifier = Modifier.height(BottariTheme.spacing.space2xLarge))

        LazyColumn(
            contentPadding = PaddingValues(bottom = BottariTheme.spacing.spaceMedium),
            verticalArrangement =
                Arrangement.spacedBy(
                    BottariTheme.spacing.spaceXSmall,
                ),
        ) {
            uiState.items.forEach { item ->
                item {
                    TeamProductStateCard(product = item)
                }
            }
        }
    }
}

@Composable
private fun TeamProductStateCard(
    product: TeamBottariUiModelStatus,
    modifier: Modifier = Modifier,
) {
    val type =
        when (product.type) {
            is BottariItemTypeUiModel.ASSIGNED -> stringResource(R.string.bottari_item_type_assigned_text)
            BottariItemTypeUiModel.PERSONAL -> stringResource(R.string.bottari_item_type_personal_text)
            BottariItemTypeUiModel.SHARED -> stringResource(R.string.bottari_item_type_shared_text)
        }
    BottariBox(modifier = modifier, contentPadding = PaddingValues(21.dp)) {
        Column {
            Row {
                Text(
                    text = product.name,
                    style = BottariTheme.typography.semiBold16.toTextStyle(),
                    color = BottariTheme.colors.black,
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text =
                        stringResource(
                            R.string.team_checklist_current_items_status_progress_text,
                            product.checkItemsCount,
                            product.totalItemsCount,
                        ),
                    style = BottariTheme.typography.semiBold16.toTextStyle(),
                    color = BottariTheme.colors.black,
                )
            }
            Row {
                Text(
                    text = type,
                    style = BottariTheme.typography.regular14.toTextStyle(),
                    color = BottariTheme.colors.black,
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text =
                        stringResource(
                            R.string.team_checklist_current_item_status_percent_text,
                            product.checkedProgress,
                        ),
                    style = BottariTheme.typography.regular14.toTextStyle(),
                    color = BottariTheme.colors.black,
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            BottariCheckIndicator(
                checkedQuantity = product.checkItemsCount,
                totalQuantity = product.totalItemsCount,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(8.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TeamItemStateScreenPreview() {
    TeamItemStateScreen(
        uiState = dummyUiState,
    )
}

@Preview
@Composable
fun TeamProductStateCardPreview() {
    TeamProductStateCard(dummyUiState.items.first())
}

private fun createDummyProductList(
    count: Int,
    type: BottariItemTypeUiModel,
    idStartIndex: Long = 0,
): List<TeamBottariUiModelStatus> =
    List(count) { index ->
        val memberCount = Random.nextInt(2, 6)
        val memberCheckStatus =
            List(memberCount) { memberIndex ->
                MemberCheckStatusUiModel(
                    name = "멤버 ${'A' + memberIndex}",
                    checked = Random.nextBoolean(),
                )
            }
        val checkedCount = memberCheckStatus.count { it.checked }

        TeamBottariUiModelStatus(
            id = idStartIndex + index,
            name = "더미 아이템 ${idStartIndex + index + 1}",
            memberCheckStatus = memberCheckStatus,
            checkItemsCount = checkedCount,
            totalItemsCount = memberCheckStatus.size,
            type = type,
        )
    }

private val dummyUiState =
    run {
        val dummySharedItems = createDummyProductList(10, BottariItemTypeUiModel.SHARED)
        val dummyAssignedItems = createDummyProductList(5, BottariItemTypeUiModel.ASSIGNED())

        ComposeTeamBottariItemStatusUiState(
            isLoading = false,
            items = dummySharedItems + dummyAssignedItems,
            selectedProduct = dummySharedItems.firstOrNull(), // 첫 번째 공유 아이템을 선택된 상태로 설정
        )
    }
