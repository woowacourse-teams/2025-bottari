package com.bottari.presentation.view.home.market

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bottari.presentation.base.BaseFragment
import com.bottari.presentation.base.UiState
import com.bottari.presentation.databinding.FragmentMarketBinding
import com.bottari.presentation.model.BottariTemplateUiModel
import com.bottari.presentation.view.home.market.adapter.MarketAdapter

class MarketFragment : BaseFragment<FragmentMarketBinding>(FragmentMarketBinding::inflate) {
    private val viewModel: MarketViewModel by viewModels {
        MarketViewModel.Factory()
    }
    private val adapter: MarketAdapter by lazy { MarketAdapter(::navigateToBottariTemplateDetail) }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        setupUI()
    }

    private fun setupObserver() {
        viewModel.bottariTemplates.observe(viewLifecycleOwner, ::handleBottariTemplateState)
    }

    private fun setupUI() {
        binding.rvBottariTemplate.adapter = adapter
        binding.rvBottariTemplate.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun handleBottariTemplateState(uiState: UiState<List<BottariTemplateUiModel>>) {
        when (uiState) {
            is UiState.Loading -> Unit
            is UiState.Success -> adapter.submitList(uiState.data)
            is UiState.Failure -> Unit
        }
    }

    private fun navigateToBottariTemplateDetail(bottariTemplateId: Long) {
    }
}
