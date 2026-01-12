package com.bottari.feature.template

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bottari.bottari.designsystem.theme.BottariTheme

@Composable
fun TemplateScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = "TemplateScreen")
    }
}

@Preview
@Composable
private fun TemplateScreenPreview() {
    BottariTheme {
        TemplateScreen()
    }
}
