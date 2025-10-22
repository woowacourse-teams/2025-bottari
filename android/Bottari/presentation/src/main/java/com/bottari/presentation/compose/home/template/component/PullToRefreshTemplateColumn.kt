package com.bottari.presentation.compose.home.template.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bottari.presentation.compose.common.theme.BottariTheme
import com.bottari.presentation.model.template.BottariTemplateHashtagUiModel
import com.bottari.presentation.model.template.BottariTemplateUiModel

@Composable
fun PullToRefreshTemplateColumn(
    type: TemplateItemType,
    templates: List<BottariTemplateUiModel>,
    listState: LazyListState,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onClickDetail: (Long) -> Unit,
    onClickBookmark: (Long) -> Unit,
    onClickDelete: (Long) -> Unit,
    onClickHashtag: (BottariTemplateHashtagUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = rememberPullToRefreshState()
    val animatedPadding by animateDpAsState(
        targetValue = (state.distanceFraction.coerceIn(0f, 1f) * 56.dp.value).dp,
        label = "TopDynamicPaddingAnimation",
    )

    PullToRefreshBox(
        state = state,
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier.fillMaxSize().padding(top = animatedPadding),
        indicator = {
            Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = isRefreshing,
                state = state,
                containerColor = BottariTheme.colors.primarySoft,
                color = BottariTheme.colors.primary,
            )
        },
    ) {
        TemplateColumn(
            type = type,
            templates = templates,
            listState = listState,
            onClickDetail = onClickDetail,
            onClickDelete = onClickDelete,
            onClickBookmark = onClickBookmark,
            onClickHashtag = onClickHashtag,
        )
    }
}
