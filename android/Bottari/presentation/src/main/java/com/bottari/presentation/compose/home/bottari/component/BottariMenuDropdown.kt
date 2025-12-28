package com.bottari.presentation.compose.home.bottari.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.bottari.designsystem.component.BottariIconButton
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.presentation.R

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
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        shape = BottariTheme.shapes.radiusMedium,
        containerColor = BottariTheme.colors.white,
    ) {
        Row(
            modifier =
                Modifier
                    .height(48.dp)
                    .padding(horizontal = BottariTheme.spacing.spaceXSmall),
            horizontalArrangement = Arrangement.spacedBy(BottariTheme.spacing.space2xSmall),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BottariMenuDropdownItem(
                iconResId = R.drawable.ic_edit,
                contentDescriptionResId = R.string.bottari_edit_btn_description,
            ) {
                onBottariEdit()
                onDismissRequest()
            }

            VerticalDivider(
                modifier = Modifier.fillMaxHeight(),
                thickness = 1.dp,
                color = BottariTheme.colors.gray200,
            )

            BottariMenuDropdownItem(
                iconResId = R.drawable.ic_delete,
                contentDescriptionResId = R.string.bottari_delete_btn_description,
            ) {
                onBottariDelete()
                onDismissRequest()
            }
        }
    }
}

@Composable
private fun BottariMenuDropdownItem(
    @DrawableRes iconResId: Int,
    @StringRes contentDescriptionResId: Int,
    onClick: () -> Unit,
) {
    BottariIconButton(onClick = onClick) {
        Icon(
            painter = painterResource(iconResId),
            contentDescription = stringResource(contentDescriptionResId),
        )
    }
}

@Preview(showBackground = true)
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
