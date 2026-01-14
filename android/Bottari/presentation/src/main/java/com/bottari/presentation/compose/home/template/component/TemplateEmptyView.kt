package com.bottari.presentation.compose.home.template.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.core.designsystem.R

@Composable
fun TemplateEmptyView(
    text: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_bottari),
            contentDescription = text,
            tint = BottariTheme.colors.gray500,
            modifier = Modifier.size(80.dp),
        )

        Spacer(modifier = Modifier.height(BottariTheme.spacing.spaceSmall))

        Text(
            text = text,
            style = BottariTheme.typography.semiBold16.toTextStyle(),
            color = BottariTheme.colors.gray500,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TemplateMyEmptyViewPreview() {
    BottariTheme {
        TemplateEmptyView(text = "항목이 존재하지 않습니다")
    }
}
