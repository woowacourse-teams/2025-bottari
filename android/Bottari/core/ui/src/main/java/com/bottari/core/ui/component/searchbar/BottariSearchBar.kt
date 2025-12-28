package com.bottari.core.ui.component.searchbar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.bottari.designsystem.component.BottariIconButton
import com.bottari.bottari.designsystem.extension.dropShadow
import com.bottari.bottari.designsystem.theme.BottariTheme

@Composable
fun BottariSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholderText: String,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    colors: TextFieldColors = defaultBottariSearchBarColors(),
    textStyle: TextStyle = TextStyle(),
) {
    var isFocused by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier =
            modifier
                .onFocusChanged { isFocused = it.isFocused }
                .fillMaxWidth()
                .dropShadow(shape, offsetY = 0.8.dp)
                .background(
                    color = Color.White,
                    shape = shape,
                )
                .border(
                    width = 0.8.dp,
                    color = if (isFocused) BottariTheme.colors.primary else Color.LightGray,
                    shape = shape,
                ),
    ) {
        TextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = {
                Text(
                    text = placeholderText,
                    style = textStyle,
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "검색 아이콘",
                )
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    BottariIconButton(onClick = { onQueryChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "검색어 지우기",
                        )
                    }
                }
            },
            singleLine = true,
            textStyle = textStyle,
            colors = colors,
            modifier = Modifier.fillMaxWidth(),
            keyboardActions = KeyboardActions(onSearch = { onSearch(query) }),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        )
    }
}

@Composable
fun defaultBottariSearchBarColors(): TextFieldColors =
    TextFieldDefaults.colors(
        cursorColor = Color.DarkGray,
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black,
        focusedPlaceholderColor = Color.Gray,
        unfocusedPlaceholderColor = Color.Gray,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        focusedLeadingIconColor = Color.Black,
        unfocusedLeadingIconColor = Color.LightGray,
        focusedTrailingIconColor = Color.Black,
        unfocusedTrailingIconColor = Color.LightGray,
    )

@Preview(showBackground = true)
@Composable
private fun BottariSearchBarPreview() {
    var state by remember { mutableStateOf("") }
    BottariSearchBar(
        query = state,
        onQueryChange = { state = it },
        placeholderText = "제목이나 해시태그를 입력하세요",
        onSearch = { state = "" },
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(12.dp),
    )
}
