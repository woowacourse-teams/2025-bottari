package com.bottari.presentation.view.checklist.main

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseFragment
import com.bottari.presentation.common.extension.showSnackbar
import com.bottari.presentation.databinding.FragmentChecklistBinding
import com.bottari.presentation.view.checklist.ChecklistUiEvent
import com.bottari.presentation.view.checklist.ChecklistViewModel
import com.bottari.presentation.view.checklist.main.adapter.MainChecklistAdapter
import com.bottari.presentation.view.edit.personal.PersonalBottariEditActivity

class MainChecklistFragment : BaseFragment<FragmentChecklistBinding>(FragmentChecklistBinding::inflate) {
    private val bottariId: Long by lazy { requireArguments().getLong(ARG_BOTTARI_ID) }
    private val viewModel: ChecklistViewModel by activityViewModels()
    private val adapter: MainChecklistAdapter by lazy {
        MainChecklistAdapter { viewModel.toggleItemChecked(it) }
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        setupUI()
        setupListener()
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchChecklist()
    }

    private fun setupObserver() {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            toggleLoadingIndicator(uiState.isLoading)
            adapter.submitList(uiState.bottariItems)
            handleEmptyView(uiState.bottariItems.isEmpty())
        }
        viewModel.uiEvent.observe(viewLifecycleOwner) { uiEvent ->
            when (uiEvent) {
                ChecklistUiEvent.FetchChecklistFailure -> requireView().showSnackbar(R.string.checklist_fetch_failure_text)
                ChecklistUiEvent.CheckItemFailure -> requireView().showSnackbar(R.string.checklist_check_failure_text)
            }
        }
    }

    private fun setupUI() {
        binding.rvChecklist.adapter = adapter
        binding.rvChecklist.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupListener() {
        binding.emptyView.btnBottariEdit.setOnClickListener {
            navigateEditView()
        }
    }

    private fun handleEmptyView(isEmpty: Boolean) {
        binding.rvChecklist.isVisible = !isEmpty
        binding.emptyView.clPersonalBottariItemEmptyView.isVisible = isEmpty
    }

    private fun navigateEditView() {
        val intent =
            PersonalBottariEditActivity.newIntent(requireContext(), bottariId, false)
        startActivity(intent)
    }

    companion object {
        private const val ARG_BOTTARI_ID = "ARG_BOTTARI_ID"

        @JvmStatic
        fun newInstance(bottariId: Long): MainChecklistFragment =
            MainChecklistFragment().apply {
                arguments = bundleOf(ARG_BOTTARI_ID to bottariId)
            }
    }
}
