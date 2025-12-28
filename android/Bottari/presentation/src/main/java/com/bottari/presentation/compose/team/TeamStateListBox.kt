package com.bottari.presentation.compose.team

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.presentation.R

@Composable
fun TeamStateListBox(
    items: List<String>,
    text: String,
    painter: Painter,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .clip(BottariTheme.shapes.radiusLarge)
                .background(BottariTheme.colors.white)
                .background(color.copy(0.1f))
                .fillMaxWidth()
                .padding(16.dp),
    ) {
        Column {
            Row {
                Icon(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = color,
                )
                Spacer(Modifier.width(BottariTheme.spacing.space2xSmall))
                Text(
                    text = text,
                    style = BottariTheme.typography.semiBold16.toTextStyle(),
                    color = color,
                )
            }
            if (items.isNotEmpty()) Spacer(Modifier.height(BottariTheme.spacing.spaceXSmall))
            FlowRow {
                items.forEach { item ->
                    Text(
                        text = item,
                        style = BottariTheme.typography.medium12.toTextStyle(),
                        modifier =
                            Modifier
                                .padding(2.dp)
                                .clip(BottariTheme.shapes.radiusLarge)
                                .background(BottariTheme.colors.white)
                                .padding(
                                    horizontal = BottariTheme.spacing.spaceXSmall,
                                    vertical = BottariTheme.spacing.space2xSmall,
                                ),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun TeamStateListBoxPreview() {
    TeamStateListBox(
        listOf("하나", "둘", "셋", "넷", "다섯"),
        "해당 물건을 챙겼습니다",
        painterResource(id = R.drawable.ic_bottari_item_empty_view),
        color = BottariTheme.colors.primary,
        modifier = Modifier.width(300.dp),
    )
}
