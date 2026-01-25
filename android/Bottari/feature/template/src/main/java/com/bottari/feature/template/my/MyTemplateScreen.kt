package com.bottari.feature.template.my

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.core.ui.model.template.BottariTemplateHashtagUiModel
import com.bottari.core.ui.model.template.BottariTemplateUiModel
import com.bottari.feature.template.component.CreateTemplateFAB
import com.bottari.feature.template.component.PullToRefreshTemplateColumn
import com.bottari.feature.template.component.TemplateItemType
import kotlinx.coroutines.delay

@Composable
fun MyTemplateScreen(
    snackbarState: SnackbarHostState,
    onClickDetail: (Long) -> Unit,
    onClickCreate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MyTemplateViewModel = hiltViewModel(),
) {
    val listState = rememberLazyListState()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is MyTemplateUiEvent.FetchBottariTemplatesFailure -> snackbarState.showSnackbar("템플릿을 가져오는데 실패 했어요")
                is MyTemplateUiEvent.DeleteTemplateFailure -> snackbarState.showSnackbar("템플릿을 삭제하는데 실패 했어요")
            }
        }
    }

    MyTemplateScreen(
        uiState = uiState.value,
        listState = listState,
        onClickDetail = onClickDetail,
        onClickDelete = viewModel::deleteTemplate,
        onClickCreate = onClickCreate,
        onRefresh = viewModel::fetchMyTemplates,
        modifier = modifier,
    )
}

@Composable
private fun MyTemplateScreen(
    uiState: MyTemplateUiState,
    listState: LazyListState,
    onClickDetail: (Long) -> Unit,
    onClickDelete: (Long) -> Unit,
    onClickCreate: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isFabVisible by remember { mutableStateOf(true) }

    LaunchedEffect(listState.isScrollInProgress) {
        if (listState.isScrollInProgress) {
            isFabVisible = false
            return@LaunchedEffect
        }
        delay(300)
        isFabVisible = true
    }

    Box(modifier = modifier) {
        PullToRefreshTemplateColumn(
            type = TemplateItemType.MyTemplate,
            templates = uiState.templates,
            listState = listState,
            emptyViewText = "아직 공유한 보따리가 없어요",
            showLoadingBlock = uiState.showLoading,
            isRefreshing = uiState.isRefreshing,
            onRefresh = onRefresh,
            onClickDetail = onClickDetail,
            onClickDelete = onClickDelete,
            onClickBookmark = {},
            onClickHashtag = {},
            modifier = modifier,
        )

        AnimatedVisibility(
            visible = isFabVisible,
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(BottariTheme.spacing.spaceMedium),
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            CreateTemplateFAB(onClickCreate)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MyTemplateScreenPreview() {
    val uiState =
        MyTemplateUiState(
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

    MyTemplateScreen(
        uiState = uiState,
        listState = rememberLazyListState(),
        onClickDetail = {},
        onClickDelete = {},
        onClickCreate = {},
        onRefresh = {},
    )
}
