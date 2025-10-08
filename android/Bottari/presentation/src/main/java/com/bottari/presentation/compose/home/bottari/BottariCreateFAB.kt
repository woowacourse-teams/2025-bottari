package com.bottari.presentation.compose.home.bottari

import androidx.activity.compose.BackHandler
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

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BottariCreateFAB(
    onOpenPersonalDialog: () -> Unit,
    onOpenTeamDialog: () -> Unit,
    onOpenCodeDialog: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }

    val items =
        listOf(
            Triple(
                R.drawable.ic_team_invite_code,
                stringResource(R.string.team_bottari_join_btn_text),
                onOpenCodeDialog,
            ),
            Triple(
                R.drawable.ic_people,
                stringResource(R.string.team_bottari_create_btn_text),
                onOpenTeamDialog,
            ),
            Triple(
                R.drawable.ic_person_filled,
                stringResource(R.string.personal_bottari_create_btn_text),
                onOpenPersonalDialog,
            ),
        )

    var fabMenuExpanded by rememberSaveable { mutableStateOf(false) }

    BackHandler(fabMenuExpanded) { fabMenuExpanded = false }

    FloatingActionButtonMenu(
        expanded = fabMenuExpanded,
        button = {
            ToggleFloatingActionButton(
                checked = fabMenuExpanded,
                onCheckedChange = { fabMenuExpanded = !fabMenuExpanded },
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
        items.forEachIndexed { i, item ->
            FloatingActionButtonMenuItem(
                modifier =
                    Modifier
                        .semantics {
                            isTraversalGroup = true
                            if (i == items.size - 1) {
                                customActions =
                                    listOf(
                                        CustomAccessibilityAction(
                                            label = "Close menu",
                                            action = {
                                                fabMenuExpanded = false
                                                true
                                            },
                                        ),
                                    )
                            }
                        }.then(
                            if (i == 0) {
                                Modifier.onKeyEvent {
                                    if (
                                        it.type == KeyEventType.KeyDown &&
                                        (
                                            it.key == Key.DirectionUp ||
                                                (it.isShiftPressed && it.key == Key.Tab)
                                        )
                                    ) {
                                        focusRequester.requestFocus()
                                        return@onKeyEvent true
                                    }
                                    return@onKeyEvent false
                                }
                            } else {
                                Modifier
                            },
                        ),
                onClick = {
                    fabMenuExpanded = false
                    item.third()
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.first),
                        contentDescription = null,
                    )
                },
                text = { Text(text = item.second) },
            )
        }
    }
}
