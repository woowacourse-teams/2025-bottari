package com.bottari.presentation.compose.personal.swipe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import com.bottari.bottari.designsystem.component.BottariButton
import com.bottari.bottari.designsystem.component.BottariButtonStyle
import com.bottari.bottari.designsystem.component.BottariCard
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.presentation.R
import com.bottari.presentation.model.bottari.PersonalChecklistItemUiModel
import com.bottari.presentation.model.bottari.team.ChecklistItemUiModel
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
    onLeftSwipe: (ChecklistItemUiModel) -> Unit,
    onRightSwipe: (ChecklistItemUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        LazySwipeableCards(
            state = state,
            onSwipe = { item, direction ->
                when (direction) {
                    SwipeableCardDirection.Right -> {
                        onRightSwipe(item)
                    }

                    SwipeableCardDirection.Left -> {
                        onLeftSwipe(item)
                    }
                }
            },
            properties =
                SwipeableCardsProperties(
                    padding = 20.dp,
                    swipeThreshold = 50.dp,
                    lockBelowCardDragging = true,
                    enableRotation = true,
                    stackedCardsOffset = 15.dp,
                    draggingAcceleration = 1.5f,
                ),
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxWidth(),
        ) { items(items) { item, _, _ -> SwipeableChecklistItem(item = item) } }
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
private fun SwipeableChecklistItem(item: ChecklistItemUiModel) {
    BottariCard(
        modifier =
            Modifier
                .fillMaxHeight()
                .padding(BottariTheme.spacing.spaceXSmall),
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
        ) {
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
        }
    }
}

@Composable
private fun SwipeButtons(
    items: List<ChecklistItemUiModel>,
    state: SwipeableCardsState,
    onLeftSwipe: (ChecklistItemUiModel) -> Unit,
    onRightSwipe: (ChecklistItemUiModel) -> Unit,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        BottariButton(
            text = stringResource(R.string.checklist_swipe_not_btn_text),
            style = BottariButtonStyle.Secondary,
            onClick = {
                state.swipe(SwipeableCardDirection.Left)
                onLeftSwipe(items[min(state.currentCardIndex, items.size - 1)])
            },
            modifier =
                Modifier
                    .weight(1f)
                    .height(60.dp),
        )
        Spacer(modifier = Modifier.width(BottariTheme.spacing.spaceMedium))
        BottariButton(
            text = stringResource(R.string.checklist_swipe_yes_btn_text),
            onClick = {
                state.swipe(SwipeableCardDirection.Right)
                onRightSwipe(items[min(state.currentCardIndex, items.size - 1)])
            },
            modifier =
                Modifier
                    .weight(1f)
                    .height(60.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CardStackScreenPreview() {
    val items =
        listOf(
            PersonalChecklistItemUiModel(
                id = 1,
                name = "눈누난나아무튼엄청긴글자",
                isChecked = false,
            ),
            PersonalChecklistItemUiModel(
                id = 2,
                name = "눈누난나아무튼엄청긴글자",
                isChecked = false,
            ),
            PersonalChecklistItemUiModel(
                id = 3,
                name = "눈누난나아무튼엄청긴글자",
                isChecked = false,
            ),
            PersonalChecklistItemUiModel(
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

@Preview(showBackground = true)
@Composable
private fun SwipeableChecklistItemPreview() {
    SwipeableChecklistItem(
        PersonalChecklistItemUiModel(
            id = 4,
            name = "눈누난나아무튼엄청긴글자",
            isChecked = false,
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun SwipeButtonsPreview() {
    val items =
        listOf(
            PersonalChecklistItemUiModel(
                id = 1,
                name = "눈누난나아무튼엄청긴글자",
                isChecked = false,
            ),
            PersonalChecklistItemUiModel(
                id = 2,
                name = "눈누난나아무튼엄청긴글자",
                isChecked = false,
            ),
            PersonalChecklistItemUiModel(
                id = 3,
                name = "눈누난나아무튼엄청긴글자",
                isChecked = false,
            ),
            PersonalChecklistItemUiModel(
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
        items = items,
        state = state,
        onLeftSwipe = {},
        onRightSwipe = {},
    )
}
