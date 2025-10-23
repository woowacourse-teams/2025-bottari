package com.bottari.presentation.compose.home.bottari

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.theme.BottariTheme

@Composable
fun BottariMenuDropdown(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onBottariDelete: () -> Unit,
    onBottariEdit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier =
            modifier.padding(
                horizontal = BottariTheme.spacing.space2xSmall,
            ),
        shape = RoundedCornerShape(12.dp),
        containerColor = BottariTheme.colors.white,
    ) {
        Row(
            modifier = Modifier.height(48.dp),
            horizontalArrangement = Arrangement.spacedBy(BottariTheme.spacing.space2xSmall),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = {
                    onBottariEdit()
                    onDismissRequest()
                },
                modifier = Modifier.padding(BottariTheme.spacing.space2xSmall),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_edit),
                    contentDescription = stringResource(R.string.bottari_edit_btn_description),
                )
            }

            VerticalDivider(
                modifier = Modifier.fillMaxHeight(0.8f),
                thickness = 1.dp,
                color = BottariTheme.colors.gray100,
            )

            IconButton(
                onClick = {
                    onBottariDelete()
                    onDismissRequest()
                },
                modifier = Modifier.padding(BottariTheme.spacing.space2xSmall),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_delete),
                    contentDescription = stringResource(R.string.bottari_delete_btn_description),
                )
            }
        }
    }
}

@Preview
@Composable
private fun BottariMenuDropdownPreview() {
    Box(modifier = Modifier.padding(150.dp)) {
        BottariMenuDropdown(
            expanded = true,
            onDismissRequest = {},
            onBottariDelete = {},
            onBottariEdit = {},
        )
    }
}
