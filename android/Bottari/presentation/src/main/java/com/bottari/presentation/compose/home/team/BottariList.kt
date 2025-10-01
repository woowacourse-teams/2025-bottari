package com.bottari.presentation.compose.home.team

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bottari.presentation.compose.common.theme.BottariTheme
import com.bottari.presentation.model.bottari.MyBottariUiModel
import com.bottari.presentation.model.bottari.personal.BottariUiModel
import com.bottari.presentation.model.bottari.team.TeamBottariUiModel

@Composable
fun BottariList(
    bottaries: List<MyBottariUiModel>,
    onBottariClick: (MyBottariUiModel) -> Unit,
    onDeletePersonalBottari: (Long) -> Unit,
    onDeleteTeamBottari: (Long) -> Unit,
    onPersonalBottariEdit: (Long, Boolean) -> Unit,
    onTeamBottariEdit: (Long, Boolean) -> Unit,
    openedMenuBottariId: Long?,
    onShowMenu: (Long) -> Unit,
    onCloseMenu: () -> Unit,
    listState: LazyListState,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(BottariTheme.spacing.spaceXSmall),
            contentPadding = PaddingValues(bottom = BottariTheme.spacing.spaceXSmall),
        ) {
            items(
                items = bottaries,
                key = { bottari -> bottari.id to bottari::class.java.simpleName },
            ) { bottari ->
                BottariItem(
                    bottari = bottari,
                    isShowMenu = openedMenuBottariId == bottari.id,
                    modifier =
                        Modifier
                            .padding(
                                horizontal = BottariTheme.spacing.spaceMedium,
                            )
                            .clickable(openedMenuBottariId == null) { onBottariClick(bottari) },
                    onPersonalBottariDelete = onDeletePersonalBottari,
                    onTeamBottariDelete = onDeleteTeamBottari,
                    onPersonalBottariEdit = onPersonalBottariEdit,
                    onTeamBottariEdit = onTeamBottariEdit,
                    showMenu = { onShowMenu(bottari.id) },
                    closeMenu = { onCloseMenu },
                )
            }
        }
    }
}

fun navigateToChecklist(
    bottari: MyBottariUiModel,
    onNavigateToPersonalChecklist: (Long, String) -> Unit,
    onNavigateToTeamChecklist: (Long, String) -> Unit,
) {
    when (bottari) {
        is BottariUiModel -> onNavigateToPersonalChecklist(bottari.id, bottari.title)
        is TeamBottariUiModel -> onNavigateToTeamChecklist(bottari.id, bottari.title)
    }
}
