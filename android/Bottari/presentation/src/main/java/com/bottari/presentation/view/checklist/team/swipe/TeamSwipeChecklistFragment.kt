package com.bottari.presentation.view.checklist.team.swipe

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseFragment
import com.bottari.presentation.common.extension.showSnackbar
import com.bottari.presentation.databinding.FragmentSwipeChecklistBinding
import com.bottari.presentation.model.TeamChecklistProductUiModel
import com.bottari.presentation.view.checklist.team.main.checklist.TeamChecklistUiEvent
import com.bottari.presentation.view.checklist.team.main.checklist.TeamChecklistUiState
import com.bottari.presentation.view.checklist.team.main.checklist.TeamChecklistViewModel
import com.bottari.presentation.view.checklist.team.swipe.adapter.TeamSwipeChecklistAdapter
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.Duration
import com.yuyakaido.android.cardstackview.StackFrom
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting

class TeamSwipeChecklistFragment :
    BaseFragment<FragmentSwipeChecklistBinding>(FragmentSwipeChecklistBinding::inflate),
    CardStackListener {
    private val viewModel: TeamChecklistViewModel by activityViewModels {
        TeamChecklistViewModel.Factory(requireArguments().getLong(ARG_BOTTARI_ID))
    }
    private val adapter: TeamSwipeChecklistAdapter by lazy { TeamSwipeChecklistAdapter() }
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

    override fun onCardAppeared(
        view: View?,
        position: Int,
    ) {
    }

    override fun onCardCanceled() {
    }

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

    override fun onCardRewound() {
    }

    override fun onCardSwiped(direction: Direction?) {
        val index = cardStackLayoutManager.topPosition - INDEX_OFFSET
        val currentItem = adapter.currentList.getOrNull(index) ?: return
        if (direction == Direction.Right) {
            viewModel.toggleItemChecked(
                currentItem.id,
                currentItem.type,
            )
        }
        viewModel.addSwipedItem(currentItem)
    }

    private fun setupObserver() {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            toggleLoadingIndicator(uiState.isLoading)
            handleSwipeChecklistStatus(uiState)
            handleProgressBar(uiState)
            updateSwipeList(uiState.nonSwipedItems)
            handleCompleteView(uiState)
            handleEmptyView(uiState.isItemsEmpty)
        }
        viewModel.uiEvent.observe(viewLifecycleOwner) { uiEvent ->
            when (uiEvent) {
                TeamChecklistUiEvent.CheckItemFailure -> requireView().showSnackbar(R.string.checklist_check_failure_text)
                TeamChecklistUiEvent.FetchChecklistFailure -> requireView().showSnackbar(R.string.checklist_fetch_failure_text)
            }
        }
    }

    private fun setupUI() {
        setupCardStackView()
    }

    private fun setupListener() {
        binding.btnSwipeChecklistYes.setOnClickListener {
            swipeCardTo(Direction.Right)
        }
        binding.btnSwipeChecklistNot.setOnClickListener {
            swipeCardTo(Direction.Left)
        }
        binding.btnSwipeChecklistReturn.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun handleSwipeChecklistStatus(uiState: TeamChecklistUiState) {
        binding.tvSwipeChecklistStatus.text =
            getString(
                R.string.common_format_checklist_swipe_status,
                uiState.totalQuantity,
                uiState.unCheckedQuantity,
            )
    }

    private fun handleCompleteView(uiState: TeamChecklistUiState) {
        if (uiState.isAllSwiped) {
            showCompleteState(isComplete = uiState.isAllChecked)
        }
    }

    private fun handleEmptyView(isEmpty: Boolean) {
        if (!isEmpty) return
        showDoneButton()
        showEmptyView()
    }

    private fun showCompleteState(isComplete: Boolean) {
        showDoneButton()
        binding.viewSwipeCompleteNotAll.clSwipeCompleteNotAll.isVisible = !isComplete
        binding.viewSwipeCompleteAll.clSwipeCompleteAll.isVisible = isComplete
    }

    private fun showDoneButton() {
        binding.btnSwipeChecklistNot.isVisible = false
        binding.btnSwipeChecklistYes.isVisible = false
        binding.btnSwipeChecklistReturn.isVisible = true
    }

    private fun showEmptyView() {
        binding.viewSwipeEmpty.clSwipeEmpty.isVisible = true
    }

    private fun updateSwipeList(items: List<TeamChecklistProductUiModel>) {
        adapter.submitList(items)
    }

    private fun handleProgressBar(uiState: TeamChecklistUiState) {
        binding.pbChecklistSwipe.apply {
            progress = uiState.checkedQuantity
            max = uiState.totalQuantity
            if (uiState.isAllChecked.not()) return@apply
            val primaryColor = ContextCompat.getColor(requireContext(), R.color.primary)
            progressTintList = ColorStateList.valueOf(primaryColor)
        }
    }

    private fun setupCardStackView() {
        viewModel.resetSwipeState()
        cardStackLayoutManager =
            CardStackLayoutManager(requireContext(), this).apply {
                setStackFrom(StackFrom.Top)
                setCanScrollVertical(false)
                setCanScrollHorizontal(true)
            }
        binding.csvChecklist.layoutManager = cardStackLayoutManager
        binding.csvChecklist.adapter = adapter
        binding.csvChecklist.itemAnimator = null
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
        private const val ARG_BOTTARI_ID = "ARG_BOTTARI_ID"
        private const val INDEX_OFFSET = 1

        fun newInstance(bottariId: Long): TeamSwipeChecklistFragment =
            TeamSwipeChecklistFragment().apply {
                arguments = bundleOf(ARG_BOTTARI_ID to bottariId)
            }
    }
}
