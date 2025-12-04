package com.bottari.presentation.compose.home.bottari.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.core.designsystem.theme.BottariTheme
import com.bottari.presentation.R

@Composable
fun MyBottariEmptyView() {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painter = painterResource(id = R.drawable.ic_bottari),
            contentDescription = stringResource(R.string.bottari_icon_home_description),
            modifier = Modifier.size(80.dp),
            tint = BottariTheme.colors.gray500,
        )
        Spacer(modifier = Modifier.height(BottariTheme.spacing.spaceSmall))
        Text(
            text = stringResource(R.string.bottari_home_empty_view_title_text),
            color = BottariTheme.colors.gray500,
            style = BottariTheme.typography.bold20.toTextStyle(),
        )
        Spacer(modifier = Modifier.height(BottariTheme.spacing.spaceSmall))
        Text(
            text = stringResource(R.string.bottari_home_empty_view_description_text),
            textAlign = TextAlign.Center,
            color = BottariTheme.colors.gray500,
            style = BottariTheme.typography.medium16.toTextStyle(),
        )
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
fun MyBottariEmptyViewPreview() {
    MyBottariEmptyView()
}
