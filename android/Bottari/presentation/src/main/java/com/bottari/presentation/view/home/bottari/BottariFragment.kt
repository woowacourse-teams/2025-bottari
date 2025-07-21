package com.bottari.presentation.view.home.bottari

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.R
import com.bottari.presentation.base.BaseFragment
import com.bottari.presentation.base.UiState
import com.bottari.presentation.databinding.FragmentBottariBinding
import com.bottari.presentation.extension.fadeIn
import com.bottari.presentation.extension.fadeOut
import com.bottari.presentation.model.BottariUiModel
import com.bottari.presentation.view.checklist.ChecklistActivity
import com.bottari.presentation.view.edit.personal.main.PersonalBottariEditActivity
import com.bottari.presentation.view.home.bottari.adapter.BottariAdapter
import com.bottari.presentation.view.home.bottari.create.BottariCreateDialog
import com.bottari.presentation.view.home.bottari.listener.OnBottariClickListener

class BottariFragment : BaseFragment<FragmentBottariBinding>(FragmentBottariBinding::inflate) {
    private val viewModel: BottariViewModel by viewModels()
    private val adapter: BottariAdapter by lazy {
        BottariAdapter(
            object : OnBottariClickListener {
                override fun onClick(bottariId: Long) {
                    navigateToChecklist(bottariId)
                }

                override fun onMoreClick(bottariId: Long) {
                    navigateToEdit(bottariId)
                }
            },
        )
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

    private fun setupObserver() {
        viewModel.bottaries.observe(viewLifecycleOwner) { uiState -> handleBottariState(uiState) }
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
                }
            },
        )

        binding.btnBottariCreate.setOnClickListener {
            BottariCreateDialog().show(parentFragmentManager, BottariCreateDialog::class.java.name)
        }
    }

    private fun handleBottariState(uiState: UiState<List<BottariUiModel>>) {
        when (uiState) {
            is UiState.Loading -> showSnackbar(R.string.home_nav_market_title)
            is UiState.Success -> adapter.submitList(uiState.data)
            is UiState.Failure -> showSnackbar(R.string.home_nav_profile_title)
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

    private fun navigateToChecklist(bottariId: Long) {
        val intent = ChecklistActivity.newIntent(requireContext(), bottariId)
        startActivity(intent)
    }

    private fun navigateToEdit(bottariId: Long) {
        val intent = PersonalBottariEditActivity.newIntent(requireContext(), bottariId)
        startActivity(intent)
    }
}
