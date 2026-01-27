package com.bottari.presentation.compose.common.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.presentation.compose.common.theme.BottariTheme

@Composable
fun IndeterminateCircularIndicator(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            modifier = Modifier.size(60.dp),
            color = BottariTheme.colors.primary,
            trackColor = BottariTheme.colors.gray100,
        )
    }
}

@Preview
@Composable
private fun IndeterminateCircularIndicatorPreview() {
    IndeterminateCircularIndicator()
}
