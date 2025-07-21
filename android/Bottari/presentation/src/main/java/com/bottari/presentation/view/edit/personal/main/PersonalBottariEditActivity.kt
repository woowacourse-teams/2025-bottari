package com.bottari.presentation.view.edit.personal.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.viewModels
import com.bottari.presentation.R
import com.bottari.presentation.base.BaseActivity
import com.bottari.presentation.base.UiState
import com.bottari.presentation.databinding.ActivityPersonalBottariEditBinding
import com.bottari.presentation.model.BottariUiModel

class PersonalBottariEditActivity : BaseActivity<ActivityPersonalBottariEditBinding>(ActivityPersonalBottariEditBinding::inflate) {
    private val viewModel: PersonalBottariEditViewModel by viewModels {
        PersonalBottariEditViewModel.Factory(getBottariId())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObsever()
        setupListener()
        navigateToEdit()
        handleBackPress()
    }

    private fun getBottariId(): Long = intent.getLongExtra(EXTRAS_BOTTARI_ID, INVALID_BOTTARI_ID)

    private fun setupListener() {
        binding.btnPrevious.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun navigateToEdit() {
        supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            replace(
                R.id.fcv_edit,
                PersonalBottariFragment::class.java,
                null,
                PersonalBottariFragment::class.java.name,
            )
            commit()
        }
    }

    private fun handleBackPress() {
        onBackPressedDispatcher.addCallback(this) {
            if (supportFragmentManager.backStackEntryCount == MIN_FRAGMENT_ENTRY_COUNT) {
                finish()
                return@addCallback
            }
            supportFragmentManager.popBackStack()
        }
    }

    private fun setupObsever() {
        viewModel.bottari.observe(this) { bottari ->
            handleBottariTitleState(bottari)
        }
    }

    private fun handleBottariTitleState(uiState: UiState<BottariUiModel>) {
        when (uiState) {
            is UiState.Loading -> return
            is UiState.Success -> binding.tvBottariTitle.text = uiState.data.title
            is UiState.Failure -> {}
        }
    }

    companion object {
        private const val EXTRAS_BOTTARI_ID = "EXTRAS_BOTTARI_ID"
        private const val INVALID_BOTTARI_ID = -1L
        private const val MIN_FRAGMENT_ENTRY_COUNT = 0

        fun newIntent(
            context: Context,
            bottariId: Long,
        ): Intent =
            Intent(context, PersonalBottariEditActivity::class.java).apply {
                putExtra(EXTRAS_BOTTARI_ID, bottariId)
            }
    }
}
