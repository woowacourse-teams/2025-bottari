package com.bottari.presentation.compose.personal.checklist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.component.BottariBox
import com.bottari.presentation.compose.common.theme.BottariTheme

@Composable
fun CardStackEndScreen(
    modifier: Modifier = Modifier,
    isComplete: Boolean,
    onClickButton: () -> Unit,
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.weight(1f))
        if (isComplete) {
            SwipeComplete()
        } else {
            SwipeNotComplete()
        }
        Spacer(modifier = Modifier.weight(1f))
        BottariBox(
            modifier =
                Modifier
                    .background(BottariTheme.colors.primary)
                    .fillMaxWidth()
                    .height(70.dp)
                    .clickable(onClick = onClickButton),
        ) {
            Text(
                text = stringResource(R.string.checklist_swipe_return_btn_text),
                style = BottariTheme.typography.medium20.toTextStyle(),
                color = BottariTheme.colors.white,
                modifier = Modifier.align(Alignment.Center),
            )
        }
    }
}

@Composable
fun SwipeComplete(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_swipe_all_checked),
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
fun SwipeNotComplete(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_swipe_not_all_checked),
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
fun CardStackEndScreenPreview() {
    CardStackEndScreen(
        modifier = Modifier.fillMaxSize().padding(BottariTheme.spacing.spaceXSmall),
        isComplete = true,
        onClickButton = {},
    )
}
