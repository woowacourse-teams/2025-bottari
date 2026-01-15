package com.bottari.feature.template.create.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.bottari.designsystem.component.BottariCard
import com.bottari.bottari.designsystem.component.BottariChipGroup
import com.bottari.bottari.designsystem.component.BottariIconButton
import com.bottari.bottari.designsystem.component.BottariIconButtonTone
import com.bottari.bottari.designsystem.theme.BottariTheme

@Composable
fun TemplateHashtagSection(
    writingHashtag: String,
    onWritingHashtagChange: (String) -> Unit,
    hashtags: List<String>,
    canAddHashtag: Boolean,
    onClickAdd: () -> Unit,
    onUpdateHashtags: (List<String>) -> Unit,
    modifier: Modifier = Modifier,
) {
    BottariCard(modifier) {
        Column {
            Text(
                text = "해시태그",
                style = BottariTheme.typography.bold18.toTextStyle(),
            )

            Spacer(modifier = Modifier.height(BottariTheme.spacing.space2xSmall))

            Text(
                text = "보따리를 표현할 태그를 추가해 주세요",
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
                onChipsChange = onUpdateHashtags,
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

        BottariIconButton(
            onClick = onClickAdd,
            enabled = canAddHashtag,
            tone = BottariIconButtonTone.Primary,
        ) {
            Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
        }
    }
}

@Composable
private fun AddedHashTagContent(
    hashtags: List<String>,
    onChipsChange: (List<String>) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (hashtags.isNotEmpty()) Spacer(modifier = Modifier.height(BottariTheme.spacing.spaceSmall))
    BottariChipGroup(
        chips = hashtags.map { hashtag -> "#$hashtag" },
        onChipsChange = onChipsChange,
        modifier = modifier.fillMaxWidth(),
    )
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
                onUpdateHashtags = {},
            )

            TemplateHashtagSection(
                writingHashtag = "",
                onWritingHashtagChange = {},
                hashtags = emptyList(),
                canAddHashtag = false,
                onClickAdd = {},
                onUpdateHashtags = {},
            )
        }
    }
}
