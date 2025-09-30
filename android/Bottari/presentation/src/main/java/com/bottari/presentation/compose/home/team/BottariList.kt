package com.bottari.presentation.compose.home.team

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    modifier: Modifier = Modifier,
) {
    var openedMenuBottariId by remember { mutableStateOf<Long?>(null) }

    LazyColumn(modifier = modifier.fillMaxSize()) {
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
                            vertical = BottariTheme.spacing.spaceXSmall,
                        ).clickable { onBottariClick(bottari) },
                onPersonalBottariDelete = onDeletePersonalBottari,
                onTeamBottariDelete = onDeleteTeamBottari,
                onPersonalBottariEdit = onPersonalBottariEdit,
                onTeamBottariEdit = onTeamBottariEdit,
                showMenu = { openedMenuBottariId = bottari.id },
                closeMenu = { openedMenuBottariId = null },
            )
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
