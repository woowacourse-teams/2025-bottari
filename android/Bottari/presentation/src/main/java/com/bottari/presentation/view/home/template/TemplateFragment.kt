package com.bottari.presentation.view.home.template

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseFragment
import com.bottari.presentation.common.extension.fadeIn
import com.bottari.presentation.common.extension.fadeOut
import com.bottari.presentation.common.extension.showSnackbar
import com.bottari.presentation.databinding.FragmentTemplateBinding
import com.bottari.presentation.view.common.decoration.BottomPaddingDecoration
import com.bottari.presentation.view.home.template.adapter.TemplateAdapter
import com.bottari.presentation.view.home.template.listener.OnTemplateClickListener
import com.bottari.presentation.view.template.TemplateActivity

class TemplateFragment :
    BaseFragment<FragmentTemplateBinding>(FragmentTemplateBinding::inflate),
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
                TemplateUiEvent.FetchBottariTemplatesFailure ->
                    requireView().showSnackbar(
                        R.string.template_fetch_template_failure_text,
                    )
            }
        }
    }

    private fun setupUI() {
        binding.rvBottariTemplate.adapter = adapter
        binding.rvBottariTemplate.layoutManager = LinearLayoutManager(requireContext())
        binding.btnTemplateCreate.doOnPreDraw {
            binding.rvBottariTemplate.addItemDecoration(BottomPaddingDecoration((it.height * PADDING_HEIGHT_RATIO).toInt()))
        }
    }

    private fun setupListener() {
        binding.etBottariTemplateTitle.addTextChangedListener(this)
        binding.btnMyBottariTemplate.setOnClickListener { navigateToMyBottariTemplate() }
        binding.btnTemplateCreate.setOnClickListener { navigateToCreateTemplate() }
        binding.rvBottariTemplate.addOnScrollListener(handleScrollState())
    }

    private fun navigateToDetail(bottariTemplateId: Long) {
        val intent = TemplateActivity.newIntentForDetail(requireContext(), bottariTemplateId)
        startActivity(intent)
    }

    private fun navigateToMyBottariTemplate() {
        val intent = TemplateActivity.newIntentForMyTemplate(requireContext())
        startActivity(intent)
    }

    private fun navigateToCreateTemplate() {
        val intent = TemplateActivity.newIntentForCreateTemplate(requireContext())
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
                    -> binding.btnTemplateCreate.fadeOut()

                    RecyclerView.SCROLL_STATE_IDLE -> binding.btnTemplateCreate.fadeIn()
                }
            }
        }

    companion object {
        private const val PADDING_HEIGHT_RATIO = 1.4f
    }
}
