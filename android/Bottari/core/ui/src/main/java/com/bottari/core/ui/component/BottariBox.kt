package com.bottari.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.core.ui.extension.dropShadow

@Composable
fun BottariBox(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(12.dp),
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier =
            Modifier
                .dropShadow(shape = shape)
                .clip(shape)
                .background(
                    shape = shape,
                    color = BottariTheme.colors.white,
                ).then(modifier)
                .padding(contentPadding),
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
private fun CardBoxPreview() {
    BottariTheme {
        Box(
            modifier = Modifier.padding(BottariTheme.spacing.spaceMedium),
        ) {
            BottariBox(
                content = {
                    Text(text = "보따리")
                },
            )
        }
    }
}
