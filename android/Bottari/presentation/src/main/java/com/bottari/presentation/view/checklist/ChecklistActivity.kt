package com.bottari.presentation.view.checklist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.bottari.presentation.R
import com.bottari.presentation.base.BaseActivity
import com.bottari.presentation.base.UiState
import com.bottari.presentation.databinding.ActivityChecklistBinding
import com.bottari.presentation.view.checklist.main.MainChecklistFragment
import com.bottari.presentation.view.checklist.swipe.SwipeChecklistFragment

class ChecklistActivity : BaseActivity<ActivityChecklistBinding>(ActivityChecklistBinding::inflate) {
    private val viewModel: ChecklistViewModel by viewModels {
        ChecklistViewModel.Factory(getBottariId())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObserver()
        setupListener()
        navigateToChecklist()
        handleBackPress()
    }

    private fun setupObserver() {
        viewModel.bottariTitle.observe(this, ::handleBottariTitleState)
    }

    private fun setupListener() {
        binding.btnPrevious.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        binding.btnSwipe.setOnClickListener { navigateToSwipeChecklist() }
    }

    private fun navigateToChecklist() {
        val tag = MainChecklistFragment::class.java.name
        if (supportFragmentManager.findFragmentByTag(tag) != null) return
        supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            replace(R.id.fcv_checklist, MainChecklistFragment::class.java, null, tag)
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
            setCustomAnimations(
                R.anim.slide_in_right_fast,
                R.anim.slide_out_right_fast,
                R.anim.slide_in_right_fast,
                R.anim.slide_out_right_fast,
            )
            add(R.id.fcv_checklist, SwipeChecklistFragment::class.java, null, tag)
            addToBackStack(null)
            commit()
        }
        updateToolbar(false)
    }

    private fun handleBottariTitleState(uiState: UiState<String>) {
        when (uiState) {
            is UiState.Loading -> return
            is UiState.Success -> binding.tvBottariTitle.text = uiState.data
            is UiState.Failure -> {}
        }
    }

    private fun getBottariId(): Long = intent.getLongExtra(EXTRA_BOTTARI_ID, INVALID_BOTTARI_ID)

    private fun updateToolbar(isVisible: Boolean) {
        binding.btnSwipe.isVisible = isVisible
        val imageRes = if (isVisible) R.drawable.btn_previous else R.drawable.btn_close
        binding.btnPrevious.setImageResource(imageRes)
    }

    companion object {
        private const val MIN_FRAGMENT_ENTRY_COUNT = 0
        private const val INVALID_BOTTARI_ID = -1L
        private const val EXTRA_BOTTARI_ID = "EXTRA_BOTTARI_ID"

        fun newIntent(
            context: Context,
            bottariId: Long,
        ): Intent =
            Intent(context, ChecklistActivity::class.java).apply {
                putExtra(EXTRA_BOTTARI_ID, bottariId)
            }
    }
}
