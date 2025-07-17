package com.bottari.presentation.view.home.bottari

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bottari.presentation.R
import com.bottari.presentation.base.BaseFragment
import com.bottari.presentation.base.UiState
import com.bottari.presentation.databinding.FragmentBottariBinding
import com.bottari.presentation.model.BottariUiModel
import com.bottari.presentation.view.home.bottari.adapter.BottariAdapter

class BottariFragment : BaseFragment<FragmentBottariBinding>(FragmentBottariBinding::inflate) {
    private val viewModel: BottariViewModel by viewModels()
    private val adapter: BottariAdapter by lazy { BottariAdapter() }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        setupUI()
    }

    private fun setupObserver() {
        viewModel.bottaries.observe(viewLifecycleOwner) { uiState -> handleBottariState(uiState) }
    }

    private fun setupUI() {
        binding.rvBottari.adapter = adapter
        binding.rvBottari.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun handleBottariState(uiState: UiState<List<BottariUiModel>>) {
        when (uiState) {
            is UiState.Loading -> showSnackbar(R.string.home_nav_market_title)
            is UiState.Success -> adapter.submitList(uiState.data)
            is UiState.Failure -> showSnackbar(R.string.home_nav_profile_title)
        }
    }
}
