package com.bottari.presentation.view.edit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.viewModels
import com.bottari.presentation.R
import com.bottari.presentation.base.BaseActivity
import com.bottari.presentation.databinding.ActivityPersonalBottariEditBinding

class PersonalBottariEditActivity : BaseActivity<ActivityPersonalBottariEditBinding>(ActivityPersonalBottariEditBinding::inflate) {
    private val viewModel: PersonalBottariViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObsever()
        setupListener()
        getBottari(intent.getIntExtra(BOTTARI_ID, DEFAULT_BOTTARI_ID))
        navigateToEdit()
        handleBackPress()
    }

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
        viewModel.bottari.observe(this) {
            binding.tvBottariTitle.text = it.title
        }
    }

    private fun getBottari(bottariId: Int) {
        viewModel.fetchBottariById(bottariId)
    }

    companion object {
        private const val BOTTARI_ID = "BOTTARI_ID"
        private const val MIN_FRAGMENT_ENTRY_COUNT = 0
        private const val DEFAULT_BOTTARI_ID = 0

        fun newIntent(
            context: Context,
            bottariId: Long,
        ): Intent =
            Intent(context, PersonalBottariEditActivity::class.java).apply {
                putExtra(BOTTARI_ID, bottariId)
            }
    }
}
