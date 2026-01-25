package com.bottari.bottari.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bottari.bottari.designsystem.preview.ComponentPreview
import com.bottari.bottari.designsystem.theme.BottariTheme

/**
 * 상단에서 제목과 내비게이션, 액션을 제공하는 TopAppBar 컴포넌트입니다.
 *
 * @param title 가운데(혹은 왼쪽)에 보여줄 제목 텍스트.
 * @param modifier 앱바에 적용할 Modifier.
 * @param navigationIcon 좌측 내비게이션 아이콘 슬롯.
 * @param actions 우측 액션 슬롯.
 * @param centerAligned true면 CenterAlignedTopAppBar를 사용합니다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottariTopAppBar(
    title: String,
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    centerAligned: Boolean = true,
) {
    val colors =
        TopAppBarDefaults.topAppBarColors(
            containerColor = BottariTheme.colors.gray50,
            titleContentColor = BottariTheme.colors.black,
        )

    val titleContent =
        @Composable {
            Text(
                text = title,
                style = BottariTheme.typography.bold20.toTextStyle(),
            )
        }

    if (centerAligned) {
        CenterAlignedTopAppBar(
            title = titleContent,
            navigationIcon = navigationIcon ?: {},
            actions = actions,
            colors = colors,
            windowInsets = WindowInsets(top = 0.dp),
        )
    } else {
        TopAppBar(
            title = titleContent,
            navigationIcon = navigationIcon ?: {},
            actions = actions,
            colors = colors,
            windowInsets = WindowInsets(top = 0.dp),
        )
    }
}

@ComponentPreview
@Composable
private fun BottariTopAppBarPreview() {
    BottariTheme {
        Column {
            BottariTopAppBar(
                title = "보따리",
                navigationIcon = {
                    BottariIconButton(onClick = {}) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    BottariIconButton(onClick = {}) {
                        Icon(Icons.Default.Search, contentDescription = null)
                    }
                },
            )

            Spacer(
                Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(Color.Gray),
            )

            BottariTopAppBar(
                title = "보따리",
                centerAligned = false,
                navigationIcon = {
                    BottariIconButton(onClick = {}) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
            )
        }
    }
}
