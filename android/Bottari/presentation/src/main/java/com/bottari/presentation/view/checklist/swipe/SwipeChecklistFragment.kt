package com.bottari.presentation.view.checklist.swipe

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.bottari.presentation.R
import com.bottari.presentation.base.BaseFragment
import com.bottari.presentation.databinding.FragmentSwipeChecklistBinding
import com.bottari.presentation.extension.takeSuccess
import com.bottari.presentation.model.ItemUiModel
import com.bottari.presentation.view.checklist.ChecklistViewModel
import com.bottari.presentation.view.checklist.swipe.adapter.SwipeCheckListAdapter
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.StackFrom

class SwipeChecklistFragment :
    BaseFragment<FragmentSwipeChecklistBinding>(FragmentSwipeChecklistBinding::inflate),
    CardStackListener {
    private val viewModel: ChecklistViewModel by activityViewModels()
    private val adapter: SwipeCheckListAdapter by lazy { SwipeCheckListAdapter() }
    private lateinit var cardStackLayoutManager: CardStackLayoutManager
    private val totalItemCount by lazy {
        viewModel.checklist.value
            ?.takeSuccess()
            ?.size ?: DEFAULT_PROGRESS_MAX_VALUE
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        setupUI()
        viewModel.filterNonChecklist()
    }

    override fun onCardSwiped(direction: Direction?) {
        if (direction == Direction.Right) {
            val index = cardStackLayoutManager.topPosition - INDEX_OFFSET
            val currentItem = adapter.currentList.getOrNull(index) ?: return
            viewModel.checkItem(currentItem.id)
        }
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

    private fun setupObserver() {
        viewModel.nonChecklist.observe(viewLifecycleOwner, ::handleNonChecklistState)
        viewModel.checkedQuantity.observe(viewLifecycleOwner, ::handleCheckedState)
        viewModel.isAllChecked.observe(viewLifecycleOwner, ::handleProgressBarState)
    }

    private fun setupUI() {
        setupCardSwipeViewAdapter()
        binding.pbChecklistSwipe.max = totalItemCount
    }

    private fun handleNonChecklistState(items: List<ItemUiModel>) {
        adapter.submitList(items)
    }

    private fun handleCheckedState(checkedQuantity: Int) {
        binding.pbChecklistSwipe.progress = checkedQuantity
        binding.tvSwipeChecklistStatus.text =
            getString(
                R.string.swipe_checklist_status_text,
                totalItemCount,
                totalItemCount - checkedQuantity,
            )
    }

    private fun handleProgressBarState(isAllChecked: Boolean) {
        if (!isAllChecked) return
        binding.pbChecklistSwipe.progressTintList =
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.primary,
                ),
            )
    }

    private fun setupCardSwipeViewAdapter() {
        binding.csvChecklist.adapter = adapter
        cardStackLayoutManager =
            CardStackLayoutManager(requireContext(), this).apply {
                setStackFrom(StackFrom.Top)
                setCanScrollVertical(false)
                setCanScrollHorizontal(true)
            }
        binding.csvChecklist.layoutManager = cardStackLayoutManager
    }

    companion object {
        private const val INDEX_OFFSET = 1
        private const val DEFAULT_PROGRESS_MAX_VALUE = 0
    }
}
