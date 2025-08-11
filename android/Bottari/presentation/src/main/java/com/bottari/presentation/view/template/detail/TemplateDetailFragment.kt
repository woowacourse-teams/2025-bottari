package com.bottari.presentation.view.template.detail

import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseFragment
import com.bottari.presentation.common.extension.showSnackbar
import com.bottari.presentation.databinding.FragmentTemplateDetailBinding
import com.bottari.presentation.view.common.report.ReportDialog
import com.bottari.presentation.view.edit.personal.PersonalBottariEditActivity
import com.bottari.presentation.view.template.detail.adapter.TemplateDetailAdapter

class TemplateDetailFragment : BaseFragment<FragmentTemplateDetailBinding>(FragmentTemplateDetailBinding::inflate) {
    private val viewModel: TemplateDetailViewModel by viewModels {
        TemplateDetailViewModel.Factory(
            templateId = requireArguments().getLong(ARG_TEMPLATE_ID, INVALID_BOTTARI_ID),
        )
    }

    private val isMyTemplate: Boolean by lazy {
        requireArguments().getBoolean(
            ARG_IS_MY_TEMPLATE,
            false,
        )
    }
    private val adapter by lazy { TemplateDetailAdapter() }
    private val popupMenu by lazy { createPopupMenu() }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupObserver()
        setupListener()
    }

    private fun setupUI() {
        binding.rvTemplateDetail.adapter = adapter
        binding.btnTakeTemplate.isVisible = !isMyTemplate
        popupMenu.menuInflater.inflate(R.menu.template_popup_menu, popupMenu.menu)
    }

    private fun createPopupMenu(): PopupMenu {
        val contextWrapper = ContextThemeWrapper(requireContext(), R.style.CustomPopupMenuText)
        return PopupMenu(
            contextWrapper,
            binding.btnTemplateMore,
            Gravity.CENTER,
            0,
            R.style.CustomPopupMenu,
        )
    }

    private fun setupObserver() {
        viewModel.uiState.observe(viewLifecycleOwner, ::handleUiState)
        viewModel.uiEvent.observe(viewLifecycleOwner, ::handleUiEvent)
    }

    private fun handleUiState(state: TemplateDetailUiState) {
        toggleLoadingIndicator(state.isLoading)
        binding.tvBottariTitle.text = state.title
        adapter.submitList(state.items)
    }

    private fun handleUiEvent(event: TemplateDetailUiEvent) {
        when (event) {
            TemplateDetailUiEvent.FetchBottariDetailFailure -> {
                requireView().showSnackbar(R.string.template_detail_fetch_failure_text)
            }

            TemplateDetailUiEvent.TakeBottariTemplateFailure -> {
                requireView().showSnackbar(R.string.template_detail_take_failure_text)
            }

            is TemplateDetailUiEvent.TakeBottariTemplateSuccess -> {
                navigateToBottariEdit(event.bottariId)
            }
        }
    }

    private fun setupListener() {
        binding.btnPrevious.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.btnTakeTemplate.setOnClickListener {
            viewModel.takeBottariTemplate()
        }

        binding.btnTemplateMore.setOnClickListener {
            popupMenu.show()
        }

        popupMenu.setOnMenuItemClickListener(::handleMenuItemClick)

        parentFragmentManager.setFragmentResultListener(
            ReportDialog.REQUEST_KEY_REPORT,
            viewLifecycleOwner,
        ) { _, bundle -> handleFragmentResult(bundle) }
    }

    private fun handleFragmentResult(bundle: Bundle) {
        val messageRes = bundle.getInt(ReportDialog.ARG_REPORT_RESULT)
        requireView().showSnackbar(messageRes)
    }

    private fun handleMenuItemClick(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.action_report -> {
                showReportDialog()
                true
            }

            else -> false
        }

    private fun showReportDialog() {
        val templateId = viewModel.uiState.value?.templateId ?: return
        ReportDialog
            .newInstance(templateId)
            .show(parentFragmentManager, ReportDialog::class.simpleName)
    }

    private fun navigateToBottariEdit(bottariId: Long?) {
        bottariId ?: return
        startActivity(PersonalBottariEditActivity.newIntent(requireContext(), bottariId, true))
        requireActivity().finish()
    }

    companion object {
        private const val ARG_TEMPLATE_ID = "ARG_BOTTARI_ID"
        private const val ARG_IS_MY_TEMPLATE = "ARG_IS_MY_TEMPLATE"
        private const val INVALID_BOTTARI_ID = -1L

        fun newBundle(
            bottariId: Long,
            isMyTemplate: Boolean,
        ): Bundle =
            Bundle().apply {
                putLong(ARG_TEMPLATE_ID, bottariId)
                putBoolean(ARG_IS_MY_TEMPLATE, isMyTemplate)
            }
    }
}
