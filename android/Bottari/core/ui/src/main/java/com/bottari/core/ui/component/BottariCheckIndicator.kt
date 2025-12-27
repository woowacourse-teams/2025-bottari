package com.bottari.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.bottari.bottari.designsystem.theme.BottariTheme

@Composable
fun BottariCheckIndicator(
    checkedQuantity: Int,
    totalQuantity: Int,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .clip(shape = RoundedCornerShape(16.dp))
                .background(BottariTheme.colors.gray400),
        contentAlignment = Alignment.CenterStart,
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth(generateIndicatorSize(checkedQuantity, totalQuantity))
                    .fillMaxHeight()
                    .clip(shape = RoundedCornerShape(16.dp))
                    .background(chooseBottariStateColor(checkedQuantity, totalQuantity)),
        )
    }
}

private fun generateIndicatorSize(
    checkedQuantity: Int,
    totalQuantity: Int,
): Float {
    if (totalQuantity <= 0) return 0F
    val safeChecked = checkedQuantity.coerceIn(0, totalQuantity)
    return safeChecked.toFloat() / totalQuantity.toFloat()
}
