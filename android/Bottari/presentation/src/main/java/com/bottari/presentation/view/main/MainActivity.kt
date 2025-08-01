package com.bottari.presentation.view.main

import android.app.AlertDialog
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseActivity
import com.bottari.presentation.common.extension.getSSAID
import com.bottari.presentation.databinding.ActivityMainBinding
import com.bottari.presentation.util.PermissionUtil
import com.bottari.presentation.util.PermissionUtil.requiredPermissions
import com.bottari.presentation.view.common.PermissionDescriptionDialog
import com.bottari.presentation.view.home.HomeActivity

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private val viewModel: MainViewModel by viewModels { MainViewModel.Factory(this.getSSAID()) }
    private val permissionLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
        ) { checkAndRequestSpecialPermission() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObserver()
    }

    private fun checkAndRequestSpecialPermission() {
        if (PermissionUtil.hasExactAlarmPermission(this)) {
            navigateToHome()
            return
        }
        showExactAlarmSettingsDialog()
    }

    private fun setupObserver() {
        viewModel.uiState.observe(this) { uiState ->
        }
        viewModel.uiEvent.observe(this) { uiEvent ->
            when (uiEvent) {
                MainUiEvent.LoginSuccess -> navigateToHome()
                MainUiEvent.LoginFailure -> Unit
                MainUiEvent.RegisterFailure -> Unit
                MainUiEvent.GetPermissionFlagFailure -> Unit
                MainUiEvent.IncompletePermissionFlow -> showPermissionDescriptionDialog()
            }
        }
    }

    private fun showPermissionDescriptionDialog() {
        PermissionDescriptionDialog {
            permissionLauncher.launch(requiredPermissions)
        }.show(
            supportFragmentManager,
            PermissionDescriptionDialog::class.simpleName,
        )
    }

    private fun navigateToHome() {
        val intent = HomeActivity.newIntent(this)
        startActivity(intent)
        finish()
    }

    private fun showExactAlarmSettingsDialog() {
        AlertDialog
            .Builder(this)
            .setTitle(R.string.common_permission_dialog_title_text)
            .setMessage(R.string.alarm_edit_exact_alarm_permission_dialog_message_text)
            .setPositiveButton(R.string.common_permission_dialog_positive_btn_text) { _, _ ->
                PermissionUtil.requestExactAlarmPermission(this)
            }.setNegativeButton(R.string.common_permission_dialog_negative_btn_text) { _, _ -> viewModel.checkRegisteredMember() }
            .show()
    }
}
