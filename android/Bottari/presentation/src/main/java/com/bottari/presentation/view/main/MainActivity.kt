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
import com.bottari.presentation.view.home.HomeActivity
import com.bottari.presentation.view.invite.InviteActivity

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private val viewModel: MainViewModel by viewModels { MainViewModel.Factory() }
    private val permissionLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityForResult(
            ActivityResultContracts.RequestMultiplePermissions(),
        ) { showExactAlarmSettingsDialog() }
    private var isNavigatedToSettings: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.uiState.value?.isReady == false
            }
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
        viewModel.uiEvent.observe(this) { uiEvent ->
            when (uiEvent) {
                is MainUiEvent.LoginSuccess -> checkPermissionAndNavigate(uiEvent.permissionFlag)
                MainUiEvent.IncompletePermissionFlow -> showPermissionDescriptionDialog()
                MainUiEvent.RegisterFailure -> finishAffinity()
                MainUiEvent.ForceUpdate -> showForceUpdateDialog()
                MainUiEvent.LoginFailure,
                MainUiEvent.GetPermissionFlagFailure,
                MainUiEvent.SavePermissionFlagFailure,
                -> Unit
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
            binding.root.showSnackbar(R.string.splash_screen_permission_denied_text) {
                handleDeeplink()
                navigateToHome()
            }
            return
        }
        handleDeeplink()
        navigateToHome()
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

    private fun handleDeeplink() {
        intent.data?.let { uri ->
            if (validateUri(uri).not()) return
            navigateToInvite(uri)
        }
    }

    private fun navigateToHome() {
        val intent = HomeActivity.newIntent(this)
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
        intent.data = "https://play.google.com/store/apps/details?id=$packageName".toUri()
        startActivity(intent)
        finishAffinity()
    }
}
