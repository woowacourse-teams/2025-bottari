package com.bottari.presentation.view.edit.personal.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.addCallback
import com.bottari.presentation.R
import com.bottari.presentation.base.BaseActivity
import com.bottari.presentation.databinding.ActivityPersonalBottariEditBinding

class PersonalBottariEditActivity : BaseActivity<ActivityPersonalBottariEditBinding>(ActivityPersonalBottariEditBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigateToEdit()
        handleBackPress()
    }

    private fun navigateToEdit() {
        supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            replace(
                R.id.fcv_edit,
                PersonalBottariEditFragment::class.java,
                null,
                PersonalBottariEditFragment::class.java.name,
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

    companion object {
        private const val EXTRAS_BOTTARI_ID = "EXTRAS_BOTTARI_ID"
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
