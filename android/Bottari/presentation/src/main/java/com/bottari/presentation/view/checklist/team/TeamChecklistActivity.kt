package com.bottari.presentation.view.checklist.team

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseActivity
import com.bottari.presentation.databinding.ActivityTeamChecklistBinding
import com.bottari.presentation.view.checklist.team.checklist.swipe.TeamSwipeChecklistFragment
import com.bottari.presentation.view.home.HomeActivity

class TeamChecklistActivity : BaseActivity<ActivityTeamChecklistBinding>(ActivityTeamChecklistBinding::inflate) {
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
        navigateToTeamChecklistMain()
    }

    private fun setupListener() {
        onBackPressedDispatcher.addCallback {
            when {
                notificationFlag -> navigateToHome()
                currentFragmentIsSwipe() -> {
                    supportFragmentManager.popBackStack()
                    navigateToTeamChecklistMain()
                }

                supportFragmentManager.backStackEntryCount == 0 -> finish()
                else -> supportFragmentManager.popBackStack()
            }
        }
        binding.btnPrevious.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.btnSwipe.setOnClickListener {
            navigateToSwipeChecklist()
        }
    }

    private fun currentFragmentIsSwipe(): Boolean =
        supportFragmentManager.findFragmentById(R.id.fcv_team_checklist) is TeamSwipeChecklistFragment

    private fun navigateToHome() {
        val intent = HomeActivity.newIntent(this)
        startActivity(intent)
        finish()
    }

    private fun navigateToTeamChecklistMain() {
        val intent = TeamChecklistMainFragment.newInstance(bottariId)
        replaceChecklistFragment(intent, false)
        binding.btnSwipe.isVisible = true
    }

    private fun navigateToSwipeChecklist() {
        val intent = TeamSwipeChecklistFragment.newInstance(bottariId)
        replaceChecklistFragment(intent, false)
        binding.btnSwipe.isVisible = false
    }

    private fun replaceChecklistFragment(
        fragment: Fragment,
        addToBackStack: Boolean,
    ) {
        supportFragmentManager.commit {
            replace(R.id.fcv_team_checklist, fragment)
            if (addToBackStack) addToBackStack(fragment::class.simpleName)
        }
    }

    companion object {
        private const val INVALID_BOTTARI_ID = -1L
        private const val EXTRA_BOTTARI_ID = "EXTRA_BOTTARI_ID"
        private const val EXTRA_BOTTARI_TITLE = "EXTRA_BOTTARI_TITLE"
        private const val EXTRA_NOTIFICATION_FLAG = "EXTRA_NOTIFICATION_FLAG"

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
