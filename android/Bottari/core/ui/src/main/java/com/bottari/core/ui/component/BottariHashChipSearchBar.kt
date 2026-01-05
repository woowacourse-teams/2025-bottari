package com.bottari.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.bottari.designsystem.component.BottariIconButton
import com.bottari.bottari.designsystem.extension.dropShadow
import com.bottari.bottari.designsystem.theme.BottariTheme

@Composable
fun BottariHashChipSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    chips: List<String>,
    onChipsChange: (List<String>) -> Unit,
    placeholderText: String,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = BottariTheme.shapes.radiusLarge,
    textStyle: TextStyle = TextStyle(),
) {
    var isFocused by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Box(
        modifier =
            modifier.setupHashChipSearchBar(
                isFocused = isFocused,
                onFocusChanged = { focusState -> isFocused = focusState.isFocused },
                shape = shape,
            ),
    ) {
        HashChipTextField(
            value = query,
            onValueChange = onQueryChange,
            chips = chips,
            onChipsChange = { new -> handleChipsChange(new, onChipsChange, focusManager) },
            placeholderText = if (chips.isEmpty()) placeholderText else "",
            onSearch = onSearch,
            textStyle = textStyle,
            leadingIcon = { HashChipSearchBarLeadingIcon(isFocused) },
            trailingIcon = { if (query.isNotEmpty()) HashChipSearchBarTrailingIcon(onQueryChange) },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(52.dp),
        )
    }
}

@Composable
private fun Modifier.setupHashChipSearchBar(
    isFocused: Boolean,
    onFocusChanged: (FocusState) -> Unit,
    shape: Shape,
): Modifier =
    this
        .onFocusChanged(onFocusChanged)
        .fillMaxWidth()
        .dropShadow(shape, offsetY = 0.8.dp)
        .background(
            color = Color.White,
            shape = shape,
        ).border(
            width = 0.8.dp,
            color = if (isFocused) BottariTheme.colors.primary else Color.LightGray,
            shape = shape,
        )

@Composable
private fun HashChipSearchBarLeadingIcon(isFocused: Boolean) {
    Icon(
        imageVector = Icons.Default.Search,
        contentDescription = "검색 아이콘",
        tint = if (isFocused) BottariTheme.colors.black else BottariTheme.colors.gray500,
    )
}

@Composable
private fun HashChipSearchBarTrailingIcon(onQueryChange: (String) -> Unit) {
    BottariIconButton(onClick = { onQueryChange("") }) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "검색어 지우기",
        )
    }
}

private fun handleChipsChange(
    new: List<String>,
    onChipsChange: (List<String>) -> Unit,
    focusManager: FocusManager,
) {
    val filtered = new.filter { it.length > 1 && it.startsWith('#') }
    onChipsChange(filtered)
    focusManager.clearFocus()
}

@Preview(showBackground = true)
@Composable
private fun BottariHashChipSearchBarPreview() {
    var state by remember { mutableStateOf("") }
    val chips = remember { mutableStateListOf("#compose", "#android") }

    BottariHashChipSearchBar(
        query = state,
        onQueryChange = { state = it },
        chips = chips,
        onChipsChange = { new ->
            chips.clear()
            chips.addAll(new)
        },
        placeholderText = "제목이나 해시태그를 입력하세요",
        onSearch = {},
        modifier = Modifier.padding(BottariTheme.spacing.spaceSmall),
    )
}
