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
    private val loadingDialog: LoadingDialog by lazy { LoadingDialog() }

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

    private fun navigateToHome(isJoinSuccess: Boolean) {
        val intent =
            if (isJoinSuccess) {
                HomeActivity.newIntentForDeeplink(this)
            } else {
                HomeActivity.newIntent(
                    this,
                )
            }
        startActivity(intent)
        finish()
    }

    private fun toggleLoadingIndicator(isShow: Boolean) {
        if (isShow) {
            if (loadingDialog.isAdded || loadingDialog.isVisible || loadingDialog.isRemoving) return
            if (supportFragmentManager.isStateSaved) return

            loadingDialog.show(supportFragmentManager, LoadingDialog::class.java.name)
            return
        }

        if (!loadingDialog.isAdded) return
        loadingDialog.dismissAllowingStateLoss()
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
