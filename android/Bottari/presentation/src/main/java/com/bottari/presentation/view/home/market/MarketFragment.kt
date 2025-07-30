package com.bottari.presentation.view.home.market

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bottari.presentation.R
import com.bottari.presentation.base.BaseFragment
import com.bottari.presentation.databinding.FragmentMarketBinding
import com.bottari.presentation.view.home.market.adapter.MarketAdapter
import com.bottari.presentation.view.home.market.listener.OnBottariTemplateClickListener
import com.bottari.presentation.view.market.MarketActivity

class MarketFragment :
    BaseFragment<FragmentMarketBinding>(FragmentMarketBinding::inflate),
    TextWatcher,
    OnBottariTemplateClickListener {
    private val viewModel: MarketViewModel by viewModels {
        MarketViewModel.Factory()
    }
    private val adapter: MarketAdapter by lazy { MarketAdapter(this) }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        setupUI()
        setupListener()
    }

    override fun afterTextChanged(s: Editable?) {}

    override fun beforeTextChanged(
        s: CharSequence?,
        start: Int,
        count: Int,
        after: Int,
    ) {
    }

    override fun onTextChanged(
        s: CharSequence?,
        start: Int,
        before: Int,
        count: Int,
    ) {
        val inputText = s?.toString()?.trim().orEmpty()
        viewModel.searchTemplates(inputText)
    }

    override fun onBottariTemplateClick(bottariTemplateId: Long) {
        navigateToDetail(bottariTemplateId)
    }

    private fun setupObserver() {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            adapter.submitList(uiState.templates)
        }
        viewModel.uiEvent.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                MarketUiEvent.FetchBottariTemplatesFailure -> showSnackbar(R.string.bottari_template_fetch_failure_text)
            }
        }
    }

    private fun setupUI() {
        binding.rvBottariTemplate.adapter = adapter
        binding.rvBottariTemplate.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupListener() {
        binding.etBottariTemplateTitle.addTextChangedListener(this)
        binding.btnMyBottariTemplate.setOnClickListener { navigateToMyBottariTemplate() }
    }

    private fun navigateToDetail(bottariTemplateId: Long) {
        val intent = MarketActivity.newIntentForDetail(requireContext(), bottariTemplateId)
        startActivity(intent)
    }

    private fun navigateToMyBottariTemplate() {
        val intent = MarketActivity.newIntentForMyTemplate(requireContext())
        startActivity(intent)
    }
}
