package com.bottari.presentation.compose.home.template.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.presentation.R

@Composable
fun CreateTemplateFAB(
    onClickAdd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FloatingActionButton(
        onClick = onClickAdd,
        shape = BottariTheme.shapes.radiusMedium,
        containerColor = BottariTheme.colors.primary,
        contentColor = BottariTheme.colors.white,
        elevation =
            FloatingActionButtonDefaults.elevation(
                defaultElevation = 1.dp,
                pressedElevation = 0.dp,
                hoveredElevation = 0.dp,
                focusedElevation = 0.dp,
            ),
        modifier = modifier,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_plus),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = BottariTheme.colors.white,
        )
    }
}

@Preview
@Composable
private fun CreateTemplateFABPreview() {
    BottariTheme {
        CreateTemplateFAB(onClickAdd = {})
    }
}
