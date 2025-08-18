package com.bottari.presentation.view.checklist.team

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseActivity
import com.bottari.presentation.databinding.ActivityTeamChecklistBinding
import com.bottari.presentation.view.checklist.team.main.TeamChecklistMainFragment
import com.bottari.presentation.view.checklist.team.swipe.TeamSwipeChecklistFragment
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
                supportFragmentManager.backStackEntryCount == 0 -> finish()
                else -> supportFragmentManager.popBackStack()
            }
        }
        supportFragmentManager.addOnBackStackChangedListener {
            handleToolbar()
        }
        binding.btnPrevious.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.btnSwipe.setOnClickListener {
            navigateToSwipeChecklist()
        }
    }

    private fun handleToolbar() {
        val isSwipeFragmentVisible = supportFragmentManager.backStackEntryCount > 0
        binding.btnSwipe.isVisible = !isSwipeFragmentVisible
    }

    private fun navigateToHome() {
        val intent = HomeActivity.newIntent(this)
        startActivity(intent)
        finish()
    }

    private fun navigateToTeamChecklistMain() {
        val intent = TeamChecklistMainFragment.newInstance(bottariId)
        replaceChecklistFragment(intent, false)
    }

    private fun navigateToSwipeChecklist() {
        val intent = TeamSwipeChecklistFragment.newInstance(bottariId)
        replaceChecklistFragment(intent, true)
    }

    private fun replaceChecklistFragment(
        fragment: Fragment,
        addToBackStack: Boolean,
    ) {
        supportFragmentManager.commit {
            setSlideFastAnimation()
            replace(R.id.fcv_team_checklist, fragment)
            if (addToBackStack) addToBackStack(fragment::class.simpleName)
        }
    }

    private fun FragmentTransaction.setSlideFastAnimation() {
        setCustomAnimations(
            R.anim.slide_in_right_fast,
            R.anim.slide_out_right_fast,
            R.anim.slide_in_right_fast,
            R.anim.slide_out_right_fast,
        )
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
