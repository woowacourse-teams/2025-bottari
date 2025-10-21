package com.bottari.presentation.compose.personal.swipe

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bottari.presentation.compose.common.theme.BottariTheme
import com.bottari.presentation.compose.personal.ChecklistProgressHeader
import com.bottari.presentation.model.bottari.PersonalChecklistItemUiModel
import com.bottari.presentation.model.bottari.team.ChecklistItemUiModel
import com.spartapps.swipeablecards.state.SwipeableCardsState

@Composable
fun SwipeScreen(
    items: List<ChecklistItemUiModel>,
    checkedQuantity: Int,
    totalQuantity: Int,
    isComplete: Boolean,
    onLeftSwipe: (ChecklistItemUiModel) -> Unit,
    onRightSwipe: (ChecklistItemUiModel) -> Unit,
    onClickCompleteButton: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val rememberableItems = remember { items }

    val state =
        remember(rememberableItems) {
            SwipeableCardsState(
                initialCardIndex = 0,
                itemCount = { rememberableItems.size },
            )
        }

    Column(modifier = modifier.padding(BottariTheme.spacing.spaceMedium)) {
        ChecklistProgressHeader(
            checkedQuantity = checkedQuantity,
            totalQuantity = totalQuantity,
        )
        Spacer(modifier = Modifier.height(BottariTheme.spacing.spaceSmall))
        if (state.currentCardIndex != (rememberableItems.size)) {
            CardStackScreen(
                state = state,
                items = rememberableItems,
                onLeftSwipe = onLeftSwipe,
                onRightSwipe = onRightSwipe,
                modifier =
                    Modifier
                        .padding(BottariTheme.spacing.spaceXSmall)
                        .weight(1f),
            )
            return@Column
        }
        CardStackEndScreen(
            modifier =
                Modifier
                    .padding(BottariTheme.spacing.spaceXSmall)
                    .weight(1f),
            isCompleted = isComplete,
            onClickButton = onClickCompleteButton,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SwipeScreenPreview() {
    SwipeScreen(
        listOf(
            PersonalChecklistItemUiModel(1, "눈누난나아무튼엄청긴글자", false),
            PersonalChecklistItemUiModel(1, "테스트", false),
            PersonalChecklistItemUiModel(1, "테스트", false),
            PersonalChecklistItemUiModel(1, "테스트", false),
            PersonalChecklistItemUiModel(1, "테스트", false),
        ),
        3,
        7,
        false,
        onLeftSwipe = {},
        onRightSwipe = {},
        {},
    )
}
