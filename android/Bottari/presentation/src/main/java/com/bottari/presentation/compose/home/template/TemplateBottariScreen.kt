package com.bottari.presentation.compose.home.template

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.component.BottariSearchBar
import com.bottari.presentation.compose.common.component.BottariTabBar
import com.bottari.presentation.compose.common.extension.rememberScrolledToEnd
import com.bottari.presentation.compose.common.modifier.noRippleClickable
import com.bottari.presentation.compose.common.modifier.topBottomFadingEdge
import com.bottari.presentation.compose.common.theme.BottariTheme
import com.bottari.presentation.compose.home.template.component.TemplateItem
import com.bottari.presentation.model.template.BottariTemplateItemUiModel
import com.bottari.presentation.model.template.BottariTemplateUiModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@Composable
fun TemplateBottariScreen(
    navigateToTemplateDetail: (Long) -> Unit,
    navigateToTemplateCreate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TemplateViewModel = viewModel(factory = TemplateViewModel.Factory()),
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val uiState = viewModel.uiState.observeAsState().value ?: return
    val uiEvent = viewModel.uiEvent.observeAsState().value
    val mainListState = rememberLazyListState()
    val myListState = rememberLazyListState()

    LaunchedEffect(uiEvent) {
        if (uiEvent == null) return@LaunchedEffect

        when (uiEvent) {
            is TemplateUiEvent.SearchTemplateSuccess -> mainListState.scrollToItem(0)
            is TemplateUiEvent.FetchBottariTemplatesFailure ->
                Toast
                    .makeText(
                        context,
                        R.string.template_fetch_template_failure_text,
                        Toast.LENGTH_SHORT,
                    ).show()
        }
    }

    TemplateBottariScreen(
        uiState = uiState,
        listState = mainListState,
        myListState = myListState,
        onClickDetail = navigateToTemplateDetail,
        onQueryChange = viewModel::updateSearchWord,
        onLoadNextPage = viewModel::fetchTemplates,
        onClickAdd = navigateToTemplateCreate,
        modifier =
            modifier.noRippleClickable {
                focusManager.clearFocus()
            },
    )
}

@Composable
private fun TemplateBottariScreen(
    uiState: TemplateUiState,
    listState: LazyListState,
    myListState: LazyListState,
    onClickDetail: (Long) -> Unit,
    onQueryChange: (String) -> Unit,
    onLoadNextPage: () -> Unit,
    onClickAdd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isScrolledToEnd by listState.rememberScrolledToEnd(5)
    val fabAlpha by animateFloatAsState(
        targetValue = if (listState.isScrollInProgress) 0f else 1f,
        animationSpec = tween(durationMillis = 300),
    )

    LaunchedEffect(Unit) {
        snapshotFlow { isScrolledToEnd }
            .distinctUntilChanged()
            .filter { it }
            .collect { onLoadNextPage() }
    }

    Box(modifier = modifier) {
        TemplatePager(
            pageTitles = listOf("전체 템플릿", "나의 템플릿"),
            modifier = Modifier,
        ) { page ->
            when (page) {
                0 ->
                    AllTemplateContent(
                        templates = uiState.templates,
                        listState = listState,
                        query = uiState.searchWord,
                        onQueryChange = onQueryChange,
                        onClickDetail = onClickDetail,
                    )

                1 ->
                    TemplateLazyColumn(
                        templates = uiState.myTemplates,
                        listState = myListState,
                        onClickDetail = onClickDetail,
                    )
            }
        }

        FloatingActionButton(
            onClick = onClickAdd,
            shape = RoundedCornerShape(12.dp),
            containerColor = BottariTheme.colors.primary,
            contentColor = BottariTheme.colors.white,
            elevation =
                FloatingActionButtonDefaults.elevation(
                    defaultElevation = 2.dp,
                    pressedElevation = 0.dp,
                    hoveredElevation = 0.dp,
                    focusedElevation = 0.dp,
                ),
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .alpha(fabAlpha),
        ) {
            Box(
                modifier = Modifier.padding(horizontal = 8.dp),
            ) {
                Text(
                    text = "보따리 등록하기",
                    style = BottariTheme.typography.semiBold16.toTextStyle(),
                )
            }
        }
    }
}

@Composable
private fun AllTemplateContent(
    templates: List<BottariTemplateUiModel>,
    listState: LazyListState,
    query: String,
    onQueryChange: (String) -> Unit,
    onClickDetail: (Long) -> Unit,
) {
    Column {
        BottariSearchBar(
            query = query,
            onQueryChange = onQueryChange,
            placeholderText = "검색어를 입력하세요",
            onSearch = {},
            modifier =
                Modifier.padding(
                    vertical = BottariTheme.spacing.spaceSmall,
                    horizontal = BottariTheme.spacing.spaceLarge,
                ),
        )

        TemplateLazyColumn(
            templates = templates,
            listState = listState,
            onClickDetail = onClickDetail,
        )
    }
}

@Composable
private fun TemplateLazyColumn(
    templates: List<BottariTemplateUiModel>,
    listState: LazyListState,
    onClickDetail: (Long) -> Unit,
) {
    LazyColumn(
        state = listState,
        modifier =
            Modifier
                .fillMaxSize()
                .padding(horizontal = BottariTheme.spacing.spaceLarge)
                .topBottomFadingEdge(color = BottariTheme.colors.gray50, width = 8.dp),
        contentPadding = PaddingValues(vertical = BottariTheme.spacing.spaceSmall),
        verticalArrangement = Arrangement.spacedBy(BottariTheme.spacing.spaceSmall),
    ) {
        templates.ifEmpty {
            item {
                TemplateMyEmptyView(
                    text = "항목이 존재하지 않습니다",
                    modifier = Modifier.fillParentMaxSize(),
                )
            }
        }

        items(templates, key = { template -> template.id }) { template ->
            TemplateItem(
                template = template,
                modifier = Modifier.noRippleClickable { onClickDetail(template.id) },
            )
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

@Composable
fun TemplateMyEmptyView(
    text: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_bottari),
            contentDescription = text,
            tint = BottariTheme.colors.gray500,
            modifier = Modifier.size(80.dp),
        )

        Spacer(modifier = Modifier.height(BottariTheme.spacing.spaceSmall))

        Text(
            text = text,
            style = BottariTheme.typography.semiBold16.toTextStyle(),
            color = BottariTheme.colors.gray500,
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
                author = "다이스",
                takenCount = 100_024 + index,
                items = items,
            )
        }

    BottariTheme {
        TemplateBottariScreen(
            uiState = TemplateUiState(templates = templates),
            listState = rememberLazyListState(),
            myListState = rememberLazyListState(),
            onClickDetail = {},
            onQueryChange = {},
            onLoadNextPage = {},
            onClickAdd = {},
        )
    }
}
