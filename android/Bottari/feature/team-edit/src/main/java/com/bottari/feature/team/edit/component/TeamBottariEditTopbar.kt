package com.bottari.feature.team.edit.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.bottari.bottari.designsystem.component.BottariIconButton
import com.bottari.bottari.designsystem.component.BottariTopAppBar
import com.bottari.core.ui.R as UIR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamEditTopbar(
    title: String,
    isMemberScreen: Boolean,
    onBackClick: () -> Unit,
    onMemberClick: () -> Unit,
) {
    BottariTopAppBar(
        title = title,
        navigationIcon = {
            BottariIconButton(onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "뒤로 가기",
                )
            }
        },
        actions = {
            if (!isMemberScreen) {
                BottariIconButton(onMemberClick) {
                    Icon(
                        painter = painterResource(UIR.drawable.ic_team),
                        contentDescription = "팀 멤버 편집",
                    )
                }
            }
        },
    )
}

@Preview
@Composable
private fun TeamEditTopbarPreview() {
    TeamEditTopbar(
        title = "보따리 이름",
        isMemberScreen = false,
        onBackClick = {},
        onMemberClick = {},
    )
}
