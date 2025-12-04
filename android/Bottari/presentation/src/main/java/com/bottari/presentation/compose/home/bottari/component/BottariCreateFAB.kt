package com.bottari.presentation.compose.home.bottari.component

import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.FloatingActionButtonMenuScope
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.material3.ToggleFloatingActionButtonDefaults
import androidx.compose.material3.ToggleFloatingActionButtonDefaults.containerCornerRadius
import androidx.compose.material3.animateFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.core.designsystem.theme.BottariTheme
import com.bottari.presentation.R
import com.bottari.core.designsystem.R as DsR

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BottariCreateFAB(
    onOpenPersonalDialog: () -> Unit,
    onOpenTeamDialog: () -> Unit,
    onOpenCodeDialog: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    var fabMenuExpanded by rememberSaveable { mutableStateOf(false) }
    val items = buildFabMenuItems(onOpenPersonalDialog, onOpenTeamDialog, onOpenCodeDialog)

    val primaryColor = BottariTheme.colors.primary
    val whiteColor = BottariTheme.colors.white

    BackHandler(fabMenuExpanded) { fabMenuExpanded = false }

    FloatingActionButtonMenu(
        expanded = fabMenuExpanded,
        button = {
            BottariCreateToggleFloatingActionButton(
                fabMenuExpanded = fabMenuExpanded,
                onChangeExpandedState = { expandedState -> fabMenuExpanded = expandedState },
                containerColor = primaryColor,
                contentColor = whiteColor,
                focusRequester = focusRequester,
            )
        },
    ) {
        items.forEachIndexed { index, item ->
            BottariFabMenuItem(
                item = item,
                isFirstItem = index == 0,
                isLastItem = index == items.lastIndex,
                onCloseMenu = { fabMenuExpanded = false },
                containerColor = primaryColor,
                contentColor = whiteColor,
                focusRequester = focusRequester,
            )
        }
    }
}

@Composable
private fun buildFabMenuItems(
    onOpenPersonalDialog: () -> Unit,
    onOpenTeamDialog: () -> Unit,
    onOpenCodeDialog: () -> Unit,
): List<FabMenuItem> {
    val personalBottariText = stringResource(R.string.personal_bottari_create_btn_text)
    val teamBottariText = stringResource(R.string.team_bottari_create_btn_text)
    val joinTeamBottariText = stringResource(R.string.team_bottari_join_btn_text)

    return listOf(
        FabMenuItem(DsR.drawable.ic_team_invite_code, joinTeamBottariText, onOpenCodeDialog),
        FabMenuItem(R.drawable.ic_people, teamBottariText, onOpenTeamDialog),
        FabMenuItem(R.drawable.ic_person_filled, personalBottariText, onOpenPersonalDialog),
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun BottariCreateToggleFloatingActionButton(
    fabMenuExpanded: Boolean,
    onChangeExpandedState: (Boolean) -> Unit,
    containerColor: Color,
    contentColor: Color,
    focusRequester: FocusRequester,
) {
    val openMenuBtnDescription = stringResource(R.string.bottari_btn_create_description)
    val openStateDescription = stringResource(R.string.common_state_open_description)
    val closeStateDescription = stringResource(R.string.common_state_close_description)

    ToggleFloatingActionButton(
        checked = fabMenuExpanded,
        onCheckedChange = { onChangeExpandedState(it) },
        containerCornerRadius = containerCornerRadius(12.dp),
        modifier =
            Modifier
                .semantics {
                    traversalIndex = -1f
                    contentDescription = openMenuBtnDescription
                    stateDescription =
                        if (fabMenuExpanded) openStateDescription else closeStateDescription
                }.animateFloatingActionButton(
                    visible = true,
                    alignment = Alignment.BottomEnd,
                ).focusRequester(focusRequester),
        containerColor =
            ToggleFloatingActionButtonDefaults.containerColor(
                initialColor = containerColor,
                finalColor = containerColor,
            ),
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_plus),
            contentDescription = null,
            tint = contentColor,
            modifier =
                Modifier
                    .size(20.dp)
                    .graphicsLayer { rotationZ = checkedProgress * 45f },
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun FloatingActionButtonMenuScope.BottariFabMenuItem(
    item: FabMenuItem,
    isFirstItem: Boolean,
    isLastItem: Boolean,
    onCloseMenu: () -> Unit,
    containerColor: Color,
    contentColor: Color,
    focusRequester: FocusRequester,
) {
    val menuCloseLabel = stringResource(R.string.bottari_action_close_label_description)

    FloatingActionButtonMenuItem(
        onClick = {
            onCloseMenu()
            item.onClick()
        },
        text = { Text(text = item.text) },
        icon = {
            Icon(
                painter = painterResource(id = item.iconRes),
                contentDescription = null,
            )
        },
        modifier =
            Modifier
                .semantics {
                    isTraversalGroup = true
                    if (isLastItem) {
                        customActions =
                            listOf(
                                CustomAccessibilityAction(label = menuCloseLabel) {
                                    onCloseMenu()
                                    true
                                },
                            )
                    }
                }.then(
                    if (isFirstItem) {
                        Modifier.onKeyEvent {
                            val isTargetKeyEvent =
                                it.type == KeyEventType.KeyDown &&
                                    (it.key == Key.DirectionUp || (it.isShiftPressed && it.key == Key.Tab))

                            if (!isTargetKeyEvent) {
                                return@onKeyEvent false
                            }

                            focusRequester.requestFocus()
                            true
                        }
                    } else {
                        Modifier
                    },
                ),
        containerColor = containerColor,
        contentColor = contentColor,
    )
}

private data class FabMenuItem(
    @DrawableRes val iconRes: Int,
    val text: String,
    val onClick: () -> Unit,
)

@Preview
@Composable
private fun BottariCreateFABPreview() {
    BottariCreateFAB(
        onOpenPersonalDialog = {},
        onOpenTeamDialog = {},
        onOpenCodeDialog = {},
    )
}
