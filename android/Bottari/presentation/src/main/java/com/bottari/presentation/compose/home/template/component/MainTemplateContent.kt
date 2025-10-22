package com.bottari.presentation.compose.home.template.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bottari.presentation.compose.common.component.BottariHashChipSearchBar
import com.bottari.presentation.compose.common.theme.BottariTheme
import com.bottari.presentation.model.template.BottariTemplateHashtagUiModel
import com.bottari.presentation.model.template.BottariTemplateUiModel

@Composable
fun MainTemplateContent(
    templates: List<BottariTemplateUiModel>,
    listState: LazyListState,
    emptyViewText: String,
    query: String,
    onQueryChange: (String) -> Unit,
    chips: List<BottariTemplateHashtagUiModel>,
    onChipsChange: (List<BottariTemplateHashtagUiModel>) -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    showLoadingBlock: Boolean,
    onClickDetail: (Long) -> Unit,
    onClickBookmark: (Long) -> Unit,
) {
    Column {
        BottariHashChipSearchBar(
            query = query,
            onQueryChange = onQueryChange,
            chips = chips.map { chip -> "#${chip.name}" },
            onChipsChange = { new -> if (new.isEmpty()) onChipsChange(emptyList()) },
            placeholderText = "제목이나 해시태그를 입력하세요",
            onSearch = {},
            modifier =
                Modifier
                    .padding(horizontal = BottariTheme.spacing.spaceLarge)
                    .padding(
                        top = BottariTheme.spacing.spaceXSmall,
                        bottom = BottariTheme.spacing.space2xSmall,
                    ),
        )

        PullToRefreshTemplateColumn(
            type = TemplateItemType.Bookmark(false),
            templates = templates,
            listState = listState,
            emptyViewText = emptyViewText,
            showLoadingBlock = showLoadingBlock,
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            onClickDetail = onClickDetail,
            onClickBookmark = onClickBookmark,
            onClickDelete = {},
            onClickHashtag = { hashtag -> onChipsChange(listOf(hashtag)) },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MainTemplateContentPreview() {
    val myTemplates =
        List(10) {
            BottariTemplateUiModel(
                id = it.toLong(),
                title = "보따리 템플릿 $it",
                description = "보따리 템플릿 설명 $it",
                items = emptyList(),
                author = "작성자 $it",
                takenCount = it * 10,
                hashtags = emptyList(),
            )
        }

    BottariTheme {
        MainTemplateContent(
            templates = myTemplates,
            listState = LazyListState(),
            emptyViewText = "아직 공유된 보따리가 없어요",
            query = "",
            onQueryChange = {},
            chips = emptyList(),
            onChipsChange = {},
            isRefreshing = false,
            onRefresh = {},
            showLoadingBlock = false,
            onClickDetail = {},
            onClickBookmark = {},
        )
    }
}
