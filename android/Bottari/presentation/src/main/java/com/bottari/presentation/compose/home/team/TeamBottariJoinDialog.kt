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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.component.BottariBox
import com.bottari.presentation.compose.common.theme.BottariTheme

@Composable
fun TeamBottariJoinDialog(
    text: String,
    onChangeText: (String) -> Unit,
    isClickable : Boolean,
    onClick: () -> Unit,
) {
    BottariBox(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp),
    ) {
        Column {
            Text(text = "팀 보따리 코드 입력", style = BottariTheme.typography.medium16.toTextStyle())
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = "공유받은 팀 보따리 코드를 입력해주세요.",
                style = BottariTheme.typography.regular12.toTextStyle(),
            )
            TextField(
                modifier =
                    Modifier
                        .padding(top = 12.dp)
                        .height(48.dp)
                        .fillMaxWidth(),
                colors =
                    TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        unfocusedContainerColor = colorResource(R.color.gray_200),
                        focusedContainerColor = colorResource(R.color.gray_200),
                    ),
                shape = RoundedCornerShape(8.dp),
                value = text,
                onValueChange = { text -> onChangeText(text) },
                singleLine = true,
            )
            Button(
                modifier =
                    Modifier
                        .padding(top = 16.dp)
                        .height(48.dp)
                        .fillMaxWidth(),
                onClick = onClick,
                enabled = isClickable,
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.primary),
                        disabledContainerColor = colorResource(R.color.gray_400),
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
fun TeamBottariJoinDialogContentPreview() {
    TeamBottariJoinDialog(text = "", onChangeText = {}, onClick = {} , isClickable = true)
}
