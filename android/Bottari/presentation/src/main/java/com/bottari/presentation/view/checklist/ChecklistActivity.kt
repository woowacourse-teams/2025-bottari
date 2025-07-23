package com.bottari.presentation.view.checklist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentTransaction
import com.bottari.presentation.R
import com.bottari.presentation.base.BaseActivity
import com.bottari.presentation.databinding.ActivityChecklistBinding
import com.bottari.presentation.extension.getSSAID
import com.bottari.presentation.view.checklist.main.MainChecklistFragment
import com.bottari.presentation.view.checklist.swipe.SwipeChecklistFragment

class ChecklistActivity :
    BaseActivity<ActivityChecklistBinding>(ActivityChecklistBinding::inflate) {
    private val viewModel: ChecklistViewModel by viewModels {
        ChecklistViewModel.Factory(getSSAID(), getBottariId())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI()
        setupListener()
        navigateToChecklist()
        handleBackPress()
    }

    private fun getBottariId(): Long = intent.getLongExtra(EXTRA_BOTTARI_ID, INVALID_BOTTARI_ID)

    private fun setupUI() {
        binding.tvBottariTitle.text = intent.getStringExtra(EXTRA_BOTTARI_TITLE)
    }

    private fun setupListener() {
        binding.btnPrevious.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        binding.btnSwipe.setOnClickListener { navigateToSwipeChecklist() }
    }

    private fun navigateToChecklist() {
        val tag = MainChecklistFragment::class.java.name
        if (supportFragmentManager.findFragmentByTag(tag) != null) return
        supportFragmentManager.beginTransaction().apply {
            val bundle = MainChecklistFragment.newBundle(getBottariId())
            replace(R.id.fcv_checklist, MainChecklistFragment::class.java, bundle, tag)
            commit()
        }
        binding.btnSwipe.isVisible = true
        binding.btnPrevious.setImageResource(R.drawable.btn_previous)
    }

    private fun handleBackPress() {
        onBackPressedDispatcher.addCallback(this) {
            if (supportFragmentManager.backStackEntryCount == MIN_FRAGMENT_ENTRY_COUNT) {
                finish()
                return@addCallback
            }
            supportFragmentManager.popBackStack()
            updateToolbar(true)
        }
    }

    private fun navigateToSwipeChecklist() {
        val tag = SwipeChecklistFragment::class.java.name
        if (supportFragmentManager.findFragmentByTag(tag) != null) return
        supportFragmentManager.beginTransaction().apply {
            setSlideFastAnimation()
            val bundle = SwipeChecklistFragment.newBundle(getBottariId())
            add(R.id.fcv_checklist, SwipeChecklistFragment::class.java, bundle, tag)
            addToBackStack(null)
            commit()
        }
        updateToolbar(false)
    }

    private fun FragmentTransaction.setSlideFastAnimation() {
        setCustomAnimations(
            R.anim.slide_in_right_fast,
            R.anim.slide_out_right_fast,
            R.anim.slide_in_right_fast,
            R.anim.slide_out_right_fast,
        )
    }

    private fun updateToolbar(isVisible: Boolean) {
        binding.btnSwipe.isVisible = isVisible
        val imageRes = if (isVisible) R.drawable.btn_previous else R.drawable.btn_close
        binding.btnPrevious.setImageResource(imageRes)
    }

    companion object {
        private const val MIN_FRAGMENT_ENTRY_COUNT = 0
        private const val INVALID_BOTTARI_ID = -1L
        private const val EXTRA_BOTTARI_ID = "EXTRA_BOTTARI_ID"
        private const val EXTRA_BOTTARI_TITLE = "EXTRA_BOTTARI_TITLE"

        fun newIntent(
            context: Context,
            bottariId: Long,
            bottariTitle: String,
        ): Intent =
            Intent(context, ChecklistActivity::class.java).apply {
                putExtra(EXTRA_BOTTARI_ID, bottariId)
                putExtra(EXTRA_BOTTARI_TITLE, bottariTitle)
            }
    }
}
