package com.bottari.presentation.view.home.team

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
import com.bottari.presentation.databinding.FragmentTeamBottariBinding
import com.bottari.presentation.view.checklist.ChecklistActivity
import com.bottari.presentation.view.common.decoration.BottomPaddingDecoration
import com.bottari.presentation.view.create.BottariCreateDialog
import com.bottari.presentation.view.edit.personal.PersonalBottariEditActivity
import com.bottari.presentation.view.home.team.adapter.TeamBottariAdapter
import com.bottari.presentation.view.home.team.adapter.TeamBottariViewHolder

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
            adapter.submitList(uiState.bottaries)
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
                .newInstance(BottariType.TEAM)
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
