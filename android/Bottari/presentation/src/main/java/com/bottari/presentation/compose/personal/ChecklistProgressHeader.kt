package com.bottari.presentation.compose.personal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.component.BottariBox
import com.bottari.presentation.compose.common.component.BottariCheckIndicator
import com.bottari.presentation.compose.common.theme.BottariTheme

@Composable
fun ChecklistProgressHeader(
    checkedQuantity: Int,
    totalQuantity: Int,
) {
    val format =
        stringResource(
            R.string.checklist_current_status_items_count_text,
        )
    BottariBox(
        contentPadding =
            PaddingValues(
                vertical = BottariTheme.spacing.spaceXLarge,
                horizontal = BottariTheme.spacing.spaceMedium,
            ),
    ) {
        Column {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(text = "완료된 항목", style = BottariTheme.typography.medium20.toTextStyle())
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text =
                        format.format(
                            checkedQuantity,
                            totalQuantity,
                        ),
                    style = BottariTheme.typography.medium20.toTextStyle(),
                )
            }
            Spacer(modifier = Modifier.height(BottariTheme.spacing.spaceMedium))
            BottariCheckIndicator(
                checkedQuantity = checkedQuantity,
                totalQuantity = totalQuantity,
                modifier = Modifier.height(10.dp),
            )
        }
    }
}

@Preview
@Composable
private fun ChecklistProgressHeaderPreview() {
    ChecklistProgressHeader(checkedQuantity = 4, totalQuantity = 7)
}