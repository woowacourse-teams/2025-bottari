package com.bottari.feature.personal.checklist.swipe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.bottari.designsystem.component.BottariButton
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.feature.personal.checklist.R
import com.bottari.core.ui.R as UIR

@Composable
fun CardStackEndScreen(
    modifier: Modifier = Modifier,
    isCompleted: Boolean,
    onClickButton: () -> Unit,
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.weight(1f))
        if (isCompleted) {
            SwipeCompletedView()
        } else {
            SwipeNotCompletedView()
        }
        Spacer(modifier = Modifier.weight(1f))
        BottariButton(
            onClick = onClickButton,
            text = stringResource(R.string.checklist_swipe_return_btn_text),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun SwipeCompletedView(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            painter = painterResource(UIR.drawable.ic_swipe_all_checked),
            contentDescription = stringResource(R.string.checklist_icon_swipe_all_checked_description),
            modifier = Modifier.size(60.dp),
        )
        Spacer(Modifier.height(BottariTheme.spacing.spaceLarge))
        Text(
            text = stringResource(R.string.checklist_swipe_complete_title),
            style = BottariTheme.typography.bold20.toTextStyle(),
        )
        Text(
            text = stringResource(R.string.checklist_swipe_complete_all_text),
            style = BottariTheme.typography.medium16.toTextStyle(),
        )
    }
}

@Composable
private fun SwipeNotCompletedView(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            painter = painterResource(UIR.drawable.ic_swipe_not_all_checked),
            contentDescription = stringResource(R.string.checklist_icon_swipe_partial_unchecked_description),
            modifier = Modifier.size(60.dp),
        )
        Spacer(Modifier.height(BottariTheme.spacing.spaceLarge))
        Text(
            text = stringResource(R.string.checklist_swipe_complete_title),
            style = BottariTheme.typography.bold20.toTextStyle(),
        )
        Text(
            text = stringResource(R.string.checklist_swipe_complete_not_all_text),
            style = BottariTheme.typography.medium16.toTextStyle(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CardStackEndScreenCompletedPreview() {
    CardStackEndScreen(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(BottariTheme.spacing.spaceXSmall),
        isCompleted = true,
        onClickButton = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun CardStackEndScreenNotCompletedPreview() {
    CardStackEndScreen(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(BottariTheme.spacing.spaceXSmall),
        isCompleted = false,
        onClickButton = {},
    )
}
