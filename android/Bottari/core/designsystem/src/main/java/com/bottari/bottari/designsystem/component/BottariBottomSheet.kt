package com.bottari.bottari.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bottari.bottari.designsystem.preview.ComponentPreview
import com.bottari.bottari.designsystem.theme.BottariTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottariBottomSheet(
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = BottariTheme.colors.white,
        dragHandle = null,
        modifier = modifier,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(BottariTheme.spacing.spaceMedium),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = title,
                    style = BottariTheme.typography.bold22.toTextStyle(),
                )
                Spacer(Modifier.weight(1f))
                BottariIconButton(onClick = onDismissRequest) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "닫기",
                    )
                }
            }

            subtitle?.let {
                Text(
                    text = it,
                    style = BottariTheme.typography.medium14.toTextStyle(),
                    color = BottariTheme.colors.gray600,
                )
            }

            Spacer(Modifier.height(BottariTheme.spacing.spaceMedium))
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@ComponentPreview
@Composable
private fun BottariBottomSheetPreview() {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    BottariTheme {
        BottariBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {},
            title = "보따리 선택",
            subtitle = "등록할 보따리를 골라주세요",
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                repeat(3) { index ->
                    BottariButton(
                        text = "보따리 ${index + 1}",
                        onClick = {},
                        style = BottariButtonStyle.Secondary,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}
