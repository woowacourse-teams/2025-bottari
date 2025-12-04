package com.bottari.presentation.compose.personal.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.core.designsystem.component.BottariBox
import com.bottari.core.designsystem.theme.BottariTheme
import com.bottari.presentation.R

@Composable
fun PersonalChecklistEmptyView(
    onClickEdit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_bottari_item_empty_view),
            contentDescription = stringResource(R.string.bottari_icon_item_empty_description),
            tint = BottariTheme.colors.gray500,
            modifier = Modifier.size(80.dp),
        )
        Spacer(Modifier.height(BottariTheme.spacing.spaceSmall))
        Text(
            text = stringResource(R.string.checklist_empty_view_title_text),
            style = BottariTheme.typography.bold20.toTextStyle(),
            color = BottariTheme.colors.gray500,
        )
        Spacer(Modifier.height(BottariTheme.spacing.spaceSmall))
        Text(
            text = stringResource(R.string.checklist_empty_view_description_text),
            textAlign = TextAlign.Center,
            style = BottariTheme.typography.medium16.toTextStyle(),
            color = BottariTheme.colors.gray500,
        )
        Spacer(Modifier.height(BottariTheme.spacing.spaceLarge))
        BottariBox(
            modifier =
                Modifier
                    .background(BottariTheme.colors.primary)
                    .clickable(onClick = onClickEdit),
            contentPadding =
                PaddingValues(
                    horizontal = BottariTheme.spacing.spaceLarge,
                    vertical = BottariTheme.spacing.spaceSmall,
                ),
        ) {
            Text(
                text = stringResource(R.string.checklist_empty_view_edit_btn_text),
                style = BottariTheme.typography.bold20.toTextStyle(),
                color = BottariTheme.colors.white,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PersonalChecklistEmptyViewPreview() {
    PersonalChecklistEmptyView(onClickEdit = {}, modifier = Modifier.fillMaxSize())
}
