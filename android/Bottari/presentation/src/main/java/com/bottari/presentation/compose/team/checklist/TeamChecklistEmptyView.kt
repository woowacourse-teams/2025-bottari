package com.bottari.presentation.compose.team.checklist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.theme.BottariTheme

@Composable
fun TeamChecklistEmptyView(modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(BottariTheme.spacing.spaceSmall))
        Icon(
            painter = painterResource(R.drawable.ic_bottari_item_empty_view),
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = BottariTheme.colors.gray500,
        )
        Text("챙길 물건이 없어요", color = BottariTheme.colors.gray500, style = BottariTheme.typography.semiBold16.toTextStyle())
        Spacer(Modifier.height(BottariTheme.spacing.spaceSmall))
    }
}

@Preview
@Composable
private fun TeamChecklistEmptyViewPreview() {
    TeamChecklistEmptyView()
}
