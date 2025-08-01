package com.bottari.presentation.view.main

import android.app.AlertDialog
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.StringRes
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseActivity
import com.bottari.presentation.common.extension.getSSAID
import com.bottari.presentation.databinding.ActivityMainBinding
import com.bottari.presentation.util.PermissionUtil
import com.bottari.presentation.util.PermissionUtil.hasAllRuntimePermissions
import com.bottari.presentation.util.PermissionUtil.hasExactAlarmPermission
import com.bottari.presentation.util.PermissionUtil.requiredPermissions
import com.bottari.presentation.view.common.PermissionDescriptionDialog
import com.bottari.presentation.view.home.HomeActivity
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlin.system.exitProcess

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private val viewModel: MainViewModel by viewModels { MainViewModel.Factory(this.getSSAID()) }
    private val permissionLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
        ) { showExactAlarmSettingsDialog() }
    private var isNavigatedToSettings: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
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
                MainUiEvent.RegisterFailure -> exitProcess(-1)
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
        if ((!hasAllRuntimePermissions(this) || !hasExactAlarmPermission(this)) && !permissionFlag.not()) {
            showSnackbar(R.string.splash_screen_permission_denied_text, ::navigateToHome)
            return
        }
        navigateToHome()
    }

    private fun showExactAlarmSettingsDialog() {
        AlertDialog
            .Builder(this)
            .setTitle(R.string.common_permission_dialog_title_text)
            .setMessage(R.string.alarm_edit_exact_alarm_permission_dialog_message_text)
            .setPositiveButton(R.string.common_permission_dialog_positive_btn_text) { _, _ ->
                PermissionUtil.requestExactAlarmPermission(this)
                isNavigatedToSettings = true
            }.setNegativeButton(R.string.common_permission_dialog_negative_btn_text) { _, _ -> viewModel.checkRegisteredMember() }
            .show()
    }

    private fun showSnackbar(
        @StringRes resId: Int,
        onDismissed: () -> Unit,
    ) {
        val snackbar = Snackbar.make(binding.root, resId, Snackbar.LENGTH_SHORT)
        snackbar.addCallback(
            object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                override fun onDismissed(
                    transientBottomBar: Snackbar?,
                    event: Int,
                ) {
                    super.onDismissed(transientBottomBar, event)
                    onDismissed()
                }
            },
        )
        snackbar.show()
    }

    private fun navigateToHome() {
        val intent = HomeActivity.newIntent(this)
        startActivity(intent)
        finish()
    }
}
