package com.bottari.presentation.view.home.bottari

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.base.BaseFragment
import com.bottari.presentation.base.UiState
import com.bottari.presentation.databinding.FragmentBottariBinding
import com.bottari.presentation.extension.fadeIn
import com.bottari.presentation.extension.fadeOut
import com.bottari.presentation.extension.getSSAID
import com.bottari.presentation.model.BottariUiModel
import com.bottari.presentation.view.checklist.ChecklistActivity
import com.bottari.presentation.view.edit.personal.PersonalBottariEditActivity
import com.bottari.presentation.view.home.bottari.adapter.BottariAdapter
import com.bottari.presentation.view.home.bottari.create.BottariCreateDialog
import com.bottari.presentation.view.home.bottari.listener.OnBottariClickListener

class BottariFragment :
    BaseFragment<FragmentBottariBinding>(FragmentBottariBinding::inflate),
    OnBottariClickListener {
    private val viewModel: BottariViewModel by viewModels {
        BottariViewModel.Factory(requireContext().getSSAID())
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

    override fun onClick(
        bottariId: Long,
        bottariTitle: String,
    ) {
        navigateToChecklist(bottariId, bottariTitle)
    }

    override fun onMoreClick(bottariId: Long) {
        navigateToEdit(bottariId)
    }

    private fun setupObserver() {
        viewModel.bottaries.observe(viewLifecycleOwner, ::handleBottariState)
    }

    private fun setupUI() {
        binding.rvBottari.adapter = adapter
        binding.rvBottari.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupListener() {
        binding.rvBottari.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(
                    recyclerView: RecyclerView,
                    newState: Int,
                ) {
                    super.onScrollStateChanged(recyclerView, newState)
                    handleScrollState(newState)
                    handleButtonVisibility(newState)
                }
            },
        )

        binding.btnBottariCreate.setOnClickListener {
            BottariCreateDialog().show(parentFragmentManager, BottariCreateDialog::class.java.name)
        }
    }

    private fun handleBottariState(uiState: UiState<List<BottariUiModel>>) {
        when (uiState) {
            is UiState.Loading -> Unit
            is UiState.Success -> adapter.submitList(uiState.data)
            is UiState.Failure -> Unit
        }
    }

    private fun handleScrollState(state: Int) {
        when (state) {
            RecyclerView.SCROLL_STATE_DRAGGING,
            RecyclerView.SCROLL_STATE_SETTLING,
            -> {
                binding.btnBottariCreate.fadeOut()
            }

            RecyclerView.SCROLL_STATE_IDLE -> {
                binding.btnBottariCreate.fadeIn()
            }
        }
    }

    private fun handleButtonVisibility(state: Int) {
        if (!binding.rvBottari.canScrollVertically(LinearLayoutManager.VERTICAL) &&
            state == RecyclerView.SCROLL_STATE_IDLE
        ) {
            binding.btnBottariCreate.isVisible = false
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
        val intent = PersonalBottariEditActivity.newIntent(requireContext(), bottariId)
        startActivity(intent)
    }
}
