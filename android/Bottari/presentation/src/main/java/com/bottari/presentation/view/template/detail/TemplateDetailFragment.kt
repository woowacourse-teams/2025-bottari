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
import com.bottari.presentation.compose.edit.personal.ComposePersonalBottariEditActivity
import com.bottari.presentation.databinding.FragmentTemplateDetailBinding
import com.bottari.presentation.view.common.report.ReportDialog
import com.bottari.presentation.view.template.detail.adapter.TemplateDetailAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TemplateDetailFragment : BaseFragment<FragmentTemplateDetailBinding>(FragmentTemplateDetailBinding::inflate) {
    private val viewModel: TemplateDetailViewModel by viewModels()
    private val isMyTemplate: Boolean by lazy {
        requireArguments().getBoolean(ARG_IS_MY_TEMPLATE, false)
    }
    private val isBookmark: Boolean by lazy {
        requireArguments().getBoolean(TemplateDetailViewModel.KEY_IS_BOOKMARK, false)
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
//        binding.btnTakeTemplate.isVisible = !isMyTemplate
        popupMenu.menuInflater.inflate(R.menu.template_popup_menu, popupMenu.menu)
        binding.btnTemplateMore.isVisible = isBookmark.not() && isMyTemplate.not()
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
        collectWithLifecycle(viewModel.uiEvent) { event -> handleUiEvent(event) }
        collectWithLifecycle(viewModel.uiState) { state -> handleUiState(state) }
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
        startActivity(
            ComposePersonalBottariEditActivity.newIntent(
                context = requireContext(),
                bottariId = bottariId,
                isNewBottari = true,
            ),
        )
        requireActivity().finish()
    }

    companion object {
        private const val ARG_IS_MY_TEMPLATE = "ARG_IS_MY_TEMPLATE"

        fun newBundle(
            templateId: Long,
            isMyTemplate: Boolean,
            isBookmark: Boolean,
        ): Bundle =
            Bundle().apply {
                putLong(TemplateDetailViewModel.KEY_TEMPLATE_ID, templateId)
                putBoolean(ARG_IS_MY_TEMPLATE, isMyTemplate)
                putBoolean(TemplateDetailViewModel.KEY_IS_BOOKMARK, isBookmark)
            }
    }
}
