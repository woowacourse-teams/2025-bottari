package com.bottari.presentation.compose.template.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.presentation.compose.common.component.BottariBox
import com.bottari.presentation.compose.common.component.chip.DeletableChip
import com.bottari.presentation.compose.common.theme.BottariTheme

@Composable
fun TemplateHashtagSection(
    writingHashtag: String,
    onWritingHashtagChange: (String) -> Unit,
    hashtags: List<String>,
    canAddHashtag: Boolean,
    onClickAdd: () -> Unit,
    onClickDelete: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    BottariBox(modifier = modifier.fillMaxWidth()) {
        Column {
            Text(
                text = "해시태그",
                style = BottariTheme.typography.bold18.toTextStyle(),
            )

            Spacer(modifier = Modifier.height(BottariTheme.spacing.space2xSmall))

            Text(
                text = "보따리를 표현할 태그를 2개 이상 추가해 주세요",
                style = BottariTheme.typography.medium12.toTextStyle(),
                color = BottariTheme.colors.gray500,
            )

            Spacer(modifier = Modifier.height(BottariTheme.spacing.spaceMedium))

            HashtagInputSection(
                writingHashtag = writingHashtag,
                onWritingHashtagChange = onWritingHashtagChange,
                canAddHashtag = canAddHashtag,
                onClickAdd = onClickAdd,
            )

            AddedHashTagContent(
                hashtags = hashtags,
                onClickDelete = onClickDelete,
            )
        }
    }
}

@Composable
private fun HashtagInputSection(
    writingHashtag: String,
    onWritingHashtagChange: (String) -> Unit,
    canAddHashtag: Boolean,
    onClickAdd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.fillMaxWidth()) {
        CreateTemplateTextField(
            value = writingHashtag,
            onValueChange = onWritingHashtagChange,
            placeholder = "해시태그는 한글만 사용할 수 있습니다.",
            maxLines = 1,
            modifier = Modifier.weight(1f),
        )

        Spacer(modifier = Modifier.width(BottariTheme.spacing.spaceXSmall))

        IconButton(
            enabled = canAddHashtag,
            onClick = { onClickAdd() },
            shape = RoundedCornerShape(12.dp),
            colors =
                IconButtonDefaults.iconButtonColors(
                    containerColor = BottariTheme.colors.primary,
                    disabledContainerColor = BottariTheme.colors.gray200,
                    contentColor = BottariTheme.colors.white,
                    disabledContentColor = BottariTheme.colors.gray500,
                ),
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = null,
            )
        }
    }
}

@Composable
private fun AddedHashTagContent(
    hashtags: List<String>,
    onClickDelete: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (hashtags.isNotEmpty()) Spacer(modifier = Modifier.height(BottariTheme.spacing.spaceSmall))

    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(BottariTheme.spacing.spaceXSmall),
        verticalArrangement = Arrangement.spacedBy(BottariTheme.spacing.spaceXSmall),
    ) {
        hashtags.forEach { hashtag ->
            DeletableChip(
                text = "#$hashtag",
                onClick = { onClickDelete(hashtag) },
                onDelete = { onClickDelete(hashtag) },
                modifier = Modifier.height(32.dp),
                textStyle = BottariTheme.typography.medium12.toTextStyle(),
            )
        }
    }
}

@Preview
@Composable
private fun TemplateHashtagSectionPreview() {
    BottariTheme {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            TemplateHashtagSection(
                writingHashtag = "보따리",
                onWritingHashtagChange = {},
                hashtags = listOf("보따리", "선물"),
                canAddHashtag = true,
                onClickAdd = {},
                onClickDelete = {},
            )

            TemplateHashtagSection(
                writingHashtag = "",
                onWritingHashtagChange = {},
                hashtags = emptyList(),
                canAddHashtag = false,
                onClickAdd = {},
                onClickDelete = {},
            )
        }
    }
}
