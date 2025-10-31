package com.bottari.presentation.view.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.net.toUri
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseActivity
import com.bottari.presentation.common.extension.showSnackbar
import com.bottari.presentation.compose.home.ComposeHomeActivity
import com.bottari.presentation.databinding.ActivityMainBinding
import com.bottari.presentation.util.DeeplinkHelper.getInviteCode
import com.bottari.presentation.util.DeeplinkHelper.validateUri
import com.bottari.presentation.util.PermissionUtil
import com.bottari.presentation.util.PermissionUtil.hasAllRuntimePermissions
import com.bottari.presentation.util.PermissionUtil.hasExactAlarmPermission
import com.bottari.presentation.util.PermissionUtil.requiredPermissions
import com.bottari.presentation.view.common.PermissionDescriptionDialog
import com.bottari.presentation.view.common.alert.CustomAlertDialog
import com.bottari.presentation.view.common.alert.DialogListener
import com.bottari.presentation.view.common.alert.DialogPresetType
import com.bottari.presentation.view.invite.InviteActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private val viewModel: MainViewModel by viewModels()
    private val permissionLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
        ) { showExactAlarmSettingsDialog() }
    private var isReady: Boolean = false
    private var isNavigatedToSettings: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition { !isReady }
        }
        super.onCreate(savedInstanceState)
        setupObserver()
    }

    override fun onResume() {
        super.onResume()
        if (isNavigatedToSettings) {
            isNavigatedToSettings = false
            viewModel.checkRegisteredMember()
        }
    }

    private fun setupObserver() {
        collectWithLifecycle(viewModel.uiState) { state ->
            isReady = state.isReady
        }

        collectWithLifecycle(viewModel.uiEvent) { event ->
            when (event) {
                is MainUiEvent.LoginSuccess ->
                    checkPermissionAndNavigate(event.permissionFlag)

                is MainUiEvent.Offline ->
                    checkPermissionAndNavigate(event.permissionFlag)

                MainUiEvent.IncompletePermissionFlow -> showPermissionDescriptionDialog()

                MainUiEvent.ForceUpdate -> showForceUpdateDialog()

                MainUiEvent.RegisterFailure,
                MainUiEvent.LoginFailure,
                MainUiEvent.GetPermissionFlagFailure,
                MainUiEvent.SavePermissionFlagFailure,
                -> finishAffinity()
            }
        }
    }

    private fun showForceUpdateDialog() {
        CustomAlertDialog
            .newInstance(DialogPresetType.FORCE_UPDATE)
            .setDialogListener(
                object : DialogListener {
                    override fun onClickNegative() = finishAffinity()

                    override fun onClickPositive() = launchPlayStore()
                },
            ).show(supportFragmentManager, DialogPresetType.FORCE_UPDATE.name)
    }

    private fun showPermissionDescriptionDialog() {
        PermissionDescriptionDialog {
            permissionLauncher.launch(requiredPermissions)
            viewModel.savePermissionFlag()
        }.show(
            supportFragmentManager,
            PermissionDescriptionDialog::class.simpleName,
        )
    }

    private fun checkPermissionAndNavigate(permissionFlag: Boolean) {
        if (!hasRequiredPermission(permissionFlag)) {
            binding.root.showSnackbar(R.string.common_permission_denied_text) {
                if (!checkDeeplink()) navigateToHome()
            }
            return
        }
        if (!checkDeeplink()) navigateToHome()
    }

    private fun showExactAlarmSettingsDialog() {
        CustomAlertDialog
            .newInstance(DialogPresetType.NAVIGATE_TO_ALARM_SETTINGS)
            .setDialogListener(
                object : DialogListener {
                    override fun onClickNegative() = viewModel.checkRegisteredMember()

                    override fun onClickPositive() {
                        PermissionUtil.requestExactAlarmPermission(this@MainActivity)
                        isNavigatedToSettings = true
                    }
                },
            ).show(supportFragmentManager, DialogPresetType.NAVIGATE_TO_ALARM_SETTINGS.name)
    }

    private fun hasRequiredPermission(permissionFlag: Boolean) =
        permissionFlag || (hasAllRuntimePermissions(this) && hasExactAlarmPermission(this))

    private fun checkDeeplink(): Boolean {
        intent.data?.let { uri ->
            if (validateUri(uri)) {
                navigateToInvite(uri)
                return true
            }
        }
        return false
    }

    private fun navigateToHome() {
        val intent = Intent(this, ComposeHomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToInvite(uri: Uri) {
        val inviteCode = getInviteCode(uri) ?: return
        val intent = InviteActivity.newIntent(this, inviteCode)
        startActivity(intent)
        finish()
    }

    private fun launchPlayStore() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = "market://details?id=$packageName".toUri()
        intent.`package` = "com.android.vending"
        startActivity(intent)
        finishAffinity()
    }
}
