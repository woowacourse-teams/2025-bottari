package com.bottari.presentation.compose.home.template.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bottari.presentation.compose.common.component.BottariHashChipSearchBar
import com.bottari.presentation.compose.common.extension.rememberScrolledToEnd
import com.bottari.presentation.compose.common.theme.BottariTheme
import com.bottari.presentation.compose.home.template.component.PullToRefreshTemplateColumn
import com.bottari.presentation.compose.home.template.component.TemplateItemType
import com.bottari.presentation.model.template.BottariTemplateHashtagUiModel
import com.bottari.presentation.model.template.BottariTemplateUiModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@Composable
fun MainTemplateScreen(
    snackbarState: SnackbarHostState,
    onClickDetail: (templateId: Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainTemplateViewModel = viewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val isScrolledToEnd by listState.rememberScrolledToEnd(5)

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is MainTemplateUiEvent.FetchBottariTemplatesFailure -> snackbarState.showSnackbar("템플릿을 가져오는데 실패 했어요")
                is MainTemplateUiEvent.SearchTemplateSuccess -> listState.animateScrollToItem(0)
                is MainTemplateUiEvent.AddBookmarkFailure -> snackbarState.showSnackbar("북마크에 실패 했어요")
                is MainTemplateUiEvent.DeleteBookmarkFailure -> snackbarState.showSnackbar("북마크 삭제에 실패 했어요")
            }
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { isScrolledToEnd }
            .distinctUntilChanged()
            .filter { it }
            .collect { viewModel.loadNextPage() }
    }

    MainTemplateScreen(
        uiState = uiState.value,
        listState = listState,
        onQueryChange = viewModel::updateSearchWord,
        onChipChange = viewModel::updateChip,
        onRefresh = viewModel::refresh,
        onClickDetail = onClickDetail,
        onClickBookmark = viewModel::toggleBookmark,
        modifier = modifier.fillMaxSize(),
    )
}

@Composable
private fun MainTemplateScreen(
    uiState: MainTemplateUiState,
    listState: LazyListState,
    onQueryChange: (String) -> Unit,
    onChipChange: (BottariTemplateHashtagUiModel?) -> Unit,
    onRefresh: () -> Unit,
    onClickDetail: (Long) -> Unit,
    onClickBookmark: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        BottariHashChipSearchBar(
            query = uiState.searchWord,
            onQueryChange = onQueryChange,
            chips = uiState.chip?.let { listOf(it.name) }.orEmpty(),
            onChipsChange = { new -> if (new.isEmpty()) onChipChange(null) },
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
            templates = uiState.templates,
            listState = listState,
            emptyViewText = if (uiState.isEmpty) "아직 공유된 보따리가 없어요" else "",
            showLoadingBlock = uiState.showLoading,
            isRefreshing = uiState.isRefreshing,
            onRefresh = onRefresh,
            onClickDetail = onClickDetail,
            onClickBookmark = onClickBookmark,
            onClickHashtag = onChipChange,
            onClickDelete = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MainTemplateScreenPreview() {
    val uiState =
        MainTemplateUiState(
            templates =
                List(10) {
                    BottariTemplateUiModel(
                        id = it.toLong(),
                        title = "우테코출근보따리글자수열다섯",
                        description = "우테코출근보따리글자수열다섯",
                        items = emptyList(),
                        author = "다이스",
                        takenCount = 100_024 + it,
                        hashtags =
                            List(3) {
                                BottariTemplateHashtagUiModel(
                                    it.toLong(),
                                    "해시태그 $it",
                                )
                            },
                        isMarked = it % 2 == 0,
                    )
                },
        )

    BottariTheme {
        MainTemplateScreen(
            uiState = uiState,
            listState = rememberLazyListState(),
            onQueryChange = {},
            onChipChange = {},
            onRefresh = {},
            onClickDetail = {},
            onClickBookmark = {},
        )
    }
}
