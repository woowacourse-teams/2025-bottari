package com.bottari.presentation.compose.personal.swipe

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
import kotlin.math.min

@Composable
fun CardStackScreen(
    state: SwipeableCardsState,
    items: List<ChecklistItemUiModel>,
    onLeftSwipe: (Long) -> Unit,
    onRightSwipe: (Long) -> Unit,
    modifier: Modifier = Modifier,
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
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxWidth(),
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
                text = stringResource(R.string.checklist_swipe_sub_title_text),
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
                    Text(
                        stringResource(R.string.checklist_swipe_no_overlay_view_text),
                        color = BottariTheme.colors.red,
                    )
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
                    Text(
                        stringResource(R.string.checklist_swipe_yes_overlay_view_text),
                        color = BottariTheme.colors.primary,
                    )
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
                text = stringResource(R.string.checklist_swipe_description_text),
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
                        state.swipe(SwipeableCardDirection.Left)
                        onLeftSwipe(items[min(state.currentCardIndex, items.size - 1)].id)
                    }),
        ) {
            Text(
                text = stringResource(R.string.checklist_swipe_not_btn_text),
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
                        state.swipe(SwipeableCardDirection.Right)
                        onRightSwipe(items[min(state.currentCardIndex, items.size - 1)].id)
                    }),
        ) {
            Text(
                text = stringResource(R.string.checklist_swipe_yes_btn_text),
                modifier = Modifier.align(Alignment.Center),
                style = BottariTheme.typography.medium20.toTextStyle(),
                color = BottariTheme.colors.white,
            )
        }
    }
}

@Preview
@Composable
private fun CardStackScreenPreview() {
    val items =
        listOf(
            ChecklistItemUiModel(
                id = 1,
                name = "눈누난나아무튼엄청긴글자",
                isChecked = false,
            ),
            ChecklistItemUiModel(
                id = 2,
                name = "눈누난나아무튼엄청긴글자",
                isChecked = false,
            ),
            ChecklistItemUiModel(
                id = 3,
                name = "눈누난나아무튼엄청긴글자",
                isChecked = false,
            ),
            ChecklistItemUiModel(
                id = 4,
                name = "눈누난나아무튼엄청긴글자",
                isChecked = false,
            ),
        )
    val state =
        remember(items) {
            SwipeableCardsState(
                initialCardIndex = 0,
                itemCount = { items.size },
            )
        }

    CardStackScreen(state, items, onLeftSwipe = {}, onRightSwipe = {})
}

@Preview
@Composable
private fun BottariCardPreview() {
    BottariCard(
        ChecklistItemUiModel(
            id = 4,
            name = "눈누난나아무튼엄청긴글자",
            isChecked = false,
        ),
    )
}

@Preview
@Composable
private fun SwipeButtonsPreview() {
    val items =
        listOf(
            ChecklistItemUiModel(
                id = 1,
                name = "눈누난나아무튼엄청긴글자",
                isChecked = false,
            ),
            ChecklistItemUiModel(
                id = 2,
                name = "눈누난나아무튼엄청긴글자",
                isChecked = false,
            ),
            ChecklistItemUiModel(
                id = 3,
                name = "눈누난나아무튼엄청긴글자",
                isChecked = false,
            ),
            ChecklistItemUiModel(
                id = 4,
                name = "눈누난나아무튼엄청긴글자",
                isChecked = false,
            ),
        )
    val state =
        remember(items) {
            SwipeableCardsState(
                initialCardIndex = 0,
                itemCount = { items.size },
            )
        }

    SwipeButtons(
        items,
        state,
        {},
        {},
    )
}
