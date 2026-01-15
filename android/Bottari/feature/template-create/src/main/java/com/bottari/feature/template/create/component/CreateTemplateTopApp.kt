package com.bottari.feature.template.create.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.bottari.bottari.designsystem.component.BottariIconButton
import com.bottari.bottari.designsystem.component.BottariTopAppBar
import com.bottari.bottari.designsystem.theme.BottariTheme

@Composable
fun CreateTemplateTopApp(navigateToBack: () -> Unit) {
    BottariTopAppBar(
        title = "보따리 템플릿 등록",
        navigationIcon = {
            BottariIconButton(onClick = navigateToBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = null,
                )
            }
        },
    )
}

@Preview
@Composable
private fun CreateTemplateTopAppPreview() {
    BottariTheme {
        CreateTemplateTopApp(navigateToBack = {})
    }
}
