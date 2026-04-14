package com.bottari.feature.template.impl.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bottari.bottari.designsystem.component.BottariInputChip
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.core.ui.component.BottariHashChipSearchBar
import com.bottari.core.ui.component.OfflineContent
import com.bottari.core.ui.extension.rememberBlockParentAfterChild
import com.bottari.core.ui.extension.rememberScrolledToEnd
import com.bottari.core.ui.extension.startEndFadingEdge
import com.bottari.core.ui.model.template.BottariTemplateHashtagUiModel
import com.bottari.core.ui.model.template.BottariTemplateUiModel
import com.bottari.core.ui.provider.LocalNetworkManager
import com.bottari.feature.template.impl.component.PullToRefreshTemplateColumn
import com.bottari.feature.template.impl.component.TemplateItemType
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@Composable
fun MainTemplateScreen(
    snackbarState: SnackbarHostState,
    onClickDetail: (templateId: Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainTemplateViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val networkManager = LocalNetworkManager.current
    val isConnected = networkManager.isConnected.collectAsStateWithLifecycle().value
    var hasLaunched by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val isScrolledToEnd by listState.rememberScrolledToEnd(5)
    val isScrolledToTop by remember(listState) { derivedStateOf { listState.firstVisibleItemIndex == 0 } }

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

    LaunchedEffect(isConnected) {
        if (hasLaunched && isConnected) viewModel.refresh()
        hasLaunched = true
    }

    if (isConnected) {
        MainTemplateScreen(
            uiState = uiState,
            listState = listState,
            showSearchBar = isScrolledToTop,
            onQueryChange = viewModel::updateSearchWord,
            onChipChange = viewModel::updateChip,
            onRefresh = viewModel::refresh,
            onClickDetail = onClickDetail,
            onClickBookmark = viewModel::toggleBookmark,
            modifier = modifier.fillMaxSize(),
        )
    } else {
        OfflineContent(
            onRetryClick = viewModel::refresh,
            modifier = modifier.fillMaxSize(),
        )
    }
}

@Composable
private fun MainTemplateScreen(
    uiState: MainTemplateUiState,
    listState: LazyListState,
    showSearchBar: Boolean,
    onQueryChange: (String) -> Unit,
    onChipChange: (BottariTemplateHashtagUiModel?) -> Unit,
    onRefresh: () -> Unit,
    onClickDetail: (Long) -> Unit,
    onClickBookmark: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        MainTemplateHeader(
            searchWord = uiState.searchWord,
            chip = uiState.chip,
            popularHashtags = uiState.popularHashtags,
            isVisible = showSearchBar,
            onQueryChange = onQueryChange,
            onChipChange = onChipChange,
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

@Composable
private fun MainTemplateHeader(
    searchWord: String,
    chip: BottariTemplateHashtagUiModel?,
    popularHashtags: List<BottariTemplateHashtagUiModel>,
    isVisible: Boolean,
    onQueryChange: (String) -> Unit,
    onChipChange: (BottariTemplateHashtagUiModel?) -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = isVisible,
        modifier = modifier.fillMaxWidth(),
    ) {
        Column {
            BottariHashChipSearchBar(
                query = searchWord,
                onQueryChange = onQueryChange,
                chips = chip?.let { listOf(it.name) }.orEmpty(),
                onChipsChange = { new -> if (new.isEmpty()) onChipChange(null) },
                placeholderText = "제목을 입력하거나 해시태그를 눌러보세요",
                onSearch = {},
                modifier =
                    Modifier
                        .padding(horizontal = BottariTheme.spacing.spaceLarge)
                        .padding(top = BottariTheme.spacing.spaceSmall),
            )

            Spacer(modifier = Modifier.height(BottariTheme.spacing.spaceSmall))

            if (popularHashtags.isNotEmpty()) {
                PopularHashtagSection(
                    popularHashtags = popularHashtags,
                    chip = chip,
                    onChipChange = onChipChange,
                )
            }
        }
    }
}

@Composable
private fun PopularHashtagSection(
    popularHashtags: List<BottariTemplateHashtagUiModel>,
    chip: BottariTemplateHashtagUiModel?,
    onChipChange: (BottariTemplateHashtagUiModel?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = "# 인기 해시태그",
            style = BottariTheme.typography.semiBold16.toTextStyle(),
            color = BottariTheme.colors.black,
            modifier = Modifier.padding(horizontal = BottariTheme.spacing.spaceXLarge),
        )

        Spacer(modifier = Modifier.height(BottariTheme.spacing.spaceSmall))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(BottariTheme.spacing.spaceXSmall),
            modifier =
                Modifier
                    .startEndFadingEdge(color = BottariTheme.colors.white)
                    .nestedScroll(rememberBlockParentAfterChild()),
        ) {
            item { Spacer(modifier = Modifier.width(BottariTheme.spacing.spaceSmall)) }
            items(popularHashtags, key = { hashtag -> hashtag.id }) { hashtag ->
                BottariInputChip(
                    text = hashtag.name,
                    selected = chip?.id == hashtag.id,
                    modifier = Modifier.clickable { onChipChange(hashtag) },
                )
            }
            item { Spacer(modifier = Modifier.width(BottariTheme.spacing.spaceSmall)) }
        }

        Spacer(modifier = Modifier.height(BottariTheme.spacing.space2xSmall))
    }
}

@Preview(showBackground = true)
@Composable
private fun MainTemplateScreenPreview() {
    val uiState =
        MainTemplateUiState(
            popularHashtags = List(3) { BottariTemplateHashtagUiModel(it.toLong(), "해시태그 $it") },
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
            showSearchBar = true,
            onQueryChange = {},
            onChipChange = {},
            onRefresh = {},
            onClickDetail = {},
            onClickBookmark = {},
        )
    }
}
