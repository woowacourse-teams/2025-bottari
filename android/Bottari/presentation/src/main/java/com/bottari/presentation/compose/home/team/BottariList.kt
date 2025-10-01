package com.bottari.presentation.compose.home.team

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
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

@Composable
fun BottariList(
    bottaries: List<MyBottariUiModel>,
    listState: LazyListState,
    onBottariClick: (MyBottariUiModel) -> Unit,
    onDeletePersonalBottari: (Long) -> Unit,
    onDeleteTeamBottari: (Long) -> Unit,
    onEditPersonalBottari: (Long) -> Unit,
    onEditTeamBottari: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    var openedMenuBottariId by remember { mutableStateOf<Long?>(null) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(vertical = BottariTheme.spacing.spaceSmall),
        verticalArrangement = Arrangement.spacedBy(BottariTheme.spacing.spaceXSmall),
    ) {
        items(
            items = bottaries,
            key = { bottari -> bottari.id to bottari::class.java.simpleName },
        ) { bottari ->
            BottariItem(
                bottari = bottari,
                isMenuShown = (openedMenuBottariId == bottari.id),
                onShowMenu = { openedMenuBottariId = bottari.id },
                onCloseMenu = { openedMenuBottariId = null },
                onBottariDelete = onBottariDelete@{
                    if (bottari is BottariUiModel) {
                        onDeletePersonalBottari(bottari.id)
                        return@onBottariDelete
                    }
                    onDeleteTeamBottari(bottari.id)
                },
                onBottariEdit = onBottariEdit@{
                    if (bottari is BottariUiModel) {
                        onEditPersonalBottari(bottari.id)
                        return@onBottariEdit
                    }
                    onEditTeamBottari(bottari.id)
                },
                modifier =
                    Modifier
                        .padding(
                            horizontal = BottariTheme.spacing.spaceMedium,
                        ).clickable { onBottariClick(bottari) },
            )
        }
    }
}
