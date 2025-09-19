package com.bottari.presentation.view.home.personal

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bottari.domain.model.bottari.BottariType
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseFragment
import com.bottari.presentation.common.extension.fadeIn
import com.bottari.presentation.common.extension.fadeOut
import com.bottari.presentation.common.extension.showSnackbar
import com.bottari.presentation.databinding.FragmentBottariBinding
import com.bottari.presentation.model.alarm.NotificationUiModel
import com.bottari.presentation.model.bottari.personal.BottariUiModel
import com.bottari.presentation.util.AlarmScheduler.cancelAlarm
import com.bottari.presentation.view.checklist.personal.ChecklistActivity
import com.bottari.presentation.view.common.decoration.BottomPaddingDecoration
import com.bottari.presentation.view.create.BottariCreateDialog
import com.bottari.presentation.view.edit.personal.PersonalBottariEditActivity
import com.bottari.presentation.view.home.personal.adapter.BottariAdapter
import com.bottari.presentation.view.home.personal.adapter.BottariViewHolder

class BottariFragment :
    BaseFragment<FragmentBottariBinding>(FragmentBottariBinding::inflate),
    BottariViewHolder.BottariEventListener {
    private val viewModel: BottariViewModel by viewModels {
        BottariViewModel.Factory()
    }
    private val adapter: BottariAdapter by lazy { BottariAdapter(this) }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        setupUI()
        setupListener()
    }

    override fun onStart() {
        super.onStart()
        viewModel.fetchBottaries()
    }

    override fun onBottariClick(
        bottariId: Long,
        bottariTitle: String,
    ) {
        navigateToChecklist(bottariId, bottariTitle)
    }

    override fun onBottariEditClick(bottariId: Long) {
        navigateToEdit(bottariId)
    }

    override fun onBottariDeleteClick(bottari: BottariUiModel) {
        viewModel.deleteBottari(bottari.id)
        if (bottari.alarm == null) return
        cancelAlarm(notification = NotificationUiModel(bottari.id, bottari.title, bottari.alarm))
    }

    private fun setupObserver() {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            binding.emptyView.clBottariEmptyView.isVisible = uiState.isEmpty
            toggleLoadingIndicator(uiState.isLoading)
            adapter.submitList(uiState.bottaries)
        }

        viewModel.uiEvent.observe(viewLifecycleOwner) { uiEvent ->
            when (uiEvent) {
                BottariUiEvent.BottariDeleteSuccess ->
                    requireView().showSnackbar(R.string.bottari_home_delete_success_text)

                BottariUiEvent.BottariDeleteFailure.NotFoundException ->
                    requireView().showSnackbar(
                        "삭제할 보따리를 찾지 못했어요",
                    )

                BottariUiEvent.BottariDeleteFailure.PermissionException ->
                    requireView().showSnackbar(
                        "보따리에 접근할 수 없어요",
                    )

                BottariUiEvent.FetchBottariesFailure.NotFoundException ->
                    requireView().showSnackbar(
                        "보따리를 가져올 수 없어요",
                    )

                BottariUiEvent.DeleteNotificationFailure,
                BottariUiEvent.BottariDeleteFailure.UnexpectedException,
                BottariUiEvent.FetchBottariesFailure.UnexpectedException,
                -> requireView().showSnackbar(R.string.common_unexpected_exception_text)
            }
        }
    }

    private fun setupUI() {
        binding.rvBottari.adapter = adapter
        binding.rvBottari.layoutManager = LinearLayoutManager(requireContext())
        binding.btnBottariCreate.doOnPreDraw {
            binding.rvBottari.addItemDecoration(BottomPaddingDecoration((it.height * PADDING_HEIGHT_RATIO).toInt()))
        }
    }

    private fun setupListener() {
        binding.rvBottari.addOnScrollListener(handleScrollState())
        binding.btnBottariCreate.setOnClickListener {
            BottariCreateDialog
                .newInstance(BottariType.PERSONAL)
                .show(parentFragmentManager, BottariCreateDialog::class.java.name)
        }
    }

    private fun navigateToChecklist(
        bottariId: Long,
        bottariTitle: String,
    ) {
        val intent = ChecklistActivity.newIntent(requireContext(), bottariId, bottariTitle)
        startActivity(intent)
    }

    private fun navigateToEdit(bottariId: Long) {
        val intent = PersonalBottariEditActivity.newIntent(requireContext(), bottariId, false)
        startActivity(intent)
    }

    private fun handleScrollState(): RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(
                recyclerView: RecyclerView,
                newState: Int,
            ) {
                when (newState) {
                    RecyclerView.SCROLL_STATE_DRAGGING,
                    RecyclerView.SCROLL_STATE_SETTLING,
                    -> binding.btnBottariCreate.fadeOut()

                    RecyclerView.SCROLL_STATE_IDLE ->
                        binding.btnBottariCreate.fadeIn()
                }
            }
        }

    companion object {
        private const val PADDING_HEIGHT_RATIO = 1.2f
    }
}
