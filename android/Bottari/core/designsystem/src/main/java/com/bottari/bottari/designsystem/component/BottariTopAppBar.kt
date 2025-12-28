package com.bottari.bottari.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bottari.bottari.designsystem.preview.ComponentPreview
import com.bottari.bottari.designsystem.theme.BottariTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottariTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    centerAligned: Boolean = true,
) {
    val colors =
        TopAppBarDefaults.topAppBarColors(
            containerColor = BottariTheme.colors.gray50,
            titleContentColor = BottariTheme.colors.black,
        )

    val titleContent =
        @Composable {
            Text(
                text = title,
                style = BottariTheme.typography.bold20.toTextStyle(),
            )
        }

    if (centerAligned) {
        CenterAlignedTopAppBar(
            modifier = modifier,
            title = titleContent,
            navigationIcon = navigationIcon ?: {},
            actions = actions,
            colors = colors,
        )
    } else {
        TopAppBar(
            modifier = modifier,
            title = titleContent,
            navigationIcon = navigationIcon ?: {},
            actions = actions,
            colors = colors,
        )
    }
}

@ComponentPreview
@Composable
private fun BottariTopAppBarPreview() {
    BottariTheme {
        Column {
            BottariTopAppBar(
                title = "보따리",
                navigationIcon = {
                    BottariIconButton(onClick = {}) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    BottariIconButton(onClick = {}) {
                        Icon(Icons.Default.Search, contentDescription = null)
                    }
                },
            )

            Spacer(
                Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(Color.Gray),
            )

            BottariTopAppBar(
                title = "보따리",
                centerAligned = false,
            )
        }
    }
}
