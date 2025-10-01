package com.bottari.presentation.compose.common.modifier

import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.TabPosition
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.lerp
import kotlin.math.abs

fun Modifier.pagerTabIndicatorOffset(
    pagerState: PagerState,
    tabPositions: List<TabPosition>,
): Modifier =
    this.layout { measurable, constraints ->
        val fraction = abs(pagerState.currentPageOffsetFraction)
        val currentTab = tabPositions.getOrNull(pagerState.currentPage)
        val targetTab = tabPositions.getOrNull(pagerState.targetPage)

        if (currentTab != null && targetTab != null) {
            val indicatorWidth = lerp(currentTab.width, targetTab.width, fraction)
            val indicatorStart = lerp(currentTab.left, targetTab.left, fraction)

            val placeable =
                measurable.measure(
                    constraints.copy(
                        minWidth = indicatorWidth.roundToPx(),
                        maxWidth = indicatorWidth.roundToPx(),
                    ),
                )
            layout(constraints.maxWidth, placeable.height) {
                placeable.placeRelative(indicatorStart.roundToPx(), 0)
            }
        } else {
            layout(0, 0) {}
        }
    }
