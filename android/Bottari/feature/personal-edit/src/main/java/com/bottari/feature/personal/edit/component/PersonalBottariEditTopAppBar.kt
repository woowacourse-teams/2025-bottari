package com.bottari.feature.personal.edit.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bottari.bottari.designsystem.component.BottariIconButton
import com.bottari.bottari.designsystem.component.BottariTopAppBar
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.feature.personal.edit.R
import com.bottari.core.ui.R as UIR

@Composable
fun PersonalBottariEditTopAppBar(
    bottariTitle: String,
    onBackClick: () -> Unit,
    onBottariRenameClick: () -> Unit,
    onCreateTemplateClick: () -> Unit,
) {
    var isMenuExpanded by remember { mutableStateOf(false) }

    BottariTopAppBar(
        title = bottariTitle,
        navigationIcon = {
            BottariIconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(UIR.string.common_previous_btn_description),
                )
            }
        },
        actions = {
            BottariIconButton(onClick = { isMenuExpanded = true }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = stringResource(UIR.string.common_option_btn_description),
                )
            }
            PersonalBottariEditMenu(
                isMenuExpanded = isMenuExpanded,
                onDismissRequest = { isMenuExpanded = false },
                onBottariRenameClick = onBottariRenameClick,
                onCreateTemplateClick = onCreateTemplateClick,
            )
        },
    )
}

@Composable
private fun PersonalBottariEditMenu(
    isMenuExpanded: Boolean,
    onDismissRequest: () -> Unit,
    onBottariRenameClick: () -> Unit,
    onCreateTemplateClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DropdownMenu(
        expanded = isMenuExpanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        containerColor = BottariTheme.colors.white,
    ) {
        DropdownMenuItem(
            text = { Text(text = stringResource(R.string.menu_bottari_rename_title_text)) },
            onClick = {
                onBottariRenameClick()
                onDismissRequest()
            },
        )
        DropdownMenuItem(
            text = { Text(text = stringResource(R.string.menu_create_template_title_text)) },
            onClick = {
                onCreateTemplateClick()
                onDismissRequest()
            },
        )
    }
}

@Preview
@Composable
private fun PersonalBottariEditTopAppBarPreview() {
    BottariTheme {
        PersonalBottariEditTopAppBar(
            bottariTitle = "보따리 편집",
            onBackClick = {},
            onBottariRenameClick = {},
            onCreateTemplateClick = {},
        )
    }
}
