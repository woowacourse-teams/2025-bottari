package com.bottari.presentation.compose.personal

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.modifier.noRippleClickable
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
            Icon(
                painter = painterResource(R.drawable.ic_previous),
                contentDescription = "Back",
                modifier =
                    Modifier
                        .size(24.dp)
                        .noRippleClickable(onClick = onBackClick),
            )
        },
        actions = {
            Icon(
                painter = painterResource(R.drawable.ic_reset),
                contentDescription = "Reset",
                modifier =
                    Modifier
                        .alpha(if (isResetIconVisible) 1f else 0f)
                        .size(24.dp)
                        .noRippleClickable(
                            onClick = onResetClick,
                            enabled = isResetIconVisible,
                        ),
            )
            Spacer(modifier = Modifier.width(BottariTheme.spacing.spaceXSmall))
            Icon(
                painter = painterResource(R.drawable.ic_swipe),
                contentDescription = "Swipe",
                modifier =
                    Modifier
                        .alpha(if (isSwipeIconVisible) 1f else 0f)
                        .noRippleClickable(
                            onClick = onSwipeClick,
                            enabled = isSwipeIconVisible,
                        ),
            )
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
        ChecklistTopBar(title = "보따리", {}, {}, {}, true, true)
    }
}
