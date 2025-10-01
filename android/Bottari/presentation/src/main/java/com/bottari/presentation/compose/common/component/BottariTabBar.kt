package com.bottari.presentation.compose.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.zIndex
import com.bottari.presentation.compose.common.modifier.dropShadow
import com.bottari.presentation.compose.common.source.NoRippleInteractionSource
import com.bottari.presentation.compose.common.theme.BottariTheme
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun BottariTabBar(
    pageTitles: List<String>,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    screen: @Composable (Int) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val interactionSource = remember { NoRippleInteractionSource() }
    val indicator = @Composable { tabPositions: List<TabPosition> ->
        BottariIndicator(
            tabPositions = tabPositions,
            fraction = pagerState.currentPageOffsetFraction,
            currentPage = pagerState.currentPage,
        )
    }

    TabRow(
        modifier =
            modifier
                .height(60.dp)
                .padding(horizontal = 16.dp)
                .clip(CircleShape),
        selectedTabIndex = pagerState.currentPage,
        indicator = indicator,
        divider = {},
        containerColor = Color(0xFFF2F2F5),
    ) {
        pageTitles.forEachIndexed { index, title ->
            BottariTap(
                title = title,
                isSelected = pagerState.currentPage == index,
                onTapClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
                interactionSource = interactionSource,
            )
        }
    }

    HorizontalPager(pagerState, modifier = Modifier.fillMaxSize()) { page ->
        screen(page)
    }
}

@Composable
private fun BottariTap(
    title: String,
    isSelected: Boolean,
    onTapClick: () -> Unit,
    interactionSource: MutableInteractionSource,
    modifier: Modifier = Modifier,
) {
    Tab(
        modifier = modifier.zIndex(2f),
        text = {
            val textColor = if (isSelected) Color.Black else BottariTheme.colors.gray700
            Text(
                text = title,
                color = textColor,
                style = BottariTheme.typography.medium16.toTextStyle(),
            )
        },
        selected = isSelected,
        onClick = onTapClick,
        interactionSource = interactionSource,
    )
}

@Composable
private fun BottariIndicator(
    tabPositions: List<TabPosition>,
    fraction: Float,
    currentPage: Int,
) {
    val currentTab = tabPositions[currentPage]

    val targetPage =
        when {
            fraction > 0 -> currentPage + 1
            fraction < 0 -> currentPage - 1
            else -> currentPage
        }.coerceIn(0, tabPositions.lastIndex)

    val targetTab = tabPositions[targetPage]

    val animationFraction = abs(fraction)

    val indicatorStart = lerp(currentTab.left, targetTab.left, animationFraction)
    val indicatorWidth = lerp(currentTab.width, targetTab.width, animationFraction)

    Box(
        modifier =
            Modifier
                .offset(x = indicatorStart)
                .wrapContentSize(align = Alignment.BottomStart)
                .width(indicatorWidth)
                .fillMaxSize()
                .padding(horizontal = 4.dp)
                .dropShadow(
                    CircleShape,
                    color = Color.Black.copy(0.05f),
                    blur = 2.dp,
                    offsetY = 1.dp,
                ).background(color = Color.White, CircleShape)
                .zIndex(1f),
    )
}

@Composable
private fun BottariPagerScreen(
    pageTitles: List<String>,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState(initialPage = 0) { pageTitles.size }

    Column(modifier = modifier.fillMaxSize()) {
        BottariTabBar(
            pageTitles = pageTitles,
            pagerState = pagerState,
            modifier = Modifier.fillMaxWidth(),
        ) { page ->
            when (page) {
                0 -> Text("첫 번째 페이지 (공통)")
                1 -> Text("두 번째 페이지 (개인)")
                2 -> Text("세 번째 페이지 (팀)")
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .weight(1f),
        ) { page ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                when (page) {
                    0 -> Text("첫 번째 페이지 (공통)")
                    1 -> Text("두 번째 페이지 (개인)")
                    2 -> Text("세 번째 페이지 (팀)")
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun BottariPagerScreenPreview() {
    val pageTitles = listOf("공통", "개인", "팀")
    BottariTheme {
        Box(modifier = Modifier.padding(12.dp)) {
            BottariPagerScreen(pageTitles = pageTitles)
        }
    }
}
