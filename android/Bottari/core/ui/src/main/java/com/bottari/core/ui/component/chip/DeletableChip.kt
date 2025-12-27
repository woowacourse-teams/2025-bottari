package com.bottari.core.ui.component.chip

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.core.ui.extension.noRippleClickable
import com.bottari.core.ui.source.NoRippleInteractionSource

@Composable
fun DeletableChip(
    text: String,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = BottariTheme.typography.medium14.toTextStyle(),
    colors: SelectableChipColors = defaultDeletableChipColors(),
) {
    InputChip(
        selected = false,
        label = { DeletableChipLabel(label = text, textStyle = textStyle) },
        onClick = onClick,
        trailingIcon = { DeletableChipTrailingIcon(onDelete) },
        border =
            BorderStroke(
                width = 1.dp,
                color = BottariTheme.colors.gray300,
            ),
        shape = RoundedCornerShape(999.dp),
        colors = colors,
        interactionSource = NoRippleInteractionSource(),
        modifier = modifier,
    )
}

@Composable
private fun defaultDeletableChipColors(): SelectableChipColors =
    InputChipDefaults.inputChipColors(
        containerColor = BottariTheme.colors.primarySoft,
        labelColor = BottariTheme.colors.primary,
        trailingIconColor = BottariTheme.colors.gray500,
    )

@Composable
private fun DeletableChipLabel(
    label: String,
    textStyle: TextStyle,
) {
    Row(
        modifier = Modifier.fillMaxHeight(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            style = textStyle,
        )
    }
}

@Composable
private fun DeletableChipTrailingIcon(onDelete: () -> Unit) {
    Icon(
        imageVector = Icons.Filled.Close,
        contentDescription = "삭제",
        modifier =
            Modifier
                .noRippleClickable { onDelete() }
                .padding(BottariTheme.spacing.space2xSmall),
    )
}

@Preview(showBackground = true)
@Composable
private fun DeletableChipPreview() {
    BottariTheme {
        Box(
            modifier =
                Modifier
                    .height(40.dp)
                    .padding(4.dp),
        ) {
            DeletableChip(
                text = "태그",
                onClick = {},
                onDelete = {},
            )
        }
    }
}
