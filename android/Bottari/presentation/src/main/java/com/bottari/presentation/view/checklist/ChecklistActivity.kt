package com.bottari.presentation.view.checklist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.bottari.presentation.R
import com.bottari.presentation.base.BaseActivity
import com.bottari.presentation.base.UiState
import com.bottari.presentation.databinding.ActivityChecklistBinding
import com.bottari.presentation.view.checklist.main.MainChecklistFragment

class ChecklistActivity : BaseActivity<ActivityChecklistBinding>(ActivityChecklistBinding::inflate) {
    private val viewModel: ChecklistViewModel by viewModels {
        ChecklistViewModel.Factory(
            getBottariId(),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupObserver()
        setupUI()
        navigateToChecklist()
    }

    private fun setupObserver() {
        viewModel.bottariTitle.observe(this) { uiState -> handleBottariTitleState(uiState) }
    }

    private fun setupUI() {
        binding.btnPrevious.setOnClickListener {
            if (supportFragmentManager.fragments.size > MIN_FRAGMENT_COUNT_TO_POP) {
                supportFragmentManager.popBackStack()
                return@setOnClickListener
            }

            finish()
        }
    }

    private fun getBottariId(): Long = intent.getLongExtra(EXTRA_BOTTARI_ID, INVALID_BOTTARI_ID)

    private fun navigateToChecklist() {
        supportFragmentManager.beginTransaction().run {
            replace(
                R.id.fcv_checklist,
                MainChecklistFragment::class.java,
                null,
            )
            commit()
        }
    }

    private fun handleBottariTitleState(uiState: UiState<String>) {
        when (uiState) {
            is UiState.Loading -> {}
            is UiState.Success -> binding.tvBottariTitle.text = uiState.data
            is UiState.Failure -> {}
        }
    }

    companion object {
        private const val MIN_FRAGMENT_COUNT_TO_POP = 1
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
