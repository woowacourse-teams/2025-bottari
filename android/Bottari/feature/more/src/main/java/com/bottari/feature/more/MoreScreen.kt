package com.bottari.feature.more

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bottari.bottari.designsystem.component.BottariCard
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.core.ui.provider.LocalSnackbarHostState
import com.bottari.feature.more.component.NicknameBox

@Composable
fun MoreScreen(
    onNavigateToBrowser: (String) -> Unit,
    snackbarState: SnackbarHostState = LocalSnackbarHostState.current,
    viewModel: MoreViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                MoreUiEvent.FetchMemberInfoFailure -> snackbarState.showSnackbar("닉네임을 불러오지 못했어요")
                MoreUiEvent.InvalidNicknameRule -> snackbarState.showSnackbar("닉네임은 2글자에서 10글자 사이여야 해요")
                MoreUiEvent.SaveMemberNicknameFailure -> snackbarState.showSnackbar("닉네임을 변경하지 못했어요")
                MoreUiEvent.SaveMemberNicknameSuccess -> snackbarState.showSnackbar("닉네임을 변경했어요")
            }
        }
    }

    MoreBottariScreen(
        uiState = uiState,
        onChangeNickname = viewModel::updateNickname,
        onSaveNickname = viewModel::saveNickname,
        onClickPrivacyPolicy = { onNavigateToBrowser(BuildConfig.PRIVACY_POLICY_URL) },
        onClickUserFeedback = { onNavigateToBrowser(BuildConfig.USER_FEEDBACK_URL) },
    )
}

@Composable
private fun MoreBottariScreen(
    uiState: MoreUiState,
    onChangeNickname: (String) -> Unit,
    onSaveNickname: () -> Unit,
    onClickPrivacyPolicy: () -> Unit,
    onClickUserFeedback: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(
                    horizontal = BottariTheme.spacing.spaceLarge,
                    vertical = BottariTheme.spacing.spaceMedium,
                ),
        verticalArrangement = Arrangement.spacedBy(BottariTheme.spacing.spaceXSmall),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        NicknameBox(
            nickname = uiState.editingNickname,
            onChangeNickname = onChangeNickname,
            onSaveNickname = onSaveNickname,
            modifier = Modifier.fillMaxWidth(),
        )
        SettingItem(
            text = stringResource(R.string.setting_privacy_policy_title_text),
            onClick = onClickPrivacyPolicy,
        )
        SettingItem(
            text = stringResource(R.string.setting_user_feedback_title_text),
            onClick = onClickUserFeedback,
        )
        SettingVersionItem(text = stringResource(R.string.setting_version_text))
    }
}

@Composable
private fun SettingItem(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BottariCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
    ) {
        Text(
            text = text,
            modifier = Modifier.fillMaxWidth(),
            color = BottariTheme.colors.black,
            style = BottariTheme.typography.medium16.toTextStyle(),
        )
    }
}

@Composable
private fun SettingVersionItem(
    text: String,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    BottariCard(modifier = modifier.fillMaxWidth()) {
        Text(
            text = text,
            modifier = Modifier.align(Alignment.CenterStart),
            color = BottariTheme.colors.black,
            style = BottariTheme.typography.medium16.toTextStyle(),
        )
        Text(
            text =
                getAppVersionName(context)
                    ?: stringResource(R.string.setting_unknown_version_text),
            modifier = Modifier.align(Alignment.CenterEnd),
            color = BottariTheme.colors.black,
            style = BottariTheme.typography.medium16.toTextStyle(),
        )
    }
}

private fun getAppVersionName(context: Context): String? {
    val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    return packageInfo?.versionName
}

@Preview(showBackground = true)
@Composable
private fun MoreBottariScreenPreview() {
    BottariTheme {
        MoreBottariScreen(
            uiState = MoreUiState(editingNickname = "닉네임"),
            onChangeNickname = {},
            onSaveNickname = {},
            onClickPrivacyPolicy = {},
            onClickUserFeedback = {},
        )
    }
}
