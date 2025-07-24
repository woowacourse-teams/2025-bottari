package com.bottari.presentation.view.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.net.toUri
import com.bottari.presentation.BuildConfig
import com.bottari.presentation.base.BaseActivity
import com.bottari.presentation.databinding.ActivitySettingBinding

class SettingActivity : BaseActivity<ActivitySettingBinding>(ActivitySettingBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupListener()
    }

    private fun setupListener() {
        binding.btnPrevious.setOnClickListener { finish() }
        binding.btnPrivacyPolicy.setOnClickListener { launchInBrowser(BuildConfig.PRIVACY_POLICY_URL) }
    }

    private fun launchInBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        startActivity(intent)
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, SettingActivity::class.java)
    }
}
