package com.bottari.presentation.compose.team.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bottari.bottari.designsystem.component.BottariCard
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.core.ui.component.BottariCheckIndicator
import com.bottari.bottari.designsystem.component.BottariCircularLoader
import com.bottari.presentation.R
import com.bottari.presentation.compose.team.TeamSendRemindDialog
import com.bottari.presentation.compose.team.TeamStateCard
import com.bottari.presentation.compose.team.TeamStateListBox
import com.bottari.presentation.model.bottari.personal.BottariItemTypeUiModel
import com.bottari.presentation.model.bottari.team.TeamBottariUiModelStatus
import com.bottari.presentation.model.bottari.team.TeamChecklistItemUiModel

@Composable
fun TeamItemStateScreen(
    checkedState: List<TeamChecklistItemUiModel>,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    viewModel: ComposeTeamBottariItemStatusViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(checkedState) { viewModel.fetchTeamStatus() }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                ComposeTeamBottariItemStatusUiEvent.FetchTeamBottariItemStatusFailure -> {
                    snackbarHostState.showSnackbar("보따리를 불러오지 못했어요")
                }

                ComposeTeamBottariItemStatusUiEvent.SendRemindFailure -> {
                    snackbarHostState.showSnackbar("보채기에 실패했어요")
                }

                ComposeTeamBottariItemStatusUiEvent.SendRemindSuccess -> {
                    snackbarHostState.showSnackbar("보채기에 성공했어요")
                }
            }
        }
    }

    TeamItemStateScreen(
        uiState = uiState,
        onSelectProduct = viewModel::selectItem,
        onSendRemind = { item -> viewModel.debouncedSendRemindByItem(item) },
        modifier = modifier,
    )
}

@Composable
private fun TeamItemStateScreen(
    uiState: ComposeTeamBottariItemStatusUiState,
    onSelectProduct: (TeamBottariUiModelStatus?) -> Unit,
    onSendRemind: (TeamBottariUiModelStatus) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box {
        if (uiState.isInitialLoading) {
            BottariCircularLoader()
            return@Box
        }

        Column(
            modifier =
                modifier
                    .fillMaxSize()
                    .padding(BottariTheme.spacing.spaceMedium),
            verticalArrangement = Arrangement.Top,
        ) {
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
                    painter = painterResource(R.drawable.ic_complete),
                    color = BottariTheme.colors.green,
                    modifier = Modifier.weight(1f),
                )
            }
            Spacer(modifier = Modifier.height(BottariTheme.spacing.space2xLarge))
            if (uiState.items.isEmpty()) {
                TeamBottariItemStatusEmptyView(modifier = Modifier.fillMaxSize())
                return
            }
            LazyColumn(
                contentPadding = PaddingValues(bottom = BottariTheme.spacing.spaceMedium),
                verticalArrangement =
                    Arrangement.spacedBy(
                        BottariTheme.spacing.spaceXSmall,
                    ),
            ) {
                items(uiState.items, key = { "${it.id} ${it.type.toTypeString()}" }) { product ->
                    TeamProductStateCard(product = product, onClick = onSelectProduct)
                }
            }
        }
        uiState.selectedProduct?.let { product ->
            TeamSendRemindDialog(
                title = product.name,
                enableRemind = (!product.isAllChecked && !uiState.isOnlyMeUnchecked),
                onDismissRequest = { onSelectProduct(null) },
                onClickRemind = { onSendRemind(product) },
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(BottariTheme.spacing.spaceXSmall)) {
                    if (product.checkedMember.isNotEmpty()) {
                        TeamStateListBox(
                            text = "해당 물건을 챙겼습니다",
                            painter = painterResource(id = R.drawable.ic_bottari_item_empty_view),
                            color = BottariTheme.colors.primary,
                            items = product.checkedMember,
                        )
                    }
                    if (product.uncheckedMember.isNotEmpty()) {
                        TeamStateListBox(
                            text = "해당 물건을 챙기지 않았습니다.",
                            painter = painterResource(id = R.drawable.ic_bottari_item_empty_view),
                            color = BottariTheme.colors.red,
                            items = product.uncheckedMember,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TeamProductStateCard(
    product: TeamBottariUiModelStatus,
    onClick: (TeamBottariUiModelStatus) -> Unit = {},
) {
    val type =
        when (product.type) {
            is BottariItemTypeUiModel.ASSIGNED -> stringResource(R.string.bottari_item_type_assigned_text)
            BottariItemTypeUiModel.PERSONAL -> stringResource(R.string.bottari_item_type_personal_text)
            BottariItemTypeUiModel.SHARED -> stringResource(R.string.bottari_item_type_shared_text)
        }
    BottariCard(onClick = { onClick(product) }) {
        Column {
            Row {
                Text(
                    text = product.name,
                    style = BottariTheme.typography.bold18.toTextStyle(),
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
private fun TeamItemStateScreenPreview() {
    TeamItemStateScreen(
        uiState = teamBottariItemStatusDummyUiState.copy(selectedProduct = null),
        onSelectProduct = {},
        onSendRemind = {},
    )
}

@Preview
@Composable
private fun TeamProductStateCardPreview() {
    TeamProductStateCard(teamBottariItemStatusDummyUiState.items.first())
}
