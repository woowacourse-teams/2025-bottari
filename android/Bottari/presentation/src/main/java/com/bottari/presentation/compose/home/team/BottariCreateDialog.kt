package com.bottari.presentation.compose.home.team

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.presentation.compose.common.component.BottariBox
import com.bottari.presentation.compose.common.theme.BottariTheme

@Composable
fun BottariCreateDialog(
    text: String,
    onChangeText: (String) -> Unit,
    isClickable: Boolean,
    onClick: () -> Unit,
    placeholder: String = "",
) {
    BottariBox(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = BottariTheme.spacing.space2xLarge),
    ) {
        Column {
            Text(text = "보따리 이름 작성", style = BottariTheme.typography.medium16.toTextStyle())
            Text(
                modifier = Modifier.padding(top = BottariTheme.spacing.space2xSmall),
                text = "이름은 언제든지 수정할 수 있어요.",
                style = BottariTheme.typography.regular12.toTextStyle(),
            )
            TextField(
                modifier =
                    Modifier
                        .padding(top = BottariTheme.spacing.spaceSmall)
                        .height(48.dp)
                        .fillMaxWidth(),
                colors =
                    TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        unfocusedContainerColor = BottariTheme.colors.gray200,
                        focusedContainerColor = BottariTheme.colors.gray200,
                    ),
                shape = RoundedCornerShape(8.dp),
                value = text,
                onValueChange = { text -> onChangeText(text) },
                placeholder = { Text(text = placeholder, style = BottariTheme.typography.medium16.toTextStyle()) },
                singleLine = true,
            )
            Button(
                modifier =
                    Modifier
                        .padding(top = BottariTheme.spacing.spaceMedium)
                        .height(48.dp)
                        .fillMaxWidth(),
                onClick = onClick,
                enabled = isClickable,
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = BottariTheme.colors.primary,
                        disabledContainerColor = BottariTheme.colors.gray400,
                        contentColor = Color.White,
                    ),
                shape = RoundedCornerShape(12.dp),
            ) {
                Text("생성하기", style = BottariTheme.typography.semiBold16.toTextStyle())
            }
        }
    }
}

@Preview
@Composable
fun MyCustomDialogContentPreview() {
    BottariCreateDialog(text = "", onChangeText = {}, onClick = {}, isClickable = true, placeholder = "새 보따리")
}
