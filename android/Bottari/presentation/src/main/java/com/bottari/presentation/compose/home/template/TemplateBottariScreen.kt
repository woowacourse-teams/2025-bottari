package com.bottari.presentation.compose.home.template

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.component.BottariTabBar
import com.bottari.presentation.compose.common.extension.rememberScrolledToEnd
import com.bottari.presentation.compose.common.modifier.noRippleClickable
import com.bottari.presentation.compose.common.theme.BottariTheme
import com.bottari.presentation.compose.home.template.bookmark.BookmarkTemplateScreen
import com.bottari.presentation.compose.home.template.component.CreateTemplateFAB
import com.bottari.presentation.compose.home.template.main.MainTemplateContent
import com.bottari.presentation.compose.home.template.my.MyTemplateContent
import com.bottari.presentation.model.template.BottariTemplateHashtagUiModel
import com.bottari.presentation.model.template.BottariTemplateItemUiModel
import com.bottari.presentation.model.template.BottariTemplateUiModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@Composable
fun TemplateBottariScreen(
    snackbarState: SnackbarHostState,
    navigateToTemplateDetail: (templateId: Long, isMyTemplate: Boolean) -> Unit,
    navigateToTemplateCreate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TemplateViewModel = viewModel(),
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val uiEvent = viewModel.uiEvent.collectAsStateWithLifecycle(null)
    val mainListState = rememberLazyListState()
    val myListState = rememberLazyListState()

    LaunchedEffect(uiEvent.value) {
        when (uiEvent.value ?: return@LaunchedEffect) {
            is TemplateUiEvent.SearchTemplateSuccess -> mainListState.scrollToItem(0)
            is TemplateUiEvent.MainTemplatesRefreshFinished -> mainListState.scrollToItem(0)
            is TemplateUiEvent.MyTemplatesRefreshFinished -> myListState.scrollToItem(0)

            is TemplateUiEvent.FetchBottariTemplatesFailure ->
                snackbarState.showSnackbar(context.getString(R.string.template_fetch_template_failure_text))

            is TemplateUiEvent.DeleteBottariTemplateSuccess ->
                snackbarState.showSnackbar(context.getString(R.string.template_my_template_delete_success_text))

            is TemplateUiEvent.DeleteBottariTemplateFailure ->
                snackbarState.showSnackbar(context.getString(R.string.template_my_template_delete_failure_text))
        }
    }

    TemplateBottariScreen(
        uiState = uiState.value,
        listState = mainListState,
        myListState = myListState,
        onClickDetail = navigateToTemplateDetail,
        onQueryChange = viewModel::updateSearchWord,
        onChipChange = viewModel::searchByChip,
        onLoadNextPage = viewModel::loadNextPage,
        onClickAdd = navigateToTemplateCreate,
        onClickDelete = viewModel::deleteTemplate,
        onClickBookmark = viewModel::toggleBookmark,
        onRefresh = viewModel::refresh,
        modifier = modifier.noRippleClickable { focusManager.clearFocus() },
    )
}

@Composable
private fun TemplateBottariScreen(
    uiState: TemplateUiState,
    listState: LazyListState,
    myListState: LazyListState,
    onClickDetail: (templateId: Long, isMyTemplate: Boolean) -> Unit,
    onQueryChange: (String) -> Unit,
    onChipChange: (List<BottariTemplateHashtagUiModel>) -> Unit,
    onLoadNextPage: () -> Unit,
    onClickAdd: () -> Unit,
    onClickDelete: (Long) -> Unit,
    onClickBookmark: (Long) -> Unit,
    onRefresh: (targetIsMain: Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isScrolledToEnd by listState.rememberScrolledToEnd(5)
    var isFabExpanded by remember { mutableStateOf(false) }
    var isFabVisible by remember { mutableStateOf(true) }

    LaunchedEffect(listState.isScrollInProgress, myListState.isScrollInProgress) {
        if (listState.isScrollInProgress || myListState.isScrollInProgress) {
            isFabVisible = false
            isFabExpanded = false
            return@LaunchedEffect
        }
        delay(400)
        isFabVisible = true
    }

    LaunchedEffect(Unit) {
        snapshotFlow { isScrolledToEnd }
            .distinctUntilChanged()
            .filter { it }
            .collect { onLoadNextPage() }
    }

    Box(modifier = modifier.fillMaxSize()) {
        TemplatePager(
            pageTitles = listOf("전체 템플릿", "나의 템플릿", "북마크"),
            modifier = Modifier,
        ) { page ->
            when (page) {
                0 ->
                    MainTemplateContent(
                        templates = uiState.templates,
                        listState = listState,
                        emptyViewText = if (uiState.isMainTemplatesEmpty) "아직 공유된 보따리가 없어요" else "",
                        query = uiState.searchWord,
                        onQueryChange = onQueryChange,
                        chips = uiState.chips,
                        onChipsChange = onChipChange,
                        onClickDetail = { id ->
                            uiState.myTemplates
                                .any { template -> template.id == id }
                                .let { isMyTemplate -> onClickDetail(id, isMyTemplate) }
                        },
                        onClickBookmark = onClickBookmark,
                        isRefreshing = uiState.isRefreshingMain,
                        onRefresh = { onRefresh(true) },
                        showLoadingBlock = uiState.showLoading,
                    )

                1 ->
                    MyTemplateContent(
                        myTemplates = uiState.myTemplates,
                        listState = myListState,
                        emptyViewText = if (uiState.isMyTemplatesEmpty) "아직 공유한 보따리가 없어요" else "",
                        onClickDetail = { id -> onClickDetail(id, true) },
                        onClickDelete = onClickDelete,
                        isRefreshing = uiState.isRefreshingMy,
                        onRefresh = { onRefresh(false) },
                        showLoadingBlock = uiState.showLoading,
                    )

                2 ->
                    BookmarkTemplateScreen(
                        navigateToDetail = { id -> onClickDetail(id, true) },
                    )
            }
        }

        AnimatedVisibility(
            visible = isFabVisible,
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(BottariTheme.spacing.spaceMedium),
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            CreateTemplateFAB(onClickAdd)
        }
    }
}

@Composable
private fun TemplatePager(
    pageTitles: List<String>,
    modifier: Modifier = Modifier,
    screen: @Composable (Int) -> Unit,
) {
    val pagerState: PagerState = rememberPagerState(initialPage = 0) { pageTitles.size }

    Column {
        BottariTabBar(
            pageTitles = pageTitles,
            pagerState = pagerState,
            modifier = modifier,
            screen = screen,
        )
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
                description = "우테코출근보따리글자수열다섯자 $index",
                author = "다이스",
                takenCount = 100_024 + index,
                items = items,
                isMarked = index % 2 == 0,
                hashtags = List(3) { BottariTemplateHashtagUiModel(it.toLong(), "해시태그 $it") },
            )
        }

    BottariTheme {
        TemplateBottariScreen(
            uiState = TemplateUiState(templates = templates),
            listState = rememberLazyListState(),
            myListState = rememberLazyListState(),
            onClickDetail = { _, _ -> },
            onQueryChange = {},
            onChipChange = {},
            onLoadNextPage = {},
            onClickDelete = {},
            onClickBookmark = {},
            onRefresh = {},
            onClickAdd = {},
        )
    }
}
