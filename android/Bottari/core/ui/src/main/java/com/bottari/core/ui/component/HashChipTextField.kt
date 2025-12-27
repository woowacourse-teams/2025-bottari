package com.bottari.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.core.ui.component.chip.DeletableChip

@Composable
fun HashChipTextField(
    value: String,
    onValueChange: (String) -> Unit,
    chips: List<String>,
    onChipsChange: (List<String>) -> Unit,
    placeholderText: String,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues =
        PaddingValues(
            vertical = BottariTheme.spacing.spaceSmall,
            horizontal = BottariTheme.spacing.spaceXSmall,
        ),
    textStyle: TextStyle = BottariTheme.typography.medium14.toTextStyle(),
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(contentPadding),
        horizontalArrangement = Arrangement.spacedBy(BottariTheme.spacing.space2xSmall),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(modifier = Modifier.width(BottariTheme.spacing.space2xSmall))

        leadingIcon?.let { leadingIcon() }

        Spacer(modifier = Modifier.width(BottariTheme.spacing.space2xSmall))

        chips.forEach { chip ->
            DeletableChip(
                text = chip,
                onClick = {},
                onDelete = {
                    val updated = chips.toMutableList().also { it.remove(chip) }
                    onChipsChange(updated)
                },
            )
        }

        BasicTextField(
            value = value,
            onValueChange = { new ->
                handleValueChange(
                    new = new,
                    onValueChange = onValueChange,
                    chips = chips,
                    onChipsChange = onChipsChange,
                )
            },
            decorationBox = { inner ->
                HashChipTextFieldDecorationBox(
                    value = value,
                    placeholderText = placeholderText,
                    textStyle = textStyle,
                    trailingIcon = trailingIcon,
                    inner = inner,
                )
            },
            maxLines = 1,
            keyboardActions = KeyboardActions(onSearch = { onSearch(value) }),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            textStyle = textStyle,
            modifier =
                Modifier.setupHashChipTextField(
                    value = value,
                    chips = chips,
                    onChipsChange = onChipsChange,
                ),
        )
    }
}

@Composable
private fun HashChipTextFieldDecorationBox(
    value: String,
    placeholderText: String,
    textStyle: TextStyle,
    trailingIcon: (@Composable () -> Unit)? = null,
    inner: @Composable () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Box(contentAlignment = Alignment.CenterStart) {
            if (value.isEmpty()) {
                Text(
                    text = placeholderText,
                    style = textStyle,
                    color = BottariTheme.colors.gray500,
                )
            }
            inner()
        }
        trailingIcon?.let { trailingIcon() }
    }
}

@Composable
private fun Modifier.setupHashChipTextField(
    value: String,
    chips: List<String>,
    onChipsChange: (List<String>) -> Unit,
): Modifier =
    this
        .wrapContentHeight()
        .onPreviewKeyEvent { event ->
            val isBackspace = event.key == Key.Backspace && event.type == KeyEventType.KeyDown
            if (isBackspace && value.isEmpty() && chips.isNotEmpty()) {
                val updated = chips.toMutableList().apply { removeAt(lastIndex) }
                onChipsChange(updated)
                return@onPreviewKeyEvent true
            }
            false
        }

private fun handleValueChange(
    new: String,
    onValueChange: (String) -> Unit,
    chips: List<String>,
    onChipsChange: (List<String>) -> Unit,
) {
    val (newTags, remain) = extractCompletedTags(new)
    if (newTags.isNotEmpty()) {
        val updated = chips.toMutableList().apply { addAll(newTags) }
        onChipsChange(updated)
    }
    onValueChange(remain)
}

private fun extractCompletedTags(input: String): Pair<List<String>, String> {
    val completeTagPattern = Regex("""(?:^|\s)(#[^\s]+)\s""")

    val found =
        completeTagPattern
            .findAll(input)
            .mapNotNull { it.groups[1]?.value }
            .toList()

    if (found.isEmpty()) return emptyList<String>() to input

    val remain =
        completeTagPattern
            .replace(input, " ")
            .replace(Regex("""\s{2,}"""), " ")
            .trimStart()
    return found to remain
}

@Preview
@Composable
private fun HashChipTextFieldPreview() {
    var text by rememberSaveable { mutableStateOf("") }
    val chips = remember { mutableStateListOf<String>() }
    var isFocused by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = Modifier.onFocusChanged { isFocused = it.isFocused },
    ) {
        HashChipTextField(
            value = text,
            onValueChange = { text = it },
            chips = chips,
            onChipsChange = { new ->
                val filtered = new.filter { it.length > 1 && it.startsWith('#') }
                chips.clear()
                chips.addAll(filtered)
            },
            placeholderText = "태그를 입력하세요... (예: #compose )",
            onSearch = {},
            modifier =
                Modifier
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp)),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "검색 아이콘",
                    tint = if (isFocused) BottariTheme.colors.black else BottariTheme.colors.gray500,
                )
            },
            trailingIcon = {
                if (text.isNotEmpty()) {
                    IconButton(
                        onClick = { text = "" },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "검색어 지우기",
                        )
                    }
                }
            },
        )
    }
}
