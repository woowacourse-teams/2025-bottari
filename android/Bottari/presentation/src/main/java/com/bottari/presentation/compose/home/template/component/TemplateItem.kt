package com.bottari.presentation.compose.home.template.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
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
import com.bottari.presentation.compose.common.component.CollapsedListLine
import com.bottari.presentation.compose.common.theme.BottariTheme
import com.bottari.presentation.model.template.BottariTemplateItemUiModel
import com.bottari.presentation.model.template.BottariTemplateUiModel

@Composable
fun TemplateItem(
    template: BottariTemplateUiModel,
    modifier: Modifier = Modifier,
) {
    BottariBox(
        modifier = modifier,
        contentPadding = PaddingValues(),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            TemplateItemHeader(
                title = template.title,
                items = template.items.map { it.name },
            )

            Spacer(modifier = Modifier.height(BottariTheme.spacing.spaceSmall))
            HorizontalDivider(color = BottariTheme.colors.gray200)
            Spacer(modifier = Modifier.height(BottariTheme.spacing.spaceXSmall))

            TemplateItemFooter(
                author = template.author,
                takenCount = template.takenCount,
            )
        }
    }
}

@Composable
private fun TemplateItemHeader(
    title: String,
    items: List<String>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(BottariTheme.spacing.spaceSmall),
        ) {
            Text(
                text = title,
                style = BottariTheme.typography.semiBold16.toTextStyle(),
                color = BottariTheme.colors.black,
            )
            CollapsedListLine(
                items = items,
                textStyle = BottariTheme.typography.regular12.toTextStyle(),
            )
        }
    }
}

@Composable
private fun TemplateItemFooter(
    author: String,
    takenCount: Int,
    modifier: Modifier = Modifier,
) {
    val contentColor = BottariTheme.colors.gray600

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_person),
            contentDescription = null,
            tint = contentColor,
        )
        Spacer(Modifier.width(BottariTheme.spacing.space2xSmall))
        Text(
            text = author,
            style = BottariTheme.typography.regular12.toTextStyle(),
            color = contentColor,
        )
        Spacer(Modifier.width(BottariTheme.spacing.spaceXSmall))
        Icon(
            painter = painterResource(R.drawable.ic_download),
            contentDescription = null,
            tint = contentColor,
        )
        Spacer(Modifier.width(BottariTheme.spacing.space2xSmall))
        Text(
            text = stringResource(R.string.template_taken_count_prefix, takenCount),
            style = BottariTheme.typography.regular12.toTextStyle(),
            color = contentColor,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TemplateItemPreview() {
    val template =
        BottariTemplateUiModel(
            id = 1,
            title = "우테코출근보따리글자수열다섯자",
            author = "다이스",
            takenCount = 100024,
            items = List(10) { BottariTemplateItemUiModel(it.toLong(), "아이템 $it") },
        )

    BottariTheme {
        Box(modifier = Modifier.padding(12.dp)) {
            TemplateItem(template = template)
        }
    }
}
