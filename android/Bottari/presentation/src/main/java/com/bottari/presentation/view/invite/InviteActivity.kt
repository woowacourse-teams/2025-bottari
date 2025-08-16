package com.bottari.presentation.view.invite

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.bottari.presentation.common.base.BaseActivity
import com.bottari.presentation.databinding.ActivityInviteBinding
import com.bottari.presentation.view.home.HomeActivity

class InviteActivity : BaseActivity<ActivityInviteBinding>(ActivityInviteBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleAppLinkIntent()
    }

    private fun handleAppLinkIntent() {
        val inviteCode = intent.getStringExtra(KEY_INVITE_CODE) ?: navigateToHome()
    }

    private fun navigateToHome() {
        val intent = HomeActivity.newIntent(this)
        startActivity(intent)
        finish()
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
