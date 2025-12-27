package com.bottari.presentation.compose.template.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.core.ui.component.BottariBox
import com.bottari.core.ui.component.CollapsedListLine
import com.bottari.core.ui.extension.topBottomFadingEdge
import com.bottari.presentation.R
import com.bottari.presentation.model.bottari.BottariItemUiModel
import com.bottari.presentation.model.bottari.personal.BottariDetailUiModel
import com.bottari.presentation.model.bottari.personal.BottariItemTypeUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateBottariSelector(
    bottaries: List<BottariDetailUiModel>,
    bottomSheetState: SheetState,
    onClickBottari: (Long) -> Unit,
    onClickClose: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        sheetState = bottomSheetState,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        containerColor = BottariTheme.colors.white,
        contentColor = BottariTheme.colors.black,
        onDismissRequest = onDismissRequest,
        dragHandle = null,
        sheetGesturesEnabled = false,
        modifier = modifier,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f)
                    .padding(BottariTheme.spacing.spaceMedium),
            verticalArrangement = Arrangement.spacedBy(BottariTheme.spacing.spaceMedium),
        ) {
            CreateBottariSelectorHeader(
                onClickClose = {
                    scope.closeAfterAction(
                        action = { bottomSheetState.hide() },
                        onCompletion = onClickClose,
                    )
                },
            )

            if (bottaries.isEmpty()) {
                CreateBottariSelectorEmptyView()
                return@Column
            }

            CreateBottariSelectorContent(
                bottaries = bottaries,
                onClickBottari = { bottariId ->
                    scope.closeAfterAction(
                        action = { bottomSheetState.hide() },
                        onCompletion = { onClickBottari(bottariId) },
                    )
                },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

private fun CoroutineScope.closeAfterAction(
    action: suspend CoroutineScope.() -> Unit,
    onCompletion: () -> Unit,
) = launch { action() }.invokeOnCompletion { onCompletion() }

@Composable
private fun CreateBottariSelectorHeader(
    onClickClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "보따리 선택",
            style = BottariTheme.typography.bold22.toTextStyle(),
        )

        IconButton(
            onClick = onClickClose,
        ) {
            Icon(
                imageVector = Icons.Rounded.Close,
                contentDescription = "닫기",
            )
        }
    }
}

@Composable
private fun CreateBottariSelectorContent(
    bottaries: List<BottariDetailUiModel>,
    onClickBottari: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier =
            modifier.topBottomFadingEdge(
                color = BottariTheme.colors.white,
                width = BottariTheme.spacing.spaceXSmall,
            ),
        verticalArrangement = Arrangement.spacedBy(BottariTheme.spacing.spaceXSmall),
        contentPadding = PaddingValues(vertical = BottariTheme.spacing.spaceXSmall),
    ) {
        items(
            count = bottaries.size,
            key = { index -> bottaries[index].id },
        ) { index ->
            CreateBottariSelectorItem(
                bottari = bottaries[index],
                onClickBottari = onClickBottari,
            )
        }
    }
}

@Composable
private fun CreateBottariSelectorItem(
    bottari: BottariDetailUiModel,
    onClickBottari: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    BottariBox(
        modifier =
            modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(color = BottariTheme.colors.primary),
                ) { onClickBottari(bottari.id) },
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(BottariTheme.spacing.spaceXSmall),
        ) {
            Text(
                text = bottari.title,
                style = BottariTheme.typography.semiBold20.toTextStyle(),
            )

            CollapsedListLine(
                items = bottari.items.map { it.name },
                textStyle =
                    BottariTheme.typography.medium14.toTextStyle().copy(
                        color = BottariTheme.colors.gray700,
                    ),
            )
        }
    }
}

@Composable
private fun CreateBottariSelectorEmptyView(modifier: Modifier = Modifier) {
    Column(
        modifier =
            modifier
                .height(150.dp)
                .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_bottari),
            contentDescription = null,
            tint = BottariTheme.colors.gray400,
            modifier = Modifier.size(60.dp),
        )

        Spacer(modifier = Modifier.height(BottariTheme.spacing.spaceXSmall))

        Text(
            text = "선택 가능한 보따리가 존재하지 않아요",
            style = BottariTheme.typography.medium16.toTextStyle(),
            color = BottariTheme.colors.gray500,
        )

        Spacer(modifier = Modifier.height(BottariTheme.spacing.space2xLarge))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun CreateBottariSelectorPreview() {
    val sheetState = rememberModalBottomSheetState()
    var isOpen by remember { mutableStateOf(true) }

    BottariTheme {
        if (isOpen) {
            CreateBottariSelector(
                bottomSheetState = sheetState,
                onClickBottari = {},
                onClickClose = { isOpen = false },
                onDismissRequest = { isOpen = false },
                bottaries =
                    List(10) {
                        BottariDetailUiModel(
                            id = it.toLong(),
                            title = "신입$it",
                            alarm = null,
                            items =
                                List(10) { itemIndex ->
                                    BottariItemUiModel(
                                        id = itemIndex.toLong(),
                                        name = "아이템 $itemIndex",
                                        type = BottariItemTypeUiModel.PERSONAL,
                                    )
                                },
                        )
                    },
            )
        } else {
            TextButton(
                onClick = { isOpen = true },
                modifier = Modifier.padding(16.dp),
            ) { Text(text = "바텀 시트 열기") }
        }
    }
}
