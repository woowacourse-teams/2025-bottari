package com.bottari.presentation.view.checklist.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bottari.presentation.base.BaseFragment
import com.bottari.presentation.base.UiState
import com.bottari.presentation.databinding.FragmentChecklistBinding
import com.bottari.presentation.extension.getSSAID
import com.bottari.presentation.model.BottariItemUiModel
import com.bottari.presentation.view.checklist.ChecklistViewModel
import com.bottari.presentation.view.checklist.main.adapter.MainChecklistAdapter

class MainChecklistFragment :
    BaseFragment<FragmentChecklistBinding>(FragmentChecklistBinding::inflate) {
    private val viewModel: ChecklistViewModel by activityViewModels {
        ChecklistViewModel.Factory(
            requireContext().getSSAID(),
            getBottariId()
        )
    }
    private val adapter: MainChecklistAdapter by lazy {
        MainChecklistAdapter { viewModel.checkItem(it) }
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

    private fun getBottariId(): Long = requireArguments().getLong(EXTRA_BOTTARI_ID)

    private fun setupObserver() {
        viewModel.checklist.observe(viewLifecycleOwner) { uiState -> handleChecklistState(uiState) }
    }

    private fun setupUI() {
        binding.rvChecklist.adapter = adapter
        binding.rvChecklist.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupListener() {
    }

    private fun handleChecklistState(uiState: UiState<List<BottariItemUiModel>>) {
        when (uiState) {
            is UiState.Loading -> Unit
            is UiState.Success -> {
                adapter.submitList(uiState.data)
            }

            is UiState.Failure -> Unit
        }
    }

    companion object {
        private const val EXTRA_BOTTARI_ID = "EXTRA_BOTTARI_ID"

        fun newBundle(bottariId: Long): Bundle = Bundle().apply {
            putLong(EXTRA_BOTTARI_ID, bottariId)
        }
    }
}
