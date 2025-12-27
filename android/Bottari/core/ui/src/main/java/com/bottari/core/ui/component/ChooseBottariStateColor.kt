package com.bottari.core.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.bottari.bottari.designsystem.theme.BottariTheme

@Composable
fun chooseBottariStateColor(
    checkedQuantity: Int,
    totalQuantity: Int,
): Color {
    if (checkedQuantity == 0) return BottariTheme.colors.gray400
    if (checkedQuantity == totalQuantity) return BottariTheme.colors.primary
    return BottariTheme.colors.red
}
