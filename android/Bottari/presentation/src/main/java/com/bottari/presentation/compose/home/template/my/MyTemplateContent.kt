package com.bottari.presentation.compose.home.template.my

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bottari.presentation.compose.home.template.component.PullToRefreshTemplateColumn
import com.bottari.presentation.compose.home.template.component.TemplateItemType
import com.bottari.presentation.model.template.BottariTemplateUiModel

@Composable
fun MyTemplateContent(
    myTemplates: List<BottariTemplateUiModel>,
    listState: LazyListState,
    emptyViewText: String,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    showLoadingBlock: Boolean,
    onClickDetail: (Long) -> Unit,
    onClickDelete: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    PullToRefreshTemplateColumn(
        type = TemplateItemType.MyTemplate,
        templates = myTemplates,
        listState = listState,
        emptyViewText = emptyViewText,
        showLoadingBlock = showLoadingBlock,
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        onClickDetail = onClickDetail,
        onClickDelete = onClickDelete,
        onClickBookmark = {},
        onClickHashtag = {},
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
private fun MyTemplateContentPreview() {
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

    MyTemplateContent(
        myTemplates = myTemplates,
        listState = rememberLazyListState(),
        emptyViewText = "아직 공유된 보따리가 없어요",
        isRefreshing = false,
        onRefresh = {},
        showLoadingBlock = false,
        onClickDetail = {},
        onClickDelete = {},
    )
}
