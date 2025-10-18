package com.bottari.presentation.compose.personal.checklist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.component.BottariBox
import com.bottari.presentation.compose.common.theme.BottariTheme
import com.bottari.presentation.model.bottari.ChecklistItemUiModel
import com.spartapps.swipeablecards.state.SwipeableCardsState
import com.spartapps.swipeablecards.ui.SwipeableCardDirection
import com.spartapps.swipeablecards.ui.SwipeableCardsProperties
import com.spartapps.swipeablecards.ui.lazy.LazySwipeableCards
import com.spartapps.swipeablecards.ui.lazy.items

@Composable
fun CardStackScreen(
    modifier: Modifier = Modifier,
    state: SwipeableCardsState,
    items: List<ChecklistItemUiModel>,
    onLeftSwipe: (Long) -> Unit,
    onRightSwipe: (Long) -> Unit,
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        LazySwipeableCards(
            state = state,
            onSwipe = { item, direction ->
                when (direction) {
                    SwipeableCardDirection.Right -> {
                        onRightSwipe(item.id)
                    }

                    SwipeableCardDirection.Left -> {
                        onLeftSwipe(item.id)
                    }
                }
            },
            properties =
                SwipeableCardsProperties(
                    padding = 20.dp, // Stack padding
                    swipeThreshold = 50.dp, // Swipe distance threshold
                    lockBelowCardDragging = true, // Lock cards below top card
                    enableRotation = true, // Enable rotation animation
                    stackedCardsOffset = 15.dp, // Offset between cards
                    draggingAcceleration = 1.5f, // Drag sensitivity
                ),
            modifier = Modifier.weight(1f).fillMaxWidth(),
        ) {
            items(items) { profile, _, _ ->
                BottariCard(
                    item = profile,
                )
            }
        }
        Spacer(modifier = Modifier.height(BottariTheme.spacing.spaceMedium))
        SwipeButtons(
            items = items,
            state = state,
            onLeftSwipe = onLeftSwipe,
            onRightSwipe = onRightSwipe,
        )
    }
}

@Composable
private fun BottariCard(item: ChecklistItemUiModel) {
    BottariBox(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(BottariTheme.spacing.spaceXSmall),
    ) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = item.name,
                modifier = Modifier.fillMaxWidth(),
                style = BottariTheme.typography.bold40.toTextStyle(),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(BottariTheme.spacing.spaceMedium))
            Text(
                text = "챙기셨나요?",
                modifier = Modifier.fillMaxWidth(),
                style = BottariTheme.typography.medium16.toTextStyle(),
                textAlign = TextAlign.Center,
                color = BottariTheme.colors.gray700,
            )
            Spacer(modifier = Modifier.height(BottariTheme.spacing.spaceMedium))
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(20.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                Row(
                    modifier =
                        Modifier
                            .weight(1f)
                            .padding(end = BottariTheme.spacing.space2xLarge),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_previous),
                        contentDescription = null,
                        tint = BottariTheme.colors.red,
                    )
                    Spacer(modifier = Modifier.width(BottariTheme.spacing.spaceMedium))
                    Text("아직이에요", color = BottariTheme.colors.red)
                }
                VerticalDivider(thickness = 2.dp, color = BottariTheme.colors.gray200)
                Row(
                    modifier =
                        Modifier
                            .weight(1f)
                            .padding(start = BottariTheme.spacing.space2xLarge),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                ) {
                    Text("챙겼어요", color = BottariTheme.colors.primary)
                    Spacer(modifier = Modifier.width(BottariTheme.spacing.spaceMedium))
                    Icon(
                        painter = painterResource(R.drawable.ic_previous),
                        contentDescription = null,
                        tint = BottariTheme.colors.primary,
                        modifier = Modifier.rotate(180f),
                    )
                }
            }
            Spacer(modifier = Modifier.height(BottariTheme.spacing.spaceMedium))
            Text(
                text = "좌우로 스와이프하거나 아래 버튼을 눌러주세요",
                modifier = Modifier.fillMaxWidth(),
                style = BottariTheme.typography.medium14.toTextStyle(),
                textAlign = TextAlign.Center,
                color = BottariTheme.colors.gray500,
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun SwipeButtons(
    items: List<ChecklistItemUiModel>,
    state: SwipeableCardsState,
    onLeftSwipe: (Long) -> Unit,
    onRightSwipe: (Long) -> Unit,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        BottariBox(
            modifier =
                Modifier
                    .height(70.dp)
                    .weight(1f)
                    .clickable(onClick = {
                        onLeftSwipe(items[state.currentCardIndex].id)
                        state.swipe(SwipeableCardDirection.Left)
                    }),
        ) {
            Text(
                text = "아직이에요",
                modifier = Modifier.align(Alignment.Center),
                style = BottariTheme.typography.medium20.toTextStyle(),
            )
        }
        Spacer(modifier = Modifier.width(BottariTheme.spacing.spaceMedium))

        BottariBox(
            modifier =
                Modifier
                    .height(70.dp)
                    .weight(1f)
                    .background(BottariTheme.colors.primary)
                    .clickable(onClick = {
                        onRightSwipe(items[state.currentCardIndex].id)
                        state.swipe(SwipeableCardDirection.Right)
                    }),
        ) {
            Text(
                text = "챙겼어요",
                modifier = Modifier.align(Alignment.Center),
                style = BottariTheme.typography.medium20.toTextStyle(),
                color = BottariTheme.colors.white,
            )
        }
    }
}
