package com.bottari.bottari.designsystem.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.bottari.designsystem.theme.BottariTheme.colors

@Immutable
data class BottariColorSystem(
    val transparent: Color = Color(0x00000000),
    val black: Color = Color(0xFF000000),
    val white: Color = Color(0xFFFFFFFF),
    val primary: Color = Color(0xFF0064FF),
    val primarySoft: Color = Color(0xFFEFF6FF),
    val red: Color = Color(0xFFFF0000),
    val redSoft: Color = Color(0xFFFF3B30),
    val green: Color = Color(0xFF22C55E),
    val purple: Color = Color(0xFFA855F7),
    val gray50: Color = Color(0xFFF8F8F8),
    val gray100: Color = Color(0xFFF2F2F5),
    val gray200: Color = Color(0xFFEEEEEE),
    val gray300: Color = Color(0xFFD1DEE8),
    val gray400: Color = Color(0xFFC4C4C4),
    val gray500: Color = Color(0xFFA6A6A6),
    val gray600: Color = Color(0xFF999999),
    val gray700: Color = Color(0xFF787878),
    val productTypePersonal: Color = Color(0xFF0064FF),
    val productTypeShared: Color = Color(0xFF22C55E),
    val productTypeAssigned: Color = Color(0xFFA855F7),
    val memberColors: List<Color> =
        listOf(
            Color(0xFF1F77B4),
            Color(0xFFFF7F0E),
            Color(0xFF2CA02C),
            Color(0xFFD62728),
            Color(0xFF9467BD),
            Color(0xFF8C564B),
            Color(0xFFE377C2),
            Color(0xFF7F7F7F),
            Color(0xFFBCBD22),
            Color(0xFF17BECF),
        ),
)

private val lightColorScheme = BottariColorSystem()

val LocalBottariColorSystem = staticCompositionLocalOf { lightColorScheme }

val LocalBottariBgColor = compositionLocalOf { lightColorScheme.gray50 }

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun BottariColorSystemPreview() {
    val colors = lightColorScheme
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier =
            Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
    ) {
        Text(
            text = "🎨 Bottari Color System",
            style = BottariTheme.typography.bold18.toTextStyle(),
            color = colors.black,
        )

        Spacer(modifier = Modifier.height(12.dp))

        ColorItem("transparent", colors.transparent)
        ColorItem("black", colors.black)
        ColorItem("white", colors.white)
        ColorItem("primary", colors.primary)
        ColorItem("primarySoft", colors.primarySoft)
        ColorItem("red", colors.red)
        ColorItem("redSoft", colors.redSoft)
        ColorItem("gray50", colors.gray50)
        ColorItem("gray100", colors.gray100)
        ColorItem("gray200", colors.gray200)
        ColorItem("gray300", colors.gray300)
        ColorItem("gray400", colors.gray400)
        ColorItem("gray500", colors.gray500)
        ColorItem("gray600", colors.gray600)
        ColorItem("gray700", colors.gray700)
        ColorItem("productTypePersonal", colors.productTypePersonal)
        ColorItem("productTypeShared", colors.productTypeShared)
        ColorItem("productTypeAssigned", colors.productTypeAssigned)
        colors.memberColors.forEachIndexed { index, color ->
            ColorItem("memberColor${index + 1}", color)
        }
    }
}

@Composable
private fun ColorItem(
    name: String,
    color: Color,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
    ) {
        val sampleShape = BottariTheme.shapes.radiusSmall
        Box(
            modifier =
                Modifier
                    .size(40.dp)
                    .background(color = color, shape = sampleShape)
                    .border(1.dp, colors.gray300, sampleShape),
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = name,
            style = BottariTheme.typography.medium14.toTextStyle(),
            color = colors.gray700,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = "#${color.toHex()}",
            style = BottariTheme.typography.medium12.toTextStyle(),
            color = colors.gray500,
        )
    }
}

private fun Color.toHex(): String {
    val intColor = this.toArgb()
    return String.format("0x%08X", intColor)
}
