package com.bottari.feature.template.create.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.bottari.designsystem.component.BottariCard
import com.bottari.bottari.designsystem.theme.BottariTheme

@Composable
fun TemplateDescriptionSection(
    description: String,
    onDescriptionChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    BottariCard(modifier) {
        Column {
            Text(
                text = "보따리 설명",
                style = BottariTheme.typography.bold18.toTextStyle(),
            )

            Spacer(modifier = Modifier.height(BottariTheme.spacing.space2xSmall))

            Text(
                text = "보따리에 대한 설명을 작성해주세요",
                style = BottariTheme.typography.medium12.toTextStyle(),
                color = BottariTheme.colors.gray500,
            )

            Spacer(modifier = Modifier.height(BottariTheme.spacing.spaceMedium))

            DescriptionInputContent(
                description = description,
                onDescriptionChange = onDescriptionChange,
            )
        }
    }
}

@Composable
private fun DescriptionInputContent(
    description: String,
    onDescriptionChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        CreateTemplateTextField(
            value = description,
            onValueChange = onDescriptionChange,
            placeholder = "작성하지 않으면 물건 목록 요약이 대신 보여져요",
            maxLines = 2,
            modifier = Modifier.height(80.dp),
        )
        Text(
            text = "${description.length}/30",
            style = BottariTheme.typography.medium12.toTextStyle(),
            color = BottariTheme.colors.gray700,
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(BottariTheme.spacing.spaceXSmall)
                    .background(
                        color = BottariTheme.colors.gray200,
                        shape = BottariTheme.shapes.pill,
                    ).padding(
                        vertical = BottariTheme.spacing.space2xSmall,
                        horizontal = BottariTheme.spacing.spaceXSmall,
                    ),
        )
    }
}

@Preview
@Composable
private fun TemplateDescriptionSectionPreview() {
    BottariTheme {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            TemplateDescriptionSection(
                description = "신입 사원 온보딩 가이드입니다.",
                onDescriptionChange = {},
            )

            TemplateDescriptionSection(
                description = "",
                onDescriptionChange = {},
            )
        }
    }
}
