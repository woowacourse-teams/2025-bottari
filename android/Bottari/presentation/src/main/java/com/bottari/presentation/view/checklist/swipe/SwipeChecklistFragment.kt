package com.bottari.presentation.view.checklist.swipe

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.bottari.presentation.R
import com.bottari.presentation.base.BaseFragment
import com.bottari.presentation.databinding.FragmentSwipeChecklistBinding
import com.bottari.presentation.extension.getSSAID
import com.bottari.presentation.view.checklist.ChecklistUiEvent
import com.bottari.presentation.view.checklist.ChecklistUiState
import com.bottari.presentation.view.checklist.ChecklistViewModel
import com.bottari.presentation.view.checklist.swipe.adapter.SwipeCheckListAdapter
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.Duration
import com.yuyakaido.android.cardstackview.StackFrom
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting

class SwipeChecklistFragment :
    BaseFragment<FragmentSwipeChecklistBinding>(FragmentSwipeChecklistBinding::inflate),
    CardStackListener {
    private val viewModel: ChecklistViewModel by activityViewModels {
        ChecklistViewModel.Factory(
            requireContext().getSSAID(),
            getBottariId(),
        )
    }
    private val adapter: SwipeCheckListAdapter by lazy { SwipeCheckListAdapter() }
    private lateinit var cardStackLayoutManager: CardStackLayoutManager

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        setupUI()
        setupListener()
    }

    override fun onCardSwiped(direction: Direction?) {
        if (direction != Direction.Right) return
        val index = cardStackLayoutManager.topPosition - INDEX_OFFSET
        val currentItem = adapter.currentList.getOrNull(index) ?: return
        viewModel.toggleItemChecked(currentItem.id)
    }

    override fun onCardAppeared(
        view: View?,
        position: Int,
    ) {
    }

    override fun onCardCanceled() {}

    override fun onCardDisappeared(
        view: View?,
        position: Int,
    ) {
    }

    override fun onCardDragging(
        direction: Direction?,
        ratio: Float,
    ) {
    }

    override fun onCardRewound() {}

    private fun getBottariId(): Long = requireArguments().getLong(EXTRA_BOTTARI_ID)

    private fun setupObserver() {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            adapter.submitList(uiState.nonCheckedItems)
            binding.tvSwipeChecklistStatus.text =
                getString(
                    R.string.swipe_checklist_status_text,
                    uiState.totalQuantity,
                    uiState.totalQuantity - uiState.checkedQuantity,
                )
            handleProgressBar(uiState)
        }
        viewModel.uiEvent.observe(viewLifecycleOwner) { uiEvent ->
            when (uiEvent) {
                ChecklistUiEvent.FetchChecklistFailure -> showSnackbar(R.string.checklist_fetch_failure_text)
                ChecklistUiEvent.CheckItemFailure -> showSnackbar(R.string.checklist_check_faliure_text)
            }
        }
    }

    private fun setupUI() {
        setupCardStackView()
    }

    private fun handleProgressBar(uiState: ChecklistUiState) {
        binding.pbChecklistSwipe.apply {
            progress = uiState.checkedQuantity
            max = uiState.totalQuantity
            if (uiState.isAllChecked) return@apply
            val primaryColor = ContextCompat.getColor(requireContext(), R.color.primary)
            progressTintList = ColorStateList.valueOf(primaryColor)
        }
    }

    private fun setupCardStackView() {
        cardStackLayoutManager =
            CardStackLayoutManager(requireContext(), this).apply {
                setStackFrom(StackFrom.Top)
                setCanScrollVertical(false)
                setCanScrollHorizontal(true)
            }
        binding.csvChecklist.layoutManager = cardStackLayoutManager
        binding.csvChecklist.adapter = adapter
    }

    private fun setupListener() {
        binding.btnSwipeChecklistYes.setOnClickListener {
            swipeCardTo(Direction.Right)
        }
        binding.btnSwipeChecklistNot.setOnClickListener {
            swipeCardTo(Direction.Left)
        }
    }

    private fun swipeCardTo(direction: Direction) {
        val setting =
            SwipeAnimationSetting
                .Builder()
                .setDirection(direction)
                .setDuration(Duration.Normal.duration)
                .build()

        cardStackLayoutManager.setSwipeAnimationSetting(setting)
        binding.csvChecklist.swipe()
    }

    companion object {
        private const val EXTRA_BOTTARI_ID = "EXTRA_BOTTARI_ID"
        private const val INDEX_OFFSET = 1

        fun newBundle(bottariId: Long): Bundle =
            Bundle().apply {
                putLong(EXTRA_BOTTARI_ID, bottariId)
            }
    }
}
