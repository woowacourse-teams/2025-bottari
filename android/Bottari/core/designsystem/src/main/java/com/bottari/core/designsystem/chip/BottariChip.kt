package com.bottari.core.designsystem.chip

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.core.designsystem.theme.BottariTheme

@Composable
fun BottariChip(
    value: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = BottariTheme.typography.medium12.toTextStyle(),
    containerColor: Color = Color(0xFFEFF6FF),
    contentColor: Color = BottariTheme.colors.primary,
    onClick: () -> Unit = {},
) {
    val chipShape = remember { RoundedCornerShape(999.dp) }

    Box(
        contentAlignment = Alignment.Center,
        modifier =
            modifier
                .clip(chipShape)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(color = containerColor),
                    onClick = onClick,
                ).background(
                    color = containerColor,
                    shape = chipShape,
                ).padding(
                    vertical = BottariTheme.spacing.space2xSmall,
                    horizontal = BottariTheme.spacing.spaceXSmall,
                ),
    ) {
        Text(
            text = value,
            style = textStyle,
            color = contentColor,
        )
    }
}

@Preview
@Composable
private fun BottariChipPreview() {
    BottariTheme {
        BottariChip(value = "해시태그")
    }
}
