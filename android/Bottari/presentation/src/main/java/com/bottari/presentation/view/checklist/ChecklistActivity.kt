package com.bottari.presentation.view.checklist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentTransaction
import com.bottari.presentation.R
import com.bottari.presentation.base.BaseActivity
import com.bottari.presentation.databinding.ActivityChecklistBinding
import com.bottari.presentation.view.checklist.main.MainChecklistFragment
import com.bottari.presentation.view.checklist.swipe.SwipeChecklistFragment

class ChecklistActivity : BaseActivity<ActivityChecklistBinding>(ActivityChecklistBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI()
        setupListener()
    }

    private fun setupUI() {
        navigateToChecklist(true)
        binding.tvBottariTitle.text = intent.getStringExtra(EXTRA_BOTTARI_TITLE)
    }

    private fun setupListener() {
        binding.btnPrevious.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        binding.btnSwipe.setOnClickListener { navigateToChecklist(false) }
    }

    private fun navigateToChecklist(isMain: Boolean) {
        val bottariId = getBottariId()
        val fragment = if (isMain) MainChecklistFragment::class.java else SwipeChecklistFragment::class.java
        val bundle = if (isMain) MainChecklistFragment.newBundle(bottariId) else SwipeChecklistFragment.newBundle(bottariId)
        supportFragmentManager.beginTransaction().apply {
            setSlideFastAnimation()
            replace(R.id.fcv_checklist, fragment, bundle, fragment.name)
            if (!isMain) addToBackStack(fragment.name)
            commit()
        }
        updateToolbar(isMain)
    }

    private fun getBottariId(): Long = intent.getLongExtra(EXTRA_BOTTARI_ID, INVALID_BOTTARI_ID)

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
