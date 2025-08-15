package com.bottari.presentation.view.checklist.team

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.addCallback
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseActivity
import com.bottari.presentation.databinding.ActivityTeamChecklistBinding
import com.bottari.presentation.view.checklist.team.checklist.adapter.TeamChecklistFragmentAdapter
import com.bottari.presentation.view.home.HomeActivity
import com.google.android.material.tabs.TabLayoutMediator

class TeamChecklistActivity : BaseActivity<ActivityTeamChecklistBinding>(ActivityTeamChecklistBinding::inflate) {
    private val adapter: TeamChecklistFragmentAdapter by lazy {
        TeamChecklistFragmentAdapter(this, bottariId)
    }
    private val bottariId: Long by lazy {
        intent.getLongExtra(
            EXTRA_BOTTARI_ID,
            INVALID_BOTTARI_ID,
        )
    }
    private val notificationFlag: Boolean by lazy {
        intent.getBooleanExtra(
            EXTRA_NOTIFICATION_FLAG,
            false,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI()
        setupListener()
    }

    private fun setupUI() {
        binding.tvBottariTitle.text = intent.getStringExtra(EXTRA_BOTTARI_TITLE)
        binding.vpTeamBottari.adapter = adapter
        TabLayoutMediator(binding.tlTeamBottari, binding.vpTeamBottari) { tab, position ->
            tab.text =
                when (position) {
                    0 -> getString(R.string.team_checklist_tap_checklist_text)
                    1 -> getString(R.string.team_checklist_tap_team_current_text)
                    2 -> getString(R.string.team_checklist_tap_member_checklist_text)
                    else -> throw IllegalArgumentException(ERROR_UNKNOWN_TYPE)
                }
        }.attach()
    }

    private fun setupListener() {
        onBackPressedDispatcher.addCallback {
            if (notificationFlag) {
                navigateToHome()
                return@addCallback
            }
            onBackPressedDispatcher.onBackPressed()
        }
        binding.btnPrevious.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun navigateToHome() {
        val intent = HomeActivity.newIntent(this)
        startActivity(intent)
        finish()
    }

    companion object {
        private const val INVALID_BOTTARI_ID = -1L
        private const val EXTRA_BOTTARI_ID = "EXTRA_BOTTARI_ID"
        private const val EXTRA_BOTTARI_TITLE = "EXTRA_BOTTARI_TITLE"
        private const val EXTRA_NOTIFICATION_FLAG = "EXTRA_NOTIFICATION_FLAG"
        private const val ERROR_UNKNOWN_TYPE = "[ERROR] 알 수 없는 타입입니다."

        fun newIntent(
            context: Context,
            bottariId: Long,
            bottariTitle: String,
        ): Intent =
            Intent(context, TeamChecklistActivity::class.java).apply {
                putExtra(EXTRA_BOTTARI_ID, bottariId)
                putExtra(EXTRA_BOTTARI_TITLE, bottariTitle)
            }

        fun newIntentForNotification(
            context: Context,
            bottariId: Long,
            bottariTitle: String,
        ): Intent =
            Intent(context, TeamChecklistActivity::class.java).apply {
                putExtra(EXTRA_BOTTARI_ID, bottariId)
                putExtra(EXTRA_BOTTARI_TITLE, bottariTitle)
                putExtra(EXTRA_NOTIFICATION_FLAG, true)
            }
    }
}
