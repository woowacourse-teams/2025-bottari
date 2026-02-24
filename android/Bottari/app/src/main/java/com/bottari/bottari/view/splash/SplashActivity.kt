package com.bottari.bottari.view.splash

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bottari.bottari.R
import com.bottari.bottari.databinding.ActivitySplashBinding
import com.bottari.bottari.util.PermissionUtil
import com.bottari.bottari.util.PermissionUtil.hasAllRuntimePermissions
import com.bottari.bottari.util.PermissionUtil.hasExactAlarmPermission
import com.bottari.bottari.util.PermissionUtil.requiredPermissions
import com.bottari.bottari.view.PermissionDescriptionDialog
import com.bottari.bottari.view.alert.CustomAlertDialog
import com.bottari.bottari.view.alert.DialogListener
import com.bottari.bottari.view.alert.DialogPresetType
import com.bottari.common.util.DeeplinkHelper.getInviteCode
import com.bottari.common.util.DeeplinkHelper.validateUri
import com.bottari.core.ui.extension.showSnackbar
import com.bottari.feature.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private val binding: ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }
    private val viewModel: SplashViewModel by viewModels()
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
        setContentView(binding.root)
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
                is SplashUiEvent.LoginSuccess -> checkPermissionAndNavigate(event.permissionFlag)

                is SplashUiEvent.Offline -> checkPermissionAndNavigate(event.permissionFlag)

                SplashUiEvent.IncompletePermissionFlow -> showPermissionDescriptionDialog()

                SplashUiEvent.ForceUpdate -> showForceUpdateDialog()

                SplashUiEvent.RegisterFailure,
                SplashUiEvent.LoginFailure,
                SplashUiEvent.GetPermissionFlagFailure,
                SplashUiEvent.SavePermissionFlagFailure,
                -> finishAffinity()
            }
        }
    }

    private fun <T> collectWithLifecycle(
        flow: Flow<T>,
        state: Lifecycle.State = Lifecycle.State.STARTED,
        collector: suspend (T) -> Unit,
    ) {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(state) {
                flow.collect(collector)
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
                        PermissionUtil.requestExactAlarmPermission(this@SplashActivity)
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
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToInvite(uri: Uri) {
        val inviteCode = getInviteCode(uri) ?: return
        val intent = MainActivity.newIntentForInvite(this, inviteCode)
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
