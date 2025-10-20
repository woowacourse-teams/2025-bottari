package com.bottari.presentation.compose.personal

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.theme.BottariTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChecklistTopBar(
    title: String,
    onBackClick: () -> Unit,
    onSwipeClick: () -> Unit,
    onResetClick: () -> Unit,
    isSwipeIconVisible: Boolean,
    isResetIconVisible: Boolean,
    modifier: Modifier = Modifier,
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = title,
                style = BottariTheme.typography.bold20.toTextStyle(),
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.size(48.dp),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_previous),
                    contentDescription = stringResource(R.string.common_previous_btn_description),
                    modifier = Modifier.size(24.dp),
                )
            }
        },
        actions = {
            if (isResetIconVisible) {
                IconButton(
                    onClick = onResetClick,
                    enabled = isResetIconVisible,
                    modifier = Modifier.size(48.dp),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_reset),
                        contentDescription = stringResource(R.string.checklist_btn_reset_description),
                        modifier = Modifier.size(24.dp),
                    )
                }
            }
            if (isSwipeIconVisible) {
                IconButton(
                    onClick = onSwipeClick,
                    enabled = isSwipeIconVisible,
                    modifier = Modifier.size(48.dp),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_swipe),
                        contentDescription = stringResource(R.string.checklist_btn_swipe_description),
                        modifier = Modifier.size(24.dp),
                    )
                }
            }
        },
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = BottariTheme.colors.gray50,
                titleContentColor = BottariTheme.colors.black,
            ),
    )
}

@Preview
@Composable
private fun HomeTopAppBarPreview() {
    BottariTheme {
        ChecklistTopBar(title = "보따리보따리보따리보따리보따리", {}, {}, {}, true, true)
    }
}
