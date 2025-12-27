package com.bottari.presentation.compose.template.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.bottari.designsystem.theme.LocalBottariBgColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTemplateTopApp(navigateToBack: () -> Unit) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "보따리 템플릿 등록",
                style = BottariTheme.typography.bold20.toTextStyle(),
            )
        },
        navigationIcon = {
            IconButton(onClick = navigateToBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = null,
                )
            }
        },
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = LocalBottariBgColor.current,
                titleContentColor = BottariTheme.colors.black,
            ),
    )
}

@Preview
@Composable
private fun CreateTemplateTopApp() {
    BottariTheme {
        CreateTemplateTopApp(navigateToBack = {})
    }
}
