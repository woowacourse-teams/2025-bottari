package com.bottari.presentation.compose.team

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.core.ui.component.BottariBox
import com.bottari.presentation.R

@Composable
fun TeamStateCard(
    title: String,
    value: String,
    painter: Painter,
    color: Color,
    modifier: Modifier = Modifier,
) {
    BottariBox(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(horizontalAlignment = Alignment.Start) {
                Text(text = title, style = BottariTheme.typography.regular14.toTextStyle())
                Text(text = value, style = BottariTheme.typography.bold22.toTextStyle())
            }
            Spacer(Modifier.weight(1f))
            Box(
                modifier =
                    Modifier
                        .clip(CircleShape)
                        .size(48.dp)
                        .background(color = color.copy(0.2f)),
            ) {
                Icon(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.Center),
                    tint = color,
                )
            }
        }
    }
}

@Preview
@Composable
private fun TeamStateCardPreview() {
    TeamStateCard(
        title = "전체 완료율",
        value = "100%",
        painter = painterResource(R.drawable.ic_progress),
        color = BottariTheme.colors.primary,
        modifier = Modifier.width(200.dp),
    )
}
