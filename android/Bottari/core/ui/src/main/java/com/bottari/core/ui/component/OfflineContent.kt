package com.bottari.core.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.bottari.designsystem.component.BottariButton
import com.bottari.bottari.designsystem.theme.BottariTheme

@Composable
fun OfflineContent(
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Default.WifiOff,
            contentDescription = "오프라인",
            modifier = Modifier.size(80.dp),
        )
        Spacer(modifier = Modifier.height(BottariTheme.spacing.spaceSmall))
        Text(
            text = "네트워크에 연결되어 있지 않아요",
            color = BottariTheme.colors.black,
            style = BottariTheme.typography.medium20.toTextStyle(),
        )
        Spacer(modifier = Modifier.height(BottariTheme.spacing.space2xSmall))
        Text(
            text = "연결 상태를 확인하고 다시 시도해주세요",
            color = BottariTheme.colors.gray700,
            style = BottariTheme.typography.medium16.toTextStyle(),
        )
        Spacer(modifier = Modifier.height(BottariTheme.spacing.spaceSmall))
        BottariButton(
            text = "다시 시도",
            onClick = onRetryClick,
        )
    }
}

@Composable
private fun RetryButton(
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TextButton(
        onClick = onRetryClick,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors =
            ButtonDefaults.buttonColors(
                containerColor = BottariTheme.colors.primary,
                contentColor = Color.White,
            ),
        elevation =
            ButtonDefaults.buttonElevation(
                defaultElevation = 2.dp,
                pressedElevation = 0.dp,
            ),
    ) {
        Text(
            text = "다시 시도",
            color = BottariTheme.colors.white,
            style = BottariTheme.typography.semiBold16.toTextStyle(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OfflineContentPreview() {
    BottariTheme {
        OfflineContent(
            onRetryClick = {},
        )
    }
}
