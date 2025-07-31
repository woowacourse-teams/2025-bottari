package com.bottari.presentation.view.template.detail

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseFragment
import com.bottari.presentation.common.extension.getSSAID
import com.bottari.presentation.databinding.FragmentTemplateDetailBinding
import com.bottari.presentation.view.edit.personal.PersonalBottariEditActivity
import com.bottari.presentation.view.template.detail.adapter.TemplateDetailAdapter

class TemplateDetailFragment : BaseFragment<FragmentTemplateDetailBinding>(FragmentTemplateDetailBinding::inflate) {
    private val viewModel: TemplateDetailViewModel by viewModels {
        TemplateDetailViewModel.Factory(
            ssaid = requireContext().getSSAID(),
            templateId = getBottariId(),
        )
    }
    private val adapter by lazy { TemplateDetailAdapter() }

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
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            toggleLoadingIndicator(uiState.isLoading)
            binding.tvBottariTitle.text = uiState.title
            adapter.submitList(uiState.items)
        }
        viewModel.uiEvent.observe(viewLifecycleOwner) { uiEvent ->
            when (uiEvent) {
                TemplateDetailUiEvent.FetchBottariDetailFailure -> showSnackbar(R.string.template_detail_fetch_failure_text)
                TemplateDetailUiEvent.TakeBottariTemplateFailure -> showSnackbar(R.string.template_detail_take_failure_text)
                is TemplateDetailUiEvent.TakeBottariTemplateSuccess ->
                    navigateBottariEdit(
                        uiEvent.bottariId,
                    )
            }
        }
    }

    private fun setupUI() {
        binding.rvTemplateDetail.adapter = adapter
        if (getIsMyTemplate()) binding.btnTakeTemplate.isVisible = false
    }

    private fun setupListener() {
        binding.btnPrevious.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.btnTakeTemplate.setOnClickListener {
            viewModel.takeBottariTemplate()
        }
    }

    private fun getBottariId(): Long = requireArguments().getLong(ARG_BOTTARI_ID, INVALID_BOTTARI_ID)

    private fun getIsMyTemplate(): Boolean = requireArguments().getBoolean(ARG_IS_MY_TEMPLATE, false)

    private fun navigateBottariEdit(bottariId: Long?) {
        if (bottariId == null) return
        startActivity(PersonalBottariEditActivity.newIntent(requireContext(), bottariId, true))
        requireActivity().finish()
    }

    companion object {
        private const val ARG_BOTTARI_ID = "ARG_BOTTARI_ID"
        private const val ARG_IS_MY_TEMPLATE = "ARG_IS_MY_TEMPLATE"
        private const val INVALID_BOTTARI_ID = -1L

        fun newBundle(
            bottariId: Long,
            isMyTemplate: Boolean,
        ): Bundle =
            Bundle().apply {
                putLong(ARG_BOTTARI_ID, bottariId)
                putBoolean(ARG_IS_MY_TEMPLATE, isMyTemplate)
            }
    }
}
