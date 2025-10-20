package com.bottari.presentation.compose.edit.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.theme.BottariTheme

@Composable
fun BottariRenameTextField(
    title: String,
    onTitleChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    TextField(
        value = title,
        onValueChange = { newTitle -> onTitleChange(newTitle) },
        modifier = modifier,
        textStyle = BottariTheme.typography.regular14.toTextStyle(),
        placeholder = {
            Text(
                text = stringResource(R.string.bottari_rename_dialog_hint_text),
                color = BottariTheme.colors.gray500,
                style = BottariTheme.typography.regular14.toTextStyle(),
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors =
            TextFieldDefaults.colors(
                focusedContainerColor = BottariTheme.colors.gray200,
                unfocusedContainerColor = BottariTheme.colors.gray200,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
    )
}

@Preview
@Composable
private fun BottariRenameTextFieldPreview() {
    var title by remember { mutableStateOf("보따리 제목") }

    BottariTheme {
        BottariRenameTextField(
            title = title,
            onTitleChange = { newTitle -> title = newTitle },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(color = BottariTheme.colors.gray200),
        )
    }
}
