package com.bottari.presentation.compose.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.presentation.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(BottariTheme.spacing.spaceSmall),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(R.drawable.img_bottari_logo),
                    contentDescription = null,
                    modifier =
                        Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(4.dp)),
                )
                Text(
                    text = title,
                    style = BottariTheme.typography.bold20.toTextStyle(),
                )
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
        HomeTopAppBar(title = "보따리")
    }
}
