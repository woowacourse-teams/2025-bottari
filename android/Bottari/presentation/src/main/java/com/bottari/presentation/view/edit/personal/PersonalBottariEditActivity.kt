package com.bottari.presentation.view.edit.personal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseActivity
import com.bottari.presentation.databinding.ActivityPersonalBottariEditBinding
import com.bottari.presentation.view.edit.personal.main.PersonalBottariEditFragment
import com.google.android.material.snackbar.Snackbar

class PersonalBottariEditActivity : BaseActivity<ActivityPersonalBottariEditBinding>(ActivityPersonalBottariEditBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showWelcomeMessage()
        if (savedInstanceState != null) return
        navigateToScreen()
    }

    private fun navigateToScreen() {
        supportFragmentManager.beginTransaction().run {
            replace(
                R.id.fcv_personal_edit,
                PersonalBottariEditFragment::class.java,
                PersonalBottariEditFragment.newBundle(getBottariId()),
            )
            commit()
        }
    }

    private fun getBottariId(): Long =
        intent.getLongExtra(
            EXTRA_BOTTARI_ID,
            INVALID_BOTTARI_ID,
        )

    private fun showWelcomeMessage() {
        if (!intent.getBooleanExtra(EXTRA_IS_NEW_BOTTARI, false)) return
        Snackbar
            .make(binding.root, R.string.bottari_create_success_text, Snackbar.LENGTH_SHORT)
            .show()
    }

    companion object {
        private const val INVALID_BOTTARI_ID = -1L
        private const val EXTRA_BOTTARI_ID = "EXTRA_BOTTARI_ID"
        private const val EXTRA_IS_NEW_BOTTARI = "EXTRA_IS_NEW_BOTTARI"

        fun newIntent(
            context: Context,
            bottariId: Long,
            isNewBottari: Boolean,
        ): Intent =
            Intent(context, PersonalBottariEditActivity::class.java).apply {
                putExtra(EXTRA_BOTTARI_ID, bottariId)
                putExtra(EXTRA_IS_NEW_BOTTARI, isNewBottari)
            }
    }
}
