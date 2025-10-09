package com.bottari.presentation.compose.home.bottari

import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.material3.animateFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
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
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.theme.BottariTheme

private data class FabMenuItem(
    @DrawableRes val iconRes: Int,
    val text: String,
    val onClick: () -> Unit,
)

@Composable
private fun rememberFabMenuItems(
    onOpenPersonalDialog: () -> Unit,
    onOpenTeamDialog: () -> Unit,
    onOpenCodeDialog: () -> Unit,
): List<FabMenuItem> {
    val personalBottariText = stringResource(R.string.personal_bottari_create_btn_text)
    val teamBottariText = stringResource(R.string.team_bottari_create_btn_text)
    val joinTeamText = stringResource(R.string.team_bottari_join_btn_text)

    return remember(onOpenPersonalDialog, onOpenTeamDialog, onOpenCodeDialog) {
        listOf(
            FabMenuItem(R.drawable.ic_team_invite_code, joinTeamText, onOpenCodeDialog),
            FabMenuItem(R.drawable.ic_people, teamBottariText, onOpenTeamDialog),
            FabMenuItem(R.drawable.ic_person_filled, personalBottariText, onOpenPersonalDialog),
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BottariCreateFAB(
    onOpenPersonalDialog: () -> Unit,
    onOpenTeamDialog: () -> Unit,
    onOpenCodeDialog: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    var fabMenuExpanded by rememberSaveable { mutableStateOf(false) }
    val items = rememberFabMenuItems(onOpenPersonalDialog, onOpenTeamDialog, onOpenCodeDialog)

    BackHandler(fabMenuExpanded) { fabMenuExpanded = false }

    FloatingActionButtonMenu(
        expanded = fabMenuExpanded,
        button = {
            ToggleFloatingActionButton(
                checked = fabMenuExpanded,
                onCheckedChange = { fabMenuExpanded = it },
                modifier =
                    Modifier
                        .semantics {
                            traversalIndex = -1f
                            stateDescription = if (fabMenuExpanded) "Expanded" else "Collapsed"
                            contentDescription = "Toggle menu"
                        }.animateFloatingActionButton(
                            visible = true,
                            alignment = Alignment.BottomEnd,
                        ).focusRequester(focusRequester),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = null,
                    modifier = Modifier.rotate((1f - checkedProgress) * 45f),
                    tint = BottariTheme.colors.white,
                )
            }
        },
    ) {
        items.forEachIndexed { index, item ->
            FloatingActionButtonMenuItem(
                modifier =
                    Modifier
                        .semantics {
                            isTraversalGroup = true
                            if (index == items.lastIndex) {
                                customActions =
                                    listOf(
                                        CustomAccessibilityAction(label = "Close menu") {
                                            fabMenuExpanded = false
                                            true
                                        },
                                    )
                            }
                        }.then(
                            if (index == 0) {
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
                onClick = {
                    fabMenuExpanded = false
                    item.onClick()
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconRes),
                        contentDescription = null,
                    )
                },
                text = { Text(text = item.text) },
            )
        }
    }
}
