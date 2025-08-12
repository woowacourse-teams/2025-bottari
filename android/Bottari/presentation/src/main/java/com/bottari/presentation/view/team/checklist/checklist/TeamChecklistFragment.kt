package com.bottari.presentation.view.team.checklist.checklist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bottari.presentation.common.base.BaseFragment
import com.bottari.presentation.databinding.FragmentTeamChecklistBinding
import com.bottari.presentation.view.team.checklist.checklist.adapter.TeamChecklistItemAdapter

class TeamChecklistFragment : BaseFragment<FragmentTeamChecklistBinding>(FragmentTeamChecklistBinding::inflate) {
    private val viewModel: TeamChecklistViewModel by activityViewModels()

    private val checklistAdapter: TeamChecklistItemAdapter by lazy {
        TeamChecklistItemAdapter(
            onParentClick = { parent ->
                viewModel.toggleParentExpanded(parent.category)
            },
            onChildClick = { item ->
                viewModel.toggleItemChecked(item.id)
            },
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        binding.rvChecklist.adapter = checklistAdapter
        binding.rvChecklist.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeViewModel() {
        viewModel.uiState.observe(viewLifecycleOwner) {
            checklistAdapter.submitList(it.expandableItems)
        }
    }
}
