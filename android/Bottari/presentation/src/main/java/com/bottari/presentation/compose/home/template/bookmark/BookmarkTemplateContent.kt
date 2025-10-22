package com.bottari.presentation.compose.home.template.bookmark

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bottari.presentation.compose.common.modifier.noRippleClickable
import com.bottari.presentation.compose.common.modifier.topBottomFadingEdge
import com.bottari.presentation.compose.common.theme.BottariTheme
import com.bottari.presentation.compose.home.template.component.TemplateItem
import com.bottari.presentation.compose.home.template.component.TemplateItemIconButton
import com.bottari.presentation.compose.home.template.component.TemplateItemType
import com.bottari.presentation.model.template.BookmarkedTemplateUiModel
import com.bottari.presentation.model.template.BottariTemplateHashtagUiModel

@Composable
fun BookmarkTemplateScreen(
    navigateToDetail: (bookmarkId: Long) -> Unit,
    viewModel: BookmarkTemplateViewModel = viewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val uiEvent = viewModel.uiEvent.collectAsStateWithLifecycle(null)

    LaunchedEffect(uiEvent) {
        when (uiEvent.value ?: return@LaunchedEffect) {
            is BookmarkTemplateEvent.FetchBookmarkTemplateSuccess -> {}
            is BookmarkTemplateEvent.FetchBookmarkTemplateFailure -> {}
            is BookmarkTemplateEvent.DeleteBookmarkTemplateSuccess -> {}
            is BookmarkTemplateEvent.DeleteBookmarkTemplateFailure -> {}
        }
    }

    BookmarkTemplateScreen(
        uiState = uiState,
        onClickDetail = navigateToDetail,
        onClickDelete = {},
    )
}

@Composable
private fun BookmarkTemplateScreen(
    uiState: BookmarkTemplateUiState,
    onClickDetail: (bookmarkId: Long) -> Unit,
    onClickDelete: (bookmarkId: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        BookmarkTemplateColumn(
            templates = uiState.templates,
            onClickDetail = onClickDetail,
            onClickDelete = onClickDelete,
        )
    }
}

@Composable
private fun BookmarkTemplateColumn(
    templates: List<BookmarkedTemplateUiModel>,
    onClickDetail: (bookmarkId: Long) -> Unit,
    onClickDelete: (bookmarkId: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        state = rememberLazyListState(),
        modifier =
            modifier
                .fillMaxSize()
                .padding(horizontal = BottariTheme.spacing.spaceLarge)
                .topBottomFadingEdge(color = BottariTheme.colors.gray50, width = 8.dp),
        contentPadding = PaddingValues(vertical = BottariTheme.spacing.spaceSmall),
        verticalArrangement = Arrangement.spacedBy(BottariTheme.spacing.spaceSmall),
    ) {
        items(templates, key = { template -> template.id }) { template ->
            TemplateItem(
                title = template.title,
                description = template.description,
                items = template.items,
                author = template.author,
                takenCount = 0,
                hashtags =
                    template.hashtags.mapIndexed { index, hashtag ->
                        BottariTemplateHashtagUiModel(index.toLong(), hashtag)
                    },
                onClickHashtag = {},
                modifier = Modifier.noRippleClickable { onClickDetail(template.id) },
                iconButton = {
                    TemplateItemIconButton(
                        type = TemplateItemType.Bookmark(true),
                        onClick = { onClickDelete(template.id) },
                    )
                },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BookmarkTemplateScreenPreview() {
    val templates =
        List(10) {
            BookmarkedTemplateUiModel(
                id = it.toLong(),
                templateId = it.toLong(),
                title = "우테코출근보따리글자수열다섯 $it",
                description = "우테코출근보따리글자수열다섯 $it",
                items = listOf("아이템 1", "아이템 2", "아이템 3"),
                hashtags = listOf("해시태그1", "해시태그2", "해시태그3"),
                author = "",
            )
        }
    BottariTheme {
        BookmarkTemplateScreen(
            uiState = BookmarkTemplateUiState(templates = templates),
            onClickDetail = {},
            onClickDelete = {},
        )
    }
}
