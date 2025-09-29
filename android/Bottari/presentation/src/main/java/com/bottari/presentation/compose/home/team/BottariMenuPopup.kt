package com.bottari.presentation.compose.home.team

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.modifier.dropShadow
import com.bottari.presentation.model.bottari.MyBottariUiModel
import com.bottari.presentation.model.bottari.personal.BottariUiModel
import com.bottari.presentation.model.bottari.team.TeamBottariUiModel

@Composable
fun BottariMenuPopup(
    bottari: MyBottariUiModel,
    showMenu: Boolean,
    onDismissRequest: () -> Unit,
    onPersonalBottariDelete: (Long) -> Unit,
    onTeamBottariDelete: (Long) -> Unit,
    onPersonalBottariEdit: (Long, Boolean) -> Unit,
    onTeamBottariEdit: (Long, Boolean) -> Unit,
) {
    if (showMenu) {
        Popup(
            alignment = Alignment.TopEnd,
            onDismissRequest = onDismissRequest,
        ) {
            Box(
                modifier =
                    Modifier.padding(
                        top = 4.dp,
                        bottom = 8.dp,
                        end = 8.dp,
                    ),
            ) {
                MoreMenuPopup(
                    onEdit = {
                        if (bottari is BottariUiModel) {
                            onPersonalBottariEdit(bottari.id, false)
                        } else if (bottari is TeamBottariUiModel) {
                            onTeamBottariEdit(bottari.id, false)
                        }
                        onDismissRequest()
                    },
                    onDelete = {
                        if (bottari is BottariUiModel) {
                            onPersonalBottariDelete(bottari.id)
                        } else if (bottari is TeamBottariUiModel) {
                            onTeamBottariDelete(bottari.id)
                        }
                        onDismissRequest()
                    },
                )
            }
        }
    }
}

@Composable
private fun MoreMenuPopup(
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .size(200.dp, 100.dp)
                .dropShadow(RoundedCornerShape(8.dp), Color.Black.copy(0.1f), 8.dp, 0.dp, 4.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable(onClick = onEdit),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_pen),
                contentDescription = "수정하기",
            )
        }

        Box(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable(onClick = onDelete),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_delete),
                contentDescription = "삭제하기",
            )
        }
    }
}
