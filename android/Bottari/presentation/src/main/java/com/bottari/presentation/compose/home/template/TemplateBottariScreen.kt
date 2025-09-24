package com.bottari.presentation.compose.home.template

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.component.CollapsedListLine
import com.bottari.presentation.compose.common.extension.topBottomFadingEdge
import com.bottari.presentation.compose.common.theme.BottariTheme
import com.bottari.presentation.model.template.BottariTemplateItemUiModel
import com.bottari.presentation.model.template.BottariTemplateUiModel

@Composable
fun TemplateBottariScreen(
    navigateToTemplateDetail: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TemplateViewModel = viewModel(factory = TemplateViewModel.Factory()),
) {
    val uiState = viewModel.uiState.observeAsState().value ?: return
    val uiEvent = viewModel.uiEvent.observeAsState().value

    LaunchedEffect(uiEvent) {
        if (uiEvent == null) return@LaunchedEffect

        when (uiEvent) {
            is TemplateUiEvent.FetchBottariTemplatesFailure -> Unit
        }
    }

    TemplateBottariScreen(
        uiState = uiState,
        onClickDetail = navigateToTemplateDetail,
        modifier = modifier,
    )
}

@Composable
private fun TemplateBottariScreen(
    uiState: TemplateUiState,
    onClickDetail: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier =
                Modifier
                    .padding(horizontal = BottariTheme.spacing.spaceLarge)
                    .topBottomFadingEdge(color = Color.White),
            contentPadding = PaddingValues(vertical = BottariTheme.spacing.spaceSmall),
            verticalArrangement = Arrangement.spacedBy(BottariTheme.spacing.spaceSmall),
        ) {
            items(uiState.templates, key = { template -> template.id }) { template ->
                TemplateItem(
                    template = template,
                    onClick = onClickDetail,
                )
            }
        }
    }
}

@Composable
private fun TemplateItem(
    template: BottariTemplateUiModel,
    onClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        colors =
            CardDefaults.cardColors(
                containerColor = BottariTheme.colors.white,
                contentColor = BottariTheme.colors.gray600,
            ),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            TemplateItemHeader(template, onClick)
            HorizontalDivider(
                color = BottariTheme.colors.gray200,
                modifier =
                    Modifier.padding(
                        top = BottariTheme.spacing.spaceSmall,
                        bottom = BottariTheme.spacing.spaceXSmall,
                    ),
            )
            TemplateItemFooter(template)
        }
    }
}

@Composable
private fun TemplateItemHeader(
    template: BottariTemplateUiModel,
    onClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(BottariTheme.spacing.spaceSmall),
        ) {
            Text(
                text = template.title,
                style = BottariTheme.typography.semiBold16.toTextStyle(),
                color = BottariTheme.colors.black,
            )
            CollapsedListLine(
                items = template.items.map { it.name },
                textStyle = BottariTheme.typography.regular12.toTextStyle(),
            )
        }

        TextButton(
            onClick = { onClick(template.id) },
            shape = RoundedCornerShape(12.dp),
            colors =
                ButtonDefaults.textButtonColors(
                    contentColor = BottariTheme.colors.white,
                    containerColor = BottariTheme.colors.primary,
                ),
        ) {
            Text(
                text = "자세히",
                style = BottariTheme.typography.medium14.toTextStyle(),
            )
        }
    }
}

@Composable
private fun TemplateItemFooter(
    template: BottariTemplateUiModel,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(BottariTheme.spacing.spaceXSmall),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(painter = painterResource(R.drawable.ic_person), contentDescription = null)
        Text(
            text = template.author,
            style = BottariTheme.typography.regular12.toTextStyle(),
        )

        Icon(painter = painterResource(R.drawable.ic_download), contentDescription = null)
        Text(
            text = stringResource(R.string.template_taken_count_prefix, template.takenCount),
            style = BottariTheme.typography.regular12.toTextStyle(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TemplateItemPreview() {
    val template =
        BottariTemplateUiModel(
            id = 1,
            title = "우테코출근보따리글자수열다섯자",
            author = "다이스",
            takenCount = 100024,
            items = List(10) { BottariTemplateItemUiModel(it.toLong(), "아이템 $it") },
        )

    BottariTheme {
        Box(modifier = Modifier.padding(12.dp)) {
            TemplateItem(template = template, onClick = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TemplateBottariScreenPreview() {
    val items =
        List(10) { item ->
            BottariTemplateItemUiModel(
                id = item.toLong(),
                name = "아이템 $item",
            )
        }
    val templates =
        List(10) { index ->
            BottariTemplateUiModel(
                id = index.toLong(),
                title = "우테코출근보따리글자수열다섯자 $index",
                author = "다이스",
                takenCount = 100_024 + index,
                items = items,
            )
        }

    BottariTheme {
        TemplateBottariScreen(
            uiState = TemplateUiState(templates = templates),
            onClickDetail = {},
        )
    }
}
