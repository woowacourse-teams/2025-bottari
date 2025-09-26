package com.bottari.presentation.compose.home.more

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bottari.presentation.BuildConfig
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.component.BottariBox
import com.bottari.presentation.compose.common.theme.BottariTheme
import com.bottari.presentation.compose.home.more.component.NicknameBox
import com.bottari.presentation.view.home.more.MoreUiState
import com.bottari.presentation.view.home.more.MoreViewModel

@Composable
fun MoreBottariScreen(
    onNavigateToBrowser: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MoreViewModel =
        viewModel(
            factory = MoreViewModel.Factory(),
        ),
) {
    val uiState = viewModel.uiState.observeAsState().value ?: MoreUiState()

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(
                    horizontal = BottariTheme.spacing.spaceLarge,
                    vertical = BottariTheme.spacing.spaceXSmall,
                ),
        verticalArrangement = Arrangement.spacedBy(BottariTheme.spacing.spaceXSmall),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        NicknameBox(
            nickname = uiState.editingNickname,
            onChangeNickname = viewModel::updateNickname,
            onSaveNickname = viewModel::saveNickname,
            modifier = Modifier.fillMaxWidth(),
        )
        SettingItem(
            text = stringResource(R.string.setting_privacy_policy_title_text),
            onClick = { onNavigateToBrowser(BuildConfig.PRIVACY_POLICY_URL) },
        )
        SettingItem(
            text = stringResource(R.string.setting_user_feedback_title_text),
            onClick = { onNavigateToBrowser(BuildConfig.USER_FEEDBACK_URL) },
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
    BottariBox(modifier = modifier.fillMaxWidth()) {
        Text(
            text = text,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onClick)
                    .padding(vertical = BottariTheme.spacing.spaceSmall),
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
    BottariBox(modifier = modifier.fillMaxWidth()) {
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

@Preview
@Composable
private fun MoreBottariScreenPreview() {
    BottariTheme {
        MoreBottariScreen(onNavigateToBrowser = {})
    }
}
