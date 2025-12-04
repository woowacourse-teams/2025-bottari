package com.bottari.core.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.bottari.presentation.R

val Pretendard =
    FontFamily(
        Font(R.font.pretendard_regular, FontWeight.W400),
        Font(R.font.pretendard_medium, FontWeight.W500),
        Font(R.font.pretendard_semibold, FontWeight.W600),
        Font(R.font.pretendard_bold, FontWeight.W700),
    )

@Immutable
data class BottariTextStyle(
    val fontFamily: FontFamily = Pretendard,
    val fontWeight: FontWeight = FontWeight.Medium,
    val fontSize: Dp = Dp.Unspecified,
    val lineHeight: Dp = Dp.Unspecified,
    val letterSpacing: TextUnit = 0.em,
    val color: Color = Color.Unspecified,
    val textAlign: TextAlign = TextAlign.Start,
) {
    @Composable
    fun toTextStyle(): TextStyle =
        TextStyle(
            fontFamily = fontFamily,
            fontWeight = fontWeight,
            fontSize = with(LocalDensity.current) { fontSize.toSp() },
            lineHeight = with(LocalDensity.current) { lineHeight.toSp() },
            letterSpacing = letterSpacing,
            color = color,
            textAlign = textAlign,
        )

    companion object {
        @Stable
        val Default = BottariTextStyle()
    }
}

@Immutable
data class BottariTypography(
    val bold40: BottariTextStyle =
        BottariTextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.W700,
            lineHeight = 40.dp,
            fontSize = 40.dp,
        ),
    val bold32: BottariTextStyle =
        BottariTextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.W700,
            lineHeight = 32.dp,
            fontSize = 32.dp,
        ),
    val bold22: BottariTextStyle =
        BottariTextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.W700,
            lineHeight = 22.dp,
            fontSize = 22.dp,
        ),
    val bold20: BottariTextStyle =
        BottariTextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.W700,
            lineHeight = 20.dp,
            fontSize = 20.dp,
        ),
    val bold18: BottariTextStyle =
        BottariTextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.W700,
            lineHeight = 18.dp,
            fontSize = 18.dp,
        ),
    val bold12: BottariTextStyle =
        BottariTextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.W700,
            lineHeight = 12.dp,
            fontSize = 12.dp,
        ),
    val semiBold24: BottariTextStyle =
        BottariTextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.W600,
            lineHeight = 24.dp,
            fontSize = 24.dp,
        ),
    val semiBold20: BottariTextStyle =
        BottariTextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.W600,
            lineHeight = 20.dp,
            fontSize = 20.dp,
        ),
    val semiBold18: BottariTextStyle =
        BottariTextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.W600,
            lineHeight = 18.dp,
            fontSize = 18.dp,
        ),
    val semiBold16: BottariTextStyle =
        BottariTextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.W600,
            lineHeight = 16.dp,
            fontSize = 16.dp,
        ),
    val medium36: BottariTextStyle =
        BottariTextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.W500,
            lineHeight = 36.dp,
            fontSize = 36.dp,
        ),
    val medium20: BottariTextStyle =
        BottariTextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.W500,
            lineHeight = 20.dp,
            fontSize = 20.dp,
        ),
    val medium16: BottariTextStyle =
        BottariTextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.W500,
            lineHeight = 16.dp,
            fontSize = 16.dp,
        ),
    val medium14: BottariTextStyle =
        BottariTextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.W500,
            lineHeight = 14.dp,
            fontSize = 14.dp,
        ),
    val medium12: BottariTextStyle =
        BottariTextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.W500,
            lineHeight = 12.dp,
            fontSize = 12.dp,
        ),
    val regular24: BottariTextStyle =
        BottariTextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.W400,
            lineHeight = 24.dp,
            fontSize = 24.dp,
        ),
    val regular16: BottariTextStyle =
        BottariTextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.W400,
            lineHeight = 16.dp,
            fontSize = 16.dp,
        ),
    val regular14: BottariTextStyle =
        BottariTextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.W400,
            lineHeight = 14.dp,
            fontSize = 14.dp,
        ),
    val regular12: BottariTextStyle =
        BottariTextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.W400,
            lineHeight = 12.dp,
            fontSize = 12.dp,
        ),
)

val LocalBottariTypographySystem = staticCompositionLocalOf { BottariTypography() }
