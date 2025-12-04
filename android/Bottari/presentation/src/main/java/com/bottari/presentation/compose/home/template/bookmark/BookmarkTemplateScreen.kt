package com.bottari.presentation.compose.home.template.bookmark

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bottari.core.designsystem.theme.BottariTheme
import com.bottari.core.ui.modifier.noRippleClickable
import com.bottari.core.ui.modifier.topBottomFadingEdge
import com.bottari.presentation.compose.home.template.component.TemplateEmptyView
import com.bottari.presentation.compose.home.template.component.TemplateItem
import com.bottari.presentation.compose.home.template.component.TemplateItemIconButton
import com.bottari.presentation.compose.home.template.component.TemplateItemType
import com.bottari.presentation.model.template.BookmarkedTemplateUiModel
import com.bottari.presentation.model.template.BottariTemplateHashtagUiModel

@Composable
fun BookmarkTemplateScreen(
    snackbarHostState: SnackbarHostState,
    navigateToDetail: (templateId: Long) -> Unit,
    viewModel: BookmarkTemplateViewModel = viewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is BookmarkTemplateEvent.FetchBookmarkTemplateFailure ->
                    snackbarHostState.showSnackbar("북마크한 보따리를 불러오지 못했어요")

                is BookmarkTemplateEvent.DeleteBookmarkTemplateFailure ->
                    snackbarHostState.showSnackbar("보따리의 북마크 해제에 실패했어요")
            }
        }
    }

    BookmarkTemplateScreen(
        uiState = uiState,
        onClickDetail = navigateToDetail,
        onClickDelete = viewModel::deleteBookmark,
    )
}

@Composable
private fun BookmarkTemplateScreen(
    uiState: BookmarkTemplateUiState,
    onClickDetail: (templateId: Long) -> Unit,
    onClickDelete: (templateId: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        if (uiState.isEmpty) {
            TemplateEmptyView(text = "북마크한 템플릿이 없어요")
            return@Box
        }

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
    onClickDetail: (templateId: Long) -> Unit,
    onClickDelete: (templateId: Long) -> Unit,
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
                hashtags =
                    template.hashtags.mapIndexed { index, hashtag ->
                        BottariTemplateHashtagUiModel(index.toLong(), hashtag)
                    },
                iconButton = {
                    TemplateItemIconButton(
                        type = TemplateItemType.Bookmark(true),
                        onClick = { onClickDelete(template.templateId) },
                    )
                },
                modifier =
                    Modifier
                        .animateItem()
                        .noRippleClickable { onClickDetail(template.templateId) },
                onClickHashtag = {},
                author = "",
                takenCount = -1,
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
