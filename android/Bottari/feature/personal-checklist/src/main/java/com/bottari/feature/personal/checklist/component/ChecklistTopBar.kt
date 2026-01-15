package com.bottari.feature.personal.checklist.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.bottari.designsystem.component.BottariIconButton
import com.bottari.bottari.designsystem.component.BottariTopAppBar
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.core.ui.R
import com.bottari.core.ui.R as PresentationR
import com.bottari.core.ui.R as UIR

@Composable
fun ChecklistTopBar(
    title: String,
    onBackClick: () -> Unit,
    onSwipeClick: () -> Unit,
    onResetClick: () -> Unit,
    isSwipeIconVisible: Boolean,
    isResetIconVisible: Boolean,
) {
    BottariTopAppBar(
        title = title,
        navigationIcon = {
            BottariIconButton(
                onClick = onBackClick,
                modifier = Modifier.size(48.dp),
            ) {
                Icon(
                    painter = painterResource(PresentationR.drawable.ic_previous),
                    contentDescription = stringResource(UIR.string.common_previous_btn_description),
                    modifier = Modifier.size(24.dp),
                )
            }
        },
        actions = {
            if (isResetIconVisible) {
                BottariIconButton(onClick = onResetClick) {
                    Icon(
                        painter = painterResource(PresentationR.drawable.ic_reset),
                        contentDescription = stringResource(R.string.checklist_btn_reset_description),
                        modifier = Modifier.size(24.dp),
                    )
                }
            }
            if (isSwipeIconVisible) {
                BottariIconButton(onClick = onSwipeClick) {
                    Icon(
                        painter = painterResource(PresentationR.drawable.ic_swipe),
                        contentDescription = stringResource(R.string.checklist_btn_swipe_description),
                    )
                }
            }
        },
    )
}

@Preview
@Composable
private fun HomeTopAppBarPreview() {
    BottariTheme {
        ChecklistTopBar(title = "보따리보따리보따리보따리보따리", {}, {}, {}, true, true)
    }
}
