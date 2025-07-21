package com.bottari.presentation.view.edit.personal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.bottari.presentation.R
import com.bottari.presentation.base.BaseActivity
import com.bottari.presentation.databinding.ActivityPersonalBottariEditBinding
import com.bottari.presentation.view.edit.personal.main.PersonalBottariEditFragment

class PersonalBottariEditActivity :
    BaseActivity<ActivityPersonalBottariEditBinding>(ActivityPersonalBottariEditBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navigateToScreen()
    }

    private fun getBottariId(): Long =
        intent.getLongExtra(
            EXTRA_BOTTARI_ID,
            INVALID_BOTTARI_ID,
        )

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

    companion object {
        private const val INVALID_BOTTARI_ID = -1L
        private const val EXTRA_BOTTARI_ID = "EXTRA_BOTTARI_ID"

        fun newIntent(
            context: Context,
            bottariId: Long,
        ): Intent =
            Intent(context, PersonalBottariEditActivity::class.java).apply {
                putExtra(EXTRA_BOTTARI_ID, bottariId)
            }
    }
}
