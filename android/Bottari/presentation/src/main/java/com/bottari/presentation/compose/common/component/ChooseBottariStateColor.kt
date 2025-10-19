package com.bottari.presentation.compose.common.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.bottari.presentation.compose.common.theme.BottariTheme

@Composable
fun chooseBottariStateColor(
    checkedQuantity: Int,
    totalQuantity: Int,
): Color {
    require(totalQuantity >= 0) { "totalQuantity는 음수가 될 수 없습니다" }
    require(checkedQuantity >= 0) { "checkedQuantity는 음수가 될 수 없습니다" }
    require(checkedQuantity <= totalQuantity) { "checkedQuantity 는 totalQuantity를 초과할 수 없습니다" }

    if (checkedQuantity == 0) return BottariTheme.colors.gray400
    if (checkedQuantity == totalQuantity) return BottariTheme.colors.primary
    return BottariTheme.colors.red
}
