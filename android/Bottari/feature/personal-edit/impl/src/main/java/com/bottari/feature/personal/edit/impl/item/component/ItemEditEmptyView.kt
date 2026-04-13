package com.bottari.feature.personal.edit.impl.item.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.feature.personal.edit.impl.R
import com.bottari.core.ui.R as UIR

@Composable
fun ItemEditEmptyView(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(UIR.drawable.ic_bottari_item_empty_view),
            contentDescription = stringResource(UIR.string.bottari_icon_item_empty_description),
            modifier = Modifier.size(80.dp),
            alpha = 0.25f,
        )
        Spacer(modifier = Modifier.height(BottariTheme.spacing.spaceSmall))
        Text(
            text = stringResource(R.string.bottari_personal_item_empty_view_title),
            color = BottariTheme.colors.gray500,
            style = BottariTheme.typography.bold20.toTextStyle(),
        )
        Spacer(modifier = Modifier.height(BottariTheme.spacing.spaceSmall))
        Text(
            text = stringResource(R.string.bottari_personal_item_empty_view_text),
            color = BottariTheme.colors.gray500,
            style = BottariTheme.typography.medium16.toTextStyle(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ItemEditEmptyViewPreview() {
    BottariTheme {
        ItemEditEmptyView()
    }
}
