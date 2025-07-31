package com.bottari.presentation.view.home.market

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseFragment
import com.bottari.presentation.databinding.FragmentMarketBinding
import com.bottari.presentation.view.home.market.adapter.TemplateAdapter
import com.bottari.presentation.view.home.market.listener.OnTemplateClickListener
import com.bottari.presentation.view.template.TemplateActivity

class TemplateFragment :
    BaseFragment<FragmentMarketBinding>(FragmentMarketBinding::inflate),
    TextWatcher,
    OnTemplateClickListener {
    private val viewModel: TemplateViewModel by viewModels {
        TemplateViewModel.Factory()
    }
    private val adapter: TemplateAdapter by lazy { TemplateAdapter(this) }

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

    override fun onTemplateClick(bottariTemplateId: Long) {
        navigateToDetail(bottariTemplateId)
    }

    private fun setupObserver() {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            toggleLoadingIndicator(uiState.isLoading)
            adapter.submitList(uiState.templates)
        }
        viewModel.uiEvent.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                TemplateUiEvent.FetchBottariTemplatesFailure -> showSnackbar(R.string.template_fetch_template_failure_text)
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
        val intent = TemplateActivity.newIntentForDetail(requireContext(), bottariTemplateId)
        startActivity(intent)
    }

    private fun navigateToMyBottariTemplate() {
        val intent = TemplateActivity.newIntentForMyTemplate(requireContext())
        startActivity(intent)
    }
}
