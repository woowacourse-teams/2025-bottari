package com.bottari.presentation.view.home.team

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bottari.domain.model.bottari.BottariType
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseFragment
import com.bottari.presentation.common.extension.fadeIn
import com.bottari.presentation.common.extension.fadeOut
import com.bottari.presentation.common.extension.showSnackbar
import com.bottari.presentation.databinding.FragmentTeamBottariBinding
import com.bottari.presentation.view.checklist.team.TeamChecklistActivity
import com.bottari.presentation.view.common.decoration.BottomPaddingDecoration
import com.bottari.presentation.view.create.BottariCreateDialog
import com.bottari.presentation.view.edit.team.TeamBottariEditActivity
import com.bottari.presentation.view.home.team.adapter.TeamBottariAdapter
import com.bottari.presentation.view.home.team.adapter.TeamBottariViewHolder
import com.bottari.presentation.view.join.TeamBottariJoinDialog

class TeamBottariFragment :
    BaseFragment<FragmentTeamBottariBinding>(FragmentTeamBottariBinding::inflate),
    TeamBottariViewHolder.BottariEventListener {
    private val viewModel: TeamBottariViewModel by viewModels {
        TeamBottariViewModel.Factory()
    }
    private val adapter: TeamBottariAdapter by lazy { TeamBottariAdapter(this) }

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

    override fun onBottariDeleteClick(bottariId: Long) {
        viewModel.deleteBottari(bottariId)
    }

    private fun setupObserver() {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            binding.emptyView.clBottariEmptyView.isVisible = uiState.isEmpty
            toggleLoadingIndicator(uiState.isLoading)
            adapter.submitList(uiState.bottaries) {
                binding.rvTeamBottari.invalidateItemDecorations()
            }
        }

        viewModel.uiEvent.observe(viewLifecycleOwner) { uiEvent ->
            val message =
                when (uiEvent) {
                    TeamBottariUiEvent.BottariDeleteFailure -> R.string.bottari_home_delete_failure_text
                    TeamBottariUiEvent.BottariDeleteSuccess -> R.string.bottari_home_delete_success_text
                    TeamBottariUiEvent.FetchBottariesFailure -> R.string.bottari_home_fetch_failure_text
                }
            requireView().showSnackbar(message)
        }
    }

    private fun setupUI() {
        binding.rvTeamBottari.adapter = adapter
        binding.rvTeamBottari.layoutManager = LinearLayoutManager(requireContext())
        binding.btnExpand.doOnPreDraw {
            binding.rvTeamBottari.addItemDecoration(BottomPaddingDecoration((it.height * PADDING_HEIGHT_RATIO).toInt()))
        }
    }

    private fun setupListener() {
        binding.rvTeamBottari.addOnScrollListener(handleScrollState())
        binding.btnTeamBottariCreate.setOnClickListener {
            binding.expandableTeamBottari.collapse()
            BottariCreateDialog
                .newInstance(BottariType.TEAM)
                .show(parentFragmentManager, BottariCreateDialog::class.java.name)
        }
        binding.btnTeamBottariJoin.setOnClickListener {
            binding.expandableTeamBottari.collapse()
            TeamBottariJoinDialog
                .newInstance()
                .show(parentFragmentManager, TeamBottariJoinDialog::class.java.name)
        }
        setFragmentResultListener(REQUEST_KEY_REQUIRE_REFRESH) { _, _ ->
            viewModel.fetchBottaries()
        }
    }

    private fun navigateToChecklist(
        bottariId: Long,
        bottariTitle: String,
    ) {
        val intent =
            TeamChecklistActivity.newIntent(
                requireContext(),
                bottariId,
                bottariTitle,
            )
        startActivity(intent)
    }

    private fun navigateToEdit(bottariId: Long) {
        val intent = TeamBottariEditActivity.newIntent(requireContext(), bottariId, false)
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
                    -> binding.expandableTeamBottari.fadeOut()

                    RecyclerView.SCROLL_STATE_IDLE ->
                        binding.expandableTeamBottari.fadeIn()
                }
            }
        }

    companion object {
        const val REQUEST_KEY_REQUIRE_REFRESH = "REFRESH"
        private const val PADDING_HEIGHT_RATIO = 1.2f
    }
}
