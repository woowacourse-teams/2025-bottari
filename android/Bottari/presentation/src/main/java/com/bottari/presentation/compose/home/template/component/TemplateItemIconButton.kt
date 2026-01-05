package com.bottari.presentation.compose.home.template.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.bottari.designsystem.component.BottariToggleButton
import com.bottari.bottari.designsystem.component.BottariToggleButtonTone
import com.bottari.bottari.designsystem.theme.BottariTheme

@Immutable
sealed interface TemplateItemType {
    data object MyTemplate : TemplateItemType

    data class Bookmark(
        val isBookmarked: Boolean,
    ) : TemplateItemType
}

@Composable
fun TemplateItemIconButton(
    type: TemplateItemType,
    onClick: () -> Unit,
) {
    BottariToggleButton(
        checked = type is TemplateItemType.Bookmark && type.isBookmarked,
        onCheckedChange = { onClick() },
        tone = BottariToggleButtonTone.Primary,
        shape = BottariTheme.shapes.circle,
    ) {
        when (type) {
            is TemplateItemType.MyTemplate -> MyTemplateIcon()
            is TemplateItemType.Bookmark -> BookmarkIcon(isBookmarked = type.isBookmarked)
        }
    }
}

@Composable
private fun MyTemplateIcon(modifier: Modifier = Modifier) {
    Icon(
        imageVector = Icons.Outlined.Delete,
        contentDescription = null,
        tint = BottariTheme.colors.gray500,
        modifier = modifier,
    )
}

@Composable
private fun BookmarkIcon(
    isBookmarked: Boolean,
    modifier: Modifier = Modifier,
) {
    val icon = if (isBookmarked) Icons.Outlined.Bookmark else Icons.Outlined.BookmarkBorder
    val iconColor = if (isBookmarked) BottariTheme.colors.primary else BottariTheme.colors.gray500

    Icon(
        imageVector = icon,
        contentDescription = null,
        tint = iconColor,
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
private fun TemplateItemIconButtonPreview() {
    BottariTheme {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            TemplateItemIconButton(
                type = TemplateItemType.MyTemplate,
                onClick = {},
            )

            TemplateItemIconButton(
                type = TemplateItemType.Bookmark(isBookmarked = true),
                onClick = {},
            )

            TemplateItemIconButton(
                type = TemplateItemType.Bookmark(isBookmarked = false),
                onClick = {},
            )
        }
    }
}
