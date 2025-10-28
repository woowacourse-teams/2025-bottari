package com.bottari.presentation.compose.home.template.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.presentation.compose.common.modifier.noRippleClickable
import com.bottari.presentation.compose.common.modifier.topBottomFadingEdge
import com.bottari.presentation.compose.common.theme.BottariTheme
import com.bottari.presentation.model.template.BottariTemplateHashtagUiModel
import com.bottari.presentation.model.template.BottariTemplateItemUiModel
import com.bottari.presentation.model.template.BottariTemplateUiModel

@Composable
fun TemplateColumn(
    type: TemplateItemType,
    templates: List<BottariTemplateUiModel>,
    listState: LazyListState,
    emptyViewText: String,
    showLoadingBlock: Boolean,
    onClickDetail: (Long) -> Unit,
    onClickDelete: (Long) -> Unit,
    onClickBookmark: (Long) -> Unit,
    onClickHashtag: (BottariTemplateHashtagUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        state = listState,
        modifier =
            modifier
                .fillMaxSize()
                .padding(horizontal = BottariTheme.spacing.spaceLarge)
                .topBottomFadingEdge(color = BottariTheme.colors.gray50, width = 8.dp),
        contentPadding = PaddingValues(vertical = BottariTheme.spacing.spaceSmall),
        verticalArrangement = Arrangement.spacedBy(BottariTheme.spacing.spaceSmall),
    ) {
        if (emptyViewText.isNotBlank() && templates.isEmpty()) {
            item {
                TemplateEmptyView(
                    text = emptyViewText,
                    modifier = Modifier.fillParentMaxSize(),
                )
            }
        }

        items(templates, key = { template -> template.id }) { template ->
            TemplateItem(
                title = template.title,
                description = template.description,
                items = template.items.map { item -> item.name },
                author = template.author,
                takenCount = template.takenCount,
                hashtags = template.hashtags,
                onClickHashtag = onClickHashtag,
                modifier =
                    Modifier
                        .animateItem()
                        .noRippleClickable { onClickDetail(template.id) },
                iconButton = {
                    TemplateItemIconButtonByTemplateItemType(
                        type = type,
                        template = template,
                        onClickDelete = onClickDelete,
                        onClickBookmark = onClickBookmark,
                    )
                },
            )
        }

        if (showLoadingBlock) item { LoadingBlock() }
    }
}

@Composable
private fun TemplateItemIconButtonByTemplateItemType(
    type: TemplateItemType,
    template: BottariTemplateUiModel,
    onClickDelete: (Long) -> Unit,
    onClickBookmark: (Long) -> Unit,
) {
    when (type) {
        is TemplateItemType.MyTemplate -> {
            TemplateItemIconButton(
                type = type,
                onClick = { onClickDelete(template.id) },
            )
        }

        is TemplateItemType.Bookmark -> {
            TemplateItemIconButton(
                type = type.copy(isBookmarked = template.isMarked),
                onClick = { onClickBookmark(template.id) },
            )
        }
    }
}

@Composable
private fun LoadingBlock(modifier: Modifier = Modifier) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = BottariTheme.spacing.space2xLarge),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            modifier = Modifier,
            color = BottariTheme.colors.primary,
            trackColor = BottariTheme.colors.primarySoft,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoadingBlockPreview() {
    BottariTheme {
        LoadingBlock()
    }
}

@Preview(showBackground = true)
@Composable
private fun TemplateColumnPreview() {
    val items =
        List(10) { item ->
            BottariTemplateItemUiModel(
                id = item.toLong(),
                name = "아이템 $item",
            )
        }
    val templates =
        List(3) { index ->
            BottariTemplateUiModel(
                id = index.toLong(),
                title = "우테코출근보따리글자수열다섯자 $index",
                description = "우테코출근보따리글자수열다섯자 $index",
                author = "다이스",
                takenCount = 100_024 + index,
                items = items,
                hashtags = List(3) { BottariTemplateHashtagUiModel(it.toLong(), "해시태그 $it") },
            )
        }
    BottariTheme {
        TemplateColumn(
            type = TemplateItemType.Bookmark(false),
            templates = templates,
            listState = LazyListState(),
            emptyViewText = "",
            onClickDetail = {},
            onClickDelete = {},
            onClickBookmark = {},
            onClickHashtag = {},
            showLoadingBlock = false,
        )
    }
}
