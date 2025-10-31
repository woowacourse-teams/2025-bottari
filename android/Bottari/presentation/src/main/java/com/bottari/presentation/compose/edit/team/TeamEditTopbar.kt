package com.bottari.presentation.compose.edit.team

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.theme.BottariTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamEditTopbar(
    title: String,
    isMemberScreen: Boolean,
    onBackClick: () -> Unit,
    onMemberClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = title,
                style = BottariTheme.typography.bold20.toTextStyle(),
            )
        },
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = BottariTheme.colors.gray50,
                titleContentColor = BottariTheme.colors.black,
            ),
        navigationIcon = {
            IconButton(onBackClick) {
                Icon(
                    painterResource(R.drawable.ic_arrow),
                    contentDescription = null,
                    tint = BottariTheme.colors.black,
                    modifier = Modifier.rotate(180f),
                )
            }
        },
        actions = {
            if (!isMemberScreen) {
                IconButton(onMemberClick) {
                    Icon(
                        painterResource(R.drawable.ic_team),
                        contentDescription = null,
                        tint = BottariTheme.colors.black,
                    )
                }
            }
        },
    )
}
