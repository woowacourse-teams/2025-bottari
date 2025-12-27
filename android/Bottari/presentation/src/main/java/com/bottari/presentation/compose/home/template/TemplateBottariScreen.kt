package com.bottari.presentation.compose.home.template

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.core.ui.component.BottariTabBar
import com.bottari.presentation.compose.home.template.bookmark.BookmarkTemplateScreen
import com.bottari.presentation.compose.home.template.main.MainTemplateScreen
import com.bottari.presentation.compose.home.template.my.MyTemplateScreen

@Composable
fun TemplateBottariScreen(
    snackbarState: SnackbarHostState,
    navigateToTemplateDetail: (templateId: Long, isMyTemplate: Boolean, isBookmark: Boolean) -> Unit,
    navigateToTemplateCreate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TemplatePager(
        pageTitles = listOf("전체 템플릿", "나의 템플릿", "북마크"),
        modifier = modifier,
    ) { page ->
        when (page) {
            0 -> {
                MainTemplateScreen(
                    snackbarState = snackbarState,
                    onClickDetail = { id -> navigateToTemplateDetail(id, false, false) },
                )
            }

            1 -> {
                MyTemplateScreen(
                    snackbarState = snackbarState,
                    onClickDetail = { id -> navigateToTemplateDetail(id, true, false) },
                    onClickCreate = navigateToTemplateCreate,
                )
            }

            2 -> {
                BookmarkTemplateScreen(
                    snackbarHostState = snackbarState,
                    navigateToDetail = { id -> navigateToTemplateDetail(id, false, true) },
                )
            }
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
private fun TemplatePagerPreview() {
    BottariTheme {
        TemplatePager(pageTitles = listOf("전체 템플릿", "나의 템플릿", "북마크")) { }
    }
}
