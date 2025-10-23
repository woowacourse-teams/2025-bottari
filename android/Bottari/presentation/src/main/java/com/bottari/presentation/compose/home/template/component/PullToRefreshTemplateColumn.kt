package com.bottari.presentation.compose.home.template.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.presentation.compose.common.theme.BottariTheme
import com.bottari.presentation.model.template.BottariTemplateHashtagUiModel
import com.bottari.presentation.model.template.BottariTemplateItemUiModel
import com.bottari.presentation.model.template.BottariTemplateUiModel

@Composable
fun PullToRefreshTemplateColumn(
    type: TemplateItemType,
    templates: List<BottariTemplateUiModel>,
    listState: LazyListState,
    emptyViewText: String,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onClickDetail: (Long) -> Unit,
    onClickBookmark: (Long) -> Unit,
    onClickDelete: (Long) -> Unit,
    onClickHashtag: (BottariTemplateHashtagUiModel) -> Unit,
    modifier: Modifier = Modifier,
    showLoadingBlock: Boolean,
) {
    val state = rememberPullToRefreshState()
    val animatedPadding by animateDpAsState(
        targetValue = (state.distanceFraction.coerceIn(0f, 1f) * 56.dp.value).dp,
        label = "TopDynamicPaddingAnimation",
    )

    PullToRefreshBox(
        state = state,
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier =
            modifier
                .fillMaxSize()
                .padding(top = animatedPadding),
        indicator = {
            Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = isRefreshing,
                state = state,
                containerColor = BottariTheme.colors.primarySoft,
                color = BottariTheme.colors.primary,
            )
        },
    ) {
        TemplateColumn(
            type = type,
            templates = templates,
            emptyViewText = emptyViewText,
            listState = listState,
            showLoadingBlock = showLoadingBlock,
            onClickDetail = onClickDetail,
            onClickDelete = onClickDelete,
            onClickBookmark = onClickBookmark,
            onClickHashtag = onClickHashtag,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PullToRefreshTemplateColumnPreview() {
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
        PullToRefreshTemplateColumn(
            type = TemplateItemType.Bookmark(false),
            templates = templates,
            listState = LazyListState(),
            emptyViewText = "결과가 존재하지 않아요",
            isRefreshing = false,
            onRefresh = {},
            onClickDetail = {},
            onClickDelete = {},
            onClickBookmark = {},
            onClickHashtag = {},
            showLoadingBlock = false,
        )
    }
}
