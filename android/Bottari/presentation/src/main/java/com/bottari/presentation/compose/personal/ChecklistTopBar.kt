package com.bottari.presentation.compose.personal

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.modifier.noRippleClickable
import com.bottari.presentation.compose.common.theme.BottariTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChecklistTopBar(
    title: String,
    onBackClick: () -> Unit,
    onSwipeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_previous),
                    contentDescription = null,
                    modifier = Modifier.noRippleClickable(onClick = onBackClick),
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = title,
                    style = BottariTheme.typography.bold20.toTextStyle(),
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(R.drawable.ic_swipe),
                    contentDescription = null,
                    modifier = Modifier.noRippleClickable(onClick = onSwipeClick),
                )
                Spacer(modifier = Modifier.width(BottariTheme.spacing.spaceMedium))
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
        ChecklistTopBar(title = "보따리", {}, {})
    }
}
