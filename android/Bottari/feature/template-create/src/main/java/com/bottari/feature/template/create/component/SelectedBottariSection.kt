package com.bottari.feature.template.create.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.bottari.designsystem.component.BottariCard
import com.bottari.bottari.designsystem.component.BottariInputChip
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.core.ui.extension.dashedBorder
import com.bottari.core.ui.extension.noRippleClickable

@Composable
fun SelectedBottariSection(
    selectedBottariTitle: String,
    selectedBottariItems: List<String>,
    isSelected: Boolean,
    onClickSelect: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BottariCard(modifier = modifier.noRippleClickable { onClickSelect() }) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = "보따리 정보",
                style = BottariTheme.typography.bold18.toTextStyle(),
            )

            Spacer(modifier = Modifier.height(BottariTheme.spacing.space2xSmall))

            Text(
                text = "여기를 눌러 등록할 보따리를 선택하세요",
                style = BottariTheme.typography.medium12.toTextStyle(),
                color = BottariTheme.colors.gray500,
            )

            Spacer(modifier = Modifier.height(BottariTheme.spacing.spaceMedium))

            if (isSelected) {
                SelectedBottariSectionContent(
                    selectedBottariTitle = selectedBottariTitle,
                    selectedBottariItems = selectedBottariItems,
                )
            } else {
                SelectedBottariSectionEmptyContent()
            }
        }
    }
}

@Composable
private fun SelectedBottariSectionContent(
    selectedBottariTitle: String,
    selectedBottariItems: List<String>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "보따리 제목",
                style = BottariTheme.typography.semiBold16.toTextStyle(),
            )
            Spacer(modifier = Modifier.height(BottariTheme.spacing.spaceXSmall))
            BottariInputChip(text = selectedBottariTitle)
        }

        Spacer(modifier = Modifier.height(BottariTheme.spacing.spaceMedium))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "물건 목록",
                style = BottariTheme.typography.semiBold16.toTextStyle(),
            )

            Spacer(modifier = Modifier.height(BottariTheme.spacing.spaceXSmall))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(BottariTheme.spacing.spaceXSmall),
                verticalArrangement = Arrangement.spacedBy(BottariTheme.spacing.space2xSmall),
            ) {
                selectedBottariItems.forEach { item ->
                    BottariInputChip(text = item)
                }
            }
        }
    }
}

@Composable
private fun SelectedBottariSectionEmptyContent(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier =
            modifier
                .fillMaxWidth()
                .dashedBorder(
                    color = BottariTheme.colors.gray500,
                    strokeWidth = 1.5.dp,
                ).padding(vertical = BottariTheme.spacing.space2xLarge),
    ) {
        Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription = null,
            tint = BottariTheme.colors.gray500,
        )

        Spacer(modifier = Modifier.height(BottariTheme.spacing.spaceXSmall))

        Text(
            text = "게시할 보따리를 선택해 주세요",
            style = BottariTheme.typography.medium16.toTextStyle(),
            color = BottariTheme.colors.gray500,
        )
    }
}

@Preview
@Composable
private fun SelectedBottariSectionPreview() {
    BottariTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            SelectedBottariSection(
                selectedBottariTitle = "",
                selectedBottariItems = emptyList(),
                onClickSelect = {},
                isSelected = false,
            )

            SelectedBottariSection(
                selectedBottariTitle = "신입 사원 온보딩 가이드",
                selectedBottariItems = List(10) { "아이템 $it" },
                onClickSelect = {},
                isSelected = true,
            )
        }
    }
}
