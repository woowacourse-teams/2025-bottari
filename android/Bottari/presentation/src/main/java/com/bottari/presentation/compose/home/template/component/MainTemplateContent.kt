package com.bottari.presentation.compose.home.template.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bottari.logger.BottariLogger
import com.bottari.presentation.compose.common.component.BottariHashChipSearchBar
import com.bottari.presentation.compose.common.theme.BottariTheme
import com.bottari.presentation.model.template.BottariTemplateHashtagUiModel
import com.bottari.presentation.model.template.BottariTemplateUiModel

@Composable
fun MainTemplateContent(
    templates: List<BottariTemplateUiModel>,
    listState: LazyListState,
    query: String,
    onQueryChange: (String) -> Unit,
    chips: List<BottariTemplateHashtagUiModel>,
    onChipsChange: (List<BottariTemplateHashtagUiModel>) -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
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
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            onClickDetail = onClickDetail,
            onClickBookmark = onClickBookmark,
            onClickDelete = {},
            onClickHashtag = { hashtag -> onChipsChange(listOf(hashtag)) },
        )
    }
}
