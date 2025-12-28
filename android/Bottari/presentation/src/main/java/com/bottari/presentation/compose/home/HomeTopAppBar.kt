package com.bottari.presentation.compose.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.bottari.designsystem.component.BottariTopAppBar
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.presentation.R

@Composable
fun HomeTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
) {
    BottariTopAppBar(
        modifier = modifier,
        title = title,
        centerAligned = false,
        navigationIcon = {
            Image(
                painter = painterResource(R.drawable.img_bottari_logo),
                contentDescription = null,
                modifier =
                    Modifier
                        .size(48.dp)
                        .padding(8.dp)
                        .clip(BottariTheme.shapes.radiusXSmall),
            )
        },
    )
}

@Preview
@Composable
private fun HomeTopAppBarPreview() {
    BottariTheme {
        HomeTopAppBar(title = "보따리")
    }
}
