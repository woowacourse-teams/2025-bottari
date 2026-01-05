package com.bottari.presentation.compose.edit.team.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.bottari.bottari.designsystem.component.BottariIconButton
import com.bottari.bottari.designsystem.component.BottariTopAppBar
import com.bottari.presentation.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamEditTopbar(
    title: String,
    isMemberScreen: Boolean,
    onBackClick: () -> Unit,
    onMemberClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BottariTopAppBar(
        modifier = modifier,
        title = title,
        navigationIcon = {
            BottariIconButton(onBackClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow),
                    contentDescription = "뒤로 가기",
                    modifier = Modifier.rotate(180f),
                )
            }
        },
        actions = {
            if (!isMemberScreen) {
                BottariIconButton(onMemberClick) {
                    Icon(
                        painter = painterResource(R.drawable.ic_team),
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
