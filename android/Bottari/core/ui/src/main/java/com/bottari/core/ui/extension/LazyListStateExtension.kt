package com.bottari.core.ui.extension

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember

/**
 * 리스트가 끝(threshold 앞)까지 스크롤되었는지 여부를 나타내는 State
 */
@Composable
fun LazyListState.rememberScrolledToEnd(threshold: Int = 1) =
    remember(this) {
        derivedStateOf {
            val lastVisible = layoutInfo.visibleItemsInfo.lastOrNull()?.index
            val lastIndex = layoutInfo.totalItemsCount - 1
            lastVisible != null && lastVisible >= (lastIndex - threshold)
        }
    }
