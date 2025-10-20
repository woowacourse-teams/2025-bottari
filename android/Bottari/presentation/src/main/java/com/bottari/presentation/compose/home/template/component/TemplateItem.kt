package com.bottari.presentation.compose.home.template.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.component.BottariBox
import com.bottari.presentation.compose.common.theme.BottariTheme
import com.bottari.presentation.model.template.BottariTemplateHashtagUiModel
import com.bottari.presentation.model.template.BottariTemplateItemUiModel
import com.bottari.presentation.model.template.BottariTemplateUiModel

@Composable
fun TemplateItem(
    template: BottariTemplateUiModel,
    onClickHashtag: (BottariTemplateHashtagUiModel) -> Unit,
    modifier: Modifier = Modifier,
    iconButton: @Composable () -> Unit,
) {
    val hasTags = remember { template.hashtags.isNotEmpty() }
    val hashTagSectionPadding = if (hasTags) BottariTheme.spacing.spaceMedium else 0.dp

    BottariBox(
        modifier = modifier,
        contentPadding = PaddingValues(),
    ) {
        Column(modifier = Modifier.padding(BottariTheme.spacing.spaceMedium)) {
            TemplateItemHeader(
                title = template.title,
                description = template.description,
                iconButton = iconButton,
            )

            Spacer(modifier = Modifier.height(BottariTheme.spacing.spaceSmall))

            TemplateHashtagSection(
                hashtags = template.hashtags,
                onClickHashtag = onClickHashtag,
            )

            Spacer(modifier = Modifier.height(hashTagSectionPadding))

            TemplateItemFooter(
                author = template.author,
                takenCount = template.takenCount,
                itemCount = template.items.size,
            )
        }
    }
}

@Composable
private fun TemplateItemHeader(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    iconButton: @Composable () -> Unit,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = BottariTheme.typography.semiBold16.toTextStyle(),
                color = BottariTheme.colors.black,
            )
            Spacer(Modifier.height(BottariTheme.spacing.space2xSmall))
            Text(
                text = description,
                style = BottariTheme.typography.regular14.toTextStyle(),
                color = BottariTheme.colors.gray500,
            )
        }

        Spacer(modifier = Modifier.width(BottariTheme.spacing.spaceSmall))

        iconButton()
    }
}

@Composable
private fun TemplateHashtagSection(
    hashtags: List<BottariTemplateHashtagUiModel>,
    onClickHashtag: (BottariTemplateHashtagUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    val chipShape = remember { RoundedCornerShape(999.dp) }

    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(BottariTheme.spacing.space2xSmall),
        verticalArrangement = Arrangement.spacedBy(BottariTheme.spacing.spaceXSmall),
        itemVerticalAlignment = Alignment.CenterVertically,
    ) {
        hashtags.forEach { hashtag ->
            Box(
                contentAlignment = Alignment.Center,
                modifier =
                    Modifier
                        .clip(chipShape)
                        .background(
                            color = Color(0xFFEFF6FF),
                            shape = chipShape,
                        ).clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(color = BottariTheme.colors.primary),
                        ) { onClickHashtag(hashtag) }
                        .padding(
                            vertical = BottariTheme.spacing.space2xSmall,
                            horizontal = BottariTheme.spacing.spaceXSmall,
                        ),
            ) {
                Text(
                    text = "#${hashtag.name.replace(" ", "")}",
                    style = BottariTheme.typography.medium12.toTextStyle(),
                    color = BottariTheme.colors.primary,
                )
            }
        }
    }
}

@Composable
private fun TemplateItemFooter(
    author: String,
    takenCount: Int,
    itemCount: Int,
    modifier: Modifier = Modifier,
) {
    val contentColor = BottariTheme.colors.gray600

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .height(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(R.drawable.ic_person),
                contentDescription = null,
                tint = contentColor,
            )
            Spacer(Modifier.width(BottariTheme.spacing.space2xSmall))
            Text(
                text = author,
                style = BottariTheme.typography.medium12.toTextStyle(),
                color = contentColor,
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(R.drawable.ic_download),
                contentDescription = null,
                tint = contentColor,
            )
            Spacer(Modifier.width(BottariTheme.spacing.space2xSmall))
            Text(
                text = takenCount.toString(),
                style = BottariTheme.typography.medium12.toTextStyle(),
                color = contentColor,
            )
            Spacer(Modifier.width(BottariTheme.spacing.spaceSmall))
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.List,
                modifier = Modifier.size(height = 24.dp, width = 20.dp),
                contentDescription = null,
                tint = contentColor,
            )
            Spacer(Modifier.width(BottariTheme.spacing.space2xSmall))
            Text(
                text = itemCount.toString(),
                style = BottariTheme.typography.medium12.toTextStyle(),
                color = contentColor,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TemplateItemPreview() {
    val template =
        BottariTemplateUiModel(
            id = 1,
            title = "신입사원 온보딩 가이드",
            description = "새로운 직장 생활을 위한 완벽 가이드",
            author = "다이스",
            takenCount = 100024,
            items = List(10) { BottariTemplateItemUiModel(it.toLong(), "아이템 $it") },
            hashtags = List(3) { BottariTemplateHashtagUiModel(it.toLong(), "해시태그 $it") },
        )

    BottariTheme {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            TemplateItem(
                template = template,
                onClickHashtag = {},
            ) {
                TemplateItemIconButton(
                    type = TemplateItemType.MyTemplate,
                    onClick = {},
                )
            }

            TemplateItem(
                template = template,
                onClickHashtag = {},
            ) {
                TemplateItemIconButton(
                    type = TemplateItemType.Bookmark(true),
                    onClick = {},
                )
            }

            TemplateItem(
                template = template,
                onClickHashtag = {},
            ) {
                TemplateItemIconButton(
                    type = TemplateItemType.Bookmark(false),
                    onClick = {},
                )
            }
        }
    }
}
