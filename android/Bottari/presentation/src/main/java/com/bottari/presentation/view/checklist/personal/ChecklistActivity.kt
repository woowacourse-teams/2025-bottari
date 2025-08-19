package com.bottari.presentation.view.checklist.personal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import com.bottari.logger.BottariLogger
import com.bottari.logger.model.UiEventType
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseActivity
import com.bottari.presentation.databinding.ActivityChecklistBinding
import com.bottari.presentation.model.ChecklistItemUiModel
import com.bottari.presentation.view.checklist.personal.main.MainChecklistFragment
import com.bottari.presentation.view.checklist.personal.swipe.SwipeChecklistFragment
import com.bottari.presentation.view.common.alert.CustomAlertDialog
import com.bottari.presentation.view.common.alert.DialogListener
import com.bottari.presentation.view.common.alert.DialogPresetType
import com.bottari.presentation.view.home.HomeActivity
import java.time.LocalDateTime

class ChecklistActivity : BaseActivity<ActivityChecklistBinding>(ActivityChecklistBinding::inflate) {
    private val viewModel: ChecklistViewModel by viewModels {
        ChecklistViewModel.Factory(bottariId)
    }
    private val bottariId: Long by lazy {
        intent.getLongExtra(
            EXTRA_BOTTARI_ID,
            INVALID_BOTTARI_ID,
        )
    }
    private val notificationFlag: Boolean by lazy {
        intent.getBooleanExtra(EXTRA_NOTIFICATION_FLAG, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObserver()
        setupUI(savedInstanceState)
        setupListener()

        if (notificationFlag) {
            BottariLogger.ui(
                UiEventType.NOTIFICATION_CLICK,
                mapOf("notification_id" to bottariId, "time" to LocalDateTime.now().toString()),
            )
        }
    }

    private fun setupObserver() {
        viewModel.uiState.observe(this) { uiState ->
            updateToolbar(isMainChecklist() && uiState.bottariItems.isNotEmpty())
            binding.btnReset.isVisible = uiState.isAnyChecked
            if (uiState.isAllChecked) logChecklistFinished(uiState.bottariItems)
        }
    }

    private fun setupUI(savedInstanceState: Bundle?) {
        binding.tvBottariTitle.text = intent.getStringExtra(EXTRA_BOTTARI_TITLE).orEmpty()
        if (savedInstanceState != null) return
        if (notificationFlag) {
            navigateToChecklistForNotification()
            return
        }
        navigateToMainChecklist()
    }

    private fun setupListener() {
        binding.btnPrevious.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            updateToolbar(isMainChecklist())
        }
        binding.btnSwipe.setOnClickListener { navigateToSwipeChecklist() }
        binding.btnReset.setOnClickListener { showResetChecklistDialog() }
        supportFragmentManager.addOnBackStackChangedListener {
            updateToolbar(isMainChecklist())
        }
        onBackPressedDispatcher.addCallback {
            when {
                notificationFlag && isMainChecklist() -> navigateToHome()
                isMainChecklist() -> finish()
            }
        }
    }

    private fun navigateToChecklistForNotification() {
        replaceChecklistFragment(MainChecklistFragment.newInstance(bottariId), false)
        replaceChecklistFragment(SwipeChecklistFragment.newInstance(), true)
        updateToolbar(false)
    }

    private fun navigateToMainChecklist() {
        val fragment = MainChecklistFragment.newInstance(bottariId)
        replaceChecklistFragment(fragment, false)
        updateToolbar(true)
    }

    private fun navigateToSwipeChecklist() {
        val fragment = SwipeChecklistFragment.newInstance()
        replaceChecklistFragment(fragment, true)
        updateToolbar(false)
    }

    private fun updateToolbar(isVisible: Boolean) {
        binding.btnSwipe.isVisible = isVisible
        val imageRes = if (isVisible) R.drawable.btn_previous else R.drawable.btn_close
        binding.btnPrevious.setImageResource(imageRes)
    }

    private fun navigateToHome() {
        val intent = HomeActivity.newIntent(this@ChecklistActivity)
        startActivity(intent)
        finish()
    }

    private fun replaceChecklistFragment(
        fragment: Fragment,
        addToBackStack: Boolean,
    ) {
        supportFragmentManager.commit {
            setSlideFastAnimation()
            replace(R.id.fcv_checklist, fragment)
            if (addToBackStack) addToBackStack(fragment::class.simpleName)
        }
    }

    private fun isMainChecklist(): Boolean = supportFragmentManager.findFragmentById(R.id.fcv_checklist) is MainChecklistFragment

    private fun FragmentTransaction.setSlideFastAnimation() {
        setCustomAnimations(
            R.anim.slide_in_right_fast,
            R.anim.slide_out_right_fast,
            R.anim.slide_in_right_fast,
            R.anim.slide_out_right_fast,
        )
    }

    private fun showResetChecklistDialog() {
        CustomAlertDialog
            .newInstance(DialogPresetType.RESET_BOTTARI_ITEMS_CHECK_STATE)
            .setDialogListener(
                object : DialogListener {
                    override fun onClickPositive() = viewModel.resetItemsCheckState()

                    override fun onClickNegative() = Unit
                },
            ).show(supportFragmentManager, DialogPresetType.RESET_BOTTARI_ITEMS_CHECK_STATE.name)
    }

    private fun logChecklistFinished(items: List<ChecklistItemUiModel>) {
        BottariLogger.ui(
            UiEventType.CHECKLIST_COMPLETE,
            mapOf(
                "bottari_id" to bottariId,
                "checklist_items" to items.toString(),
            ),
        )
    }

    companion object {
        private const val INVALID_BOTTARI_ID = -1L
        private const val EXTRA_BOTTARI_ID = "EXTRA_BOTTARI_ID"
        private const val EXTRA_BOTTARI_TITLE = "EXTRA_BOTTARI_TITLE"
        private const val EXTRA_NOTIFICATION_FLAG = "EXTRA_FLAG"

        fun newIntent(
            context: Context,
            bottariId: Long,
            bottariTitle: String,
        ): Intent =
            Intent(context, ChecklistActivity::class.java).apply {
                putExtra(EXTRA_BOTTARI_ID, bottariId)
                putExtra(EXTRA_BOTTARI_TITLE, bottariTitle)
            }

        fun newIntentForNotification(
            context: Context,
            bottariId: Long,
            bottariTitle: String,
        ): Intent =
            Intent(context, ChecklistActivity::class.java).apply {
                putExtra(EXTRA_BOTTARI_ID, bottariId)
                putExtra(EXTRA_BOTTARI_TITLE, bottariTitle)
                putExtra(EXTRA_NOTIFICATION_FLAG, true)
            }
    }
}
