package com.bottari.presentation.compose.edit.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.theme.BottariTheme

@Composable
fun BottariRenameButton(
    onClick: () -> Unit,
    isClickable: Boolean,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = isClickable,
        shape = RoundedCornerShape(12.dp),
        colors =
            ButtonDefaults.buttonColors(
                containerColor = BottariTheme.colors.primary,
                disabledContainerColor = BottariTheme.colors.gray400,
                contentColor = Color.White,
            ),
    ) {
        Text(
            text = stringResource(R.string.bottari_rename_dialog_btn_text),
            style = BottariTheme.typography.semiBold16.toTextStyle(),
            color = BottariTheme.colors.white,
        )
    }
}

@Preview
@Composable
private fun BottariRenameButtonPreview() {
    BottariTheme {
        BottariRenameButton(
            onClick = {},
            isClickable = true,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
        )
    }
}
