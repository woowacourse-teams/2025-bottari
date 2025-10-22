package com.bottari.presentation.compose.home.template.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bottari.presentation.compose.common.modifier.noRippleClickable
import com.bottari.presentation.compose.common.modifier.topBottomFadingEdge
import com.bottari.presentation.compose.common.theme.BottariTheme
import com.bottari.presentation.model.template.BottariTemplateHashtagUiModel
import com.bottari.presentation.model.template.BottariTemplateUiModel

@Composable
fun TemplateColumn(
    type: TemplateItemType,
    templates: List<BottariTemplateUiModel>,
    listState: LazyListState,
    onClickDetail: (Long) -> Unit,
    onClickDelete: (Long) -> Unit,
    onClickBookmark: (Long) -> Unit,
    onClickHashtag: (BottariTemplateHashtagUiModel) -> Unit,
) {
    LazyColumn(
        state = listState,
        modifier =
            Modifier
                .fillMaxSize()
                .padding(horizontal = BottariTheme.spacing.spaceLarge)
                .topBottomFadingEdge(color = BottariTheme.colors.gray50, width = 8.dp),
        contentPadding = PaddingValues(vertical = BottariTheme.spacing.spaceSmall),
        verticalArrangement = Arrangement.spacedBy(BottariTheme.spacing.spaceSmall),
    ) {
        templates.ifEmpty {
            item {
                TemplateEmptyView(
                    text = "항목이 존재하지 않습니다",
                    modifier = Modifier.fillParentMaxSize(),
                )
            }
        }

        items(templates, key = { template -> template.id }) { template ->
            TemplateItem(
                template = template,
                onClickHashtag = onClickHashtag,
                modifier = Modifier.noRippleClickable { onClickDetail(template.id) },
                iconButton = {
                    TemplateItemIconButtonByTemplateItemType(
                        type = type,
                        template = template,
                        onClickDelete = onClickDelete,
                        onClickBookmark = onClickBookmark,
                    )
                },
            )
        }
    }
}


@Composable
private fun TemplateItemIconButtonByTemplateItemType(
    type: TemplateItemType,
    template: BottariTemplateUiModel,
    onClickDelete: (Long) -> Unit,
    onClickBookmark: (Long) -> Unit,
) {
    when (type) {
        is TemplateItemType.MyTemplate -> {
            TemplateItemIconButton(
                type = type,
                onClick = { onClickDelete(template.id) },
            )
        }

        is TemplateItemType.Bookmark -> {
            TemplateItemIconButton(
                type = type,
                onClick = { onClickBookmark(template.id) },
            )
        }
    }
}
