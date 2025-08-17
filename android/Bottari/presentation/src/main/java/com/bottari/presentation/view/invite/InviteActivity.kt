package com.bottari.presentation.view.invite

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseActivity
import com.bottari.presentation.common.extension.SnackBarDuration
import com.bottari.presentation.common.extension.showSnackbar
import com.bottari.presentation.databinding.ActivityInviteBinding
import com.bottari.presentation.view.common.LoadingDialog
import com.bottari.presentation.view.home.HomeActivity

class InviteActivity : BaseActivity<ActivityInviteBinding>(ActivityInviteBinding::inflate) {
    private val viewModel: InviteViewModel by viewModels { InviteViewModel.Factory() }
    private var loadingDialog: LoadingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObserver()
        handleInviteCode()
    }

    private fun setupObserver() {
        viewModel.uiState.observe(this) { uiState ->
            toggleLoadingIndicator(uiState.isLoading)
        }
        viewModel.uiEvent.observe(this) { uiEvent ->
            when (uiEvent) {
                InviteUiEvent.JoinTeamBottariFailure -> {
                    binding.root.showSnackbar(
                        R.string.join_team_bottari_failure_text,
                        SnackBarDuration.VERY_SHORT_DELAY,
                    ) {
                        navigateToHome(false)
                    }
                }

                InviteUiEvent.JoinTeamBottariSuccess -> {
                    binding.root.showSnackbar(
                        R.string.join_team_bottari_success_text,
                        SnackBarDuration.VERY_SHORT_DELAY,
                    ) {
                        navigateToHome(true)
                    }
                }
            }
        }
    }

    private fun handleInviteCode() {
        val inviteCode = intent.getStringExtra(KEY_INVITE_CODE)
        if (inviteCode == null) {
            navigateToHome(false)
            return
        }
        viewModel.joinTeamBottari(inviteCode)
    }

    private fun toggleLoadingIndicator(isShow: Boolean) {
        if (loadingDialog?.context != this) {
            loadingDialog?.dismiss()
            loadingDialog = LoadingDialog(this)
        }
        val dialog = loadingDialog ?: return
        if (isShow) {
            if (dialog.isShowing.not()) dialog.show()
            return
        }
        if (dialog.isShowing) dialog.dismiss()
    }

    private fun navigateToHome(isJoinSuccess: Boolean) {
        val intent = createIntent(isJoinSuccess)
        startActivity(intent)
        finish()
    }

    private fun createIntent(isJoinSuccess: Boolean): Intent {
        if (isJoinSuccess) return HomeActivity.newIntentForDeeplink(this)
        return HomeActivity.newIntent(this)
    }

    companion object {
        private const val KEY_INVITE_CODE = "KEY_INVITE_CODE"

        fun newIntent(
            context: Context,
            inviteCode: String,
        ): Intent =
            Intent(context, InviteActivity::class.java).apply {
                putExtra(KEY_INVITE_CODE, inviteCode)
            }
    }
}
