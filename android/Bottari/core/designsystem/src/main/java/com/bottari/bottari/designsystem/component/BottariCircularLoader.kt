package com.bottari.bottari.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bottari.bottari.designsystem.preview.ComponentPreview
import com.bottari.bottari.designsystem.theme.BottariTheme

@Composable
fun BottariCircularLoader(
    modifier: Modifier = Modifier,
    color: Color = BottariTheme.colors.primary,
    trackColor: Color = BottariTheme.colors.gray100,
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            modifier = modifier.size(60.dp),
            color = color,
            trackColor = trackColor,
        )
    }
}

@ComponentPreview
@Composable
private fun BottariCircularLoaderPreview() {
    BottariTheme {
        BottariCircularLoader()
    }
}
