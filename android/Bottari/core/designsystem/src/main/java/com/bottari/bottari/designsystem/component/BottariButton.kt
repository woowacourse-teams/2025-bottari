package com.bottari.bottari.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bottari.bottari.designsystem.preview.ComponentPreview
import com.bottari.bottari.designsystem.theme.BottariTheme

enum class BottariButtonStyle { Primary, Secondary, None }

@Composable
fun BottariButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    style: BottariButtonStyle = BottariButtonStyle.Primary,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
) {
    val elevationValue = if (style != BottariButtonStyle.None) 1.dp else 0.dp

    Button(
        onClick = onClick,
        modifier = modifier.heightIn(min = 48.dp),
        enabled = enabled,
        shape = BottariTheme.shapes.radiusMedium,
        colors = buttonColorsByStyle(style),
        elevation =
            ButtonDefaults.buttonElevation(
                defaultElevation = elevationValue,
                pressedElevation = elevationValue,
            ),
        contentPadding = PaddingValues(horizontal = BottariTheme.spacing.spaceSmall),
    ) {
        leadingIcon?.let { leadingIcon ->
            leadingIcon()
            Spacer(Modifier.width(BottariTheme.spacing.spaceXSmall))
        }

        Text(
            text = text,
            style = BottariTheme.typography.semiBold16.toTextStyle(),
        )

        trailingIcon?.let { trailingIcon ->
            Spacer(Modifier.width(BottariTheme.spacing.spaceXSmall))
            trailingIcon()
        }
    }
}

@Composable
private fun buttonColorsByStyle(style: BottariButtonStyle) =
    when (style) {
        BottariButtonStyle.Primary -> {
            ButtonDefaults.buttonColors(
                containerColor = BottariTheme.colors.primary,
                disabledContainerColor = BottariTheme.colors.gray400,
                contentColor = BottariTheme.colors.white,
                disabledContentColor = BottariTheme.colors.white,
            )
        }

        BottariButtonStyle.Secondary -> {
            ButtonDefaults.buttonColors(
                containerColor = BottariTheme.colors.white,
                disabledContainerColor = BottariTheme.colors.gray200,
                contentColor = BottariTheme.colors.black,
                disabledContentColor = BottariTheme.colors.gray500,
            )
        }

        BottariButtonStyle.None -> {
            ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                contentColor = BottariTheme.colors.primary,
                disabledContentColor = BottariTheme.colors.gray400,
            )
        }
    }

@ComponentPreview
@Composable
private fun EnabledBottariButtonPreview() {
    BottariTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(16.dp),
        ) {
            BottariButton(text = "기본", onClick = {})
            BottariButton(text = "기본", onClick = {}, enabled = false)

            BottariButton(text = "Secondary", onClick = {}, style = BottariButtonStyle.Secondary)
            BottariButton(
                text = "Secondary",
                onClick = {},
                style = BottariButtonStyle.Secondary,
                enabled = false,
            )

            BottariButton(
                text = "Ghost",
                onClick = {},
                style = BottariButtonStyle.None,
                leadingIcon = { Icon(Icons.Default.Search, null) },
                trailingIcon = { Icon(Icons.Default.Close, null) },
            )
            BottariButton(
                text = "Ghost",
                onClick = {},
                style = BottariButtonStyle.None,
                leadingIcon = { Icon(Icons.Default.Search, null) },
                trailingIcon = { Icon(Icons.Default.Close, null) },
                enabled = false,
            )
        }
    }
}
