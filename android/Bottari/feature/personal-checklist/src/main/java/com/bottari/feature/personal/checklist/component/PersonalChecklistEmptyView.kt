package com.bottari.feature.personal.checklist.component

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.bottari.designsystem.component.BottariButton
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.core.ui.R
import com.bottari.core.ui.R as PresentationR

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
            painter = painterResource(PresentationR.drawable.ic_bottari_item_empty_view),
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
        BottariButton(
            onClick = onClickEdit,
            text = stringResource(R.string.checklist_empty_view_edit_btn_text),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PersonalChecklistEmptyViewPreview() {
    PersonalChecklistEmptyView(onClickEdit = {}, modifier = Modifier.fillMaxSize())
}
