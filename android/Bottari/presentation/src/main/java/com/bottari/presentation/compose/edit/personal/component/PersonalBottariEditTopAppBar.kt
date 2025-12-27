package com.bottari.presentation.compose.edit.personal.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.presentation.R
import com.bottari.core.ui.R as UIR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalBottariEditTopAppBar(
    bottariTitle: String,
    onBackClick: () -> Unit,
    onBottariRenameClick: () -> Unit,
    onCreateTemplateClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isMenuExpanded by remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = bottariTitle,
                style = BottariTheme.typography.semiBold20.toTextStyle(),
            )
        },
        modifier = modifier,
        navigationIcon = {
            IconButton(
                onClick = onBackClick,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(UIR.string.common_previous_btn_description),
                )
            }
        },
        actions = {
            IconButton(
                onClick = { isMenuExpanded = true },
            ) {
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
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = BottariTheme.colors.gray50,
                titleContentColor = BottariTheme.colors.black,
            ),
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
            text = {
                Text(
                    text = stringResource(R.string.menu_bottari_rename_title_text),
                )
            },
            onClick = {
                onBottariRenameClick()
                onDismissRequest()
            },
        )
        DropdownMenuItem(
            text = {
                Text(
                    text = stringResource(R.string.menu_create_template_title_text),
                )
            },
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
