package com.bottari.presentation.view.main

import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
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
        registerForActivityResult(
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
                MainUiEvent.LoginFailure -> Unit
                MainUiEvent.RegisterFailure -> finishAffinity()
                MainUiEvent.GetPermissionFlagFailure -> Unit
                MainUiEvent.SavePermissionFlagFailure -> Unit
                MainUiEvent.IncompletePermissionFlow -> showPermissionDescriptionDialog()
            }
        }
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
            .newInstance(DialogPresetType.NAVIGATE_TO_SETTINGS)
            .setDialogListener(
                object : DialogListener {
                    override fun onClickNegative() {
                        viewModel.checkRegisteredMember()
                    }

                    override fun onClickPositive() {
                        PermissionUtil.requestExactAlarmPermission(this@MainActivity)
                        isNavigatedToSettings = true
                    }
                },
            ).show(supportFragmentManager, DialogPresetType.NAVIGATE_TO_SETTINGS.name)
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
}
