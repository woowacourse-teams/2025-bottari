package com.bottari.feature.more

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bottari.bottari.designsystem.theme.BottariTheme

@Composable
fun MoreScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = "MoreScreen")
    }
}

@Preview
@Composable
private fun MoreScreenPreview() {
    BottariTheme {
        MoreScreen()
    }
}
