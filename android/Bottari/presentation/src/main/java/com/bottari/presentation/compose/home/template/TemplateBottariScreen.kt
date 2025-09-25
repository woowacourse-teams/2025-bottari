package com.bottari.presentation.compose.home.template

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.component.BottariSearchBar
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
    modifier: Modifier = Modifier,
    viewModel: TemplateViewModel = viewModel(factory = TemplateViewModel.Factory()),
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val uiState = viewModel.uiState.observeAsState().value ?: return
    val uiEvent = viewModel.uiEvent.observeAsState().value
    val listState = rememberLazyListState()

    LaunchedEffect(uiEvent) {
        if (uiEvent == null) return@LaunchedEffect

        when (uiEvent) {
            is TemplateUiEvent.SearchTemplateSuccess -> listState.scrollToItem(0)
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
        listState = listState,
        onClickDetail = navigateToTemplateDetail,
        onQueryChange = viewModel::updateSearchWord,
        onLoadNextPage = viewModel::fetchTemplates,
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
    onClickDetail: (Long) -> Unit,
    onQueryChange: (String) -> Unit,
    onLoadNextPage: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isScrolledToEnd by listState.rememberScrolledToEnd(5)

    LaunchedEffect(Unit) {
        snapshotFlow { isScrolledToEnd }
            .distinctUntilChanged()
            .filter { it }
            .collect { onLoadNextPage() }
    }

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        BottariSearchBar(
            query = uiState.searchWord,
            onQueryChange = onQueryChange,
            placeholderText = "검색어를 입력하세요",
            onSearch = {},
            modifier =
                Modifier.padding(
                    vertical = BottariTheme.spacing.spaceSmall,
                    horizontal = BottariTheme.spacing.spaceLarge,
                ),
        )
        LazyColumn(
            state = listState,
            modifier =
                Modifier
                    .padding(horizontal = BottariTheme.spacing.spaceLarge)
                    .topBottomFadingEdge(color = BottariTheme.colors.gray50, width = 8.dp),
            contentPadding = PaddingValues(vertical = BottariTheme.spacing.spaceSmall),
            verticalArrangement = Arrangement.spacedBy(BottariTheme.spacing.spaceSmall),
        ) {
            items(uiState.templates, key = { template -> template.id }) { template ->
                TemplateItem(
                    template = template,
                    modifier = Modifier.noRippleClickable { onClickDetail(template.id) },
                )
            }
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
            listState = rememberLazyListState(),
            onClickDetail = {},
            onQueryChange = {},
            onLoadNextPage = {},
        )
    }
}
