package com.bottari.presentation.view.common.report

import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.graphics.drawable.toDrawable
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.bottari.presentation.R
import com.bottari.presentation.common.extension.getSSAID
import com.bottari.presentation.databinding.DialogReportBinding

class ReportDialog : DialogFragment() {
    private var _binding: DialogReportBinding? = null
    val binding get() = _binding!!

    private val viewModel: ReportViewModel by viewModels {
        ReportViewModel.Factory(
            ssaid = requireContext().getSSAID(),
            templateId = requireArguments().getLong(ARG_TEMPLATE_ID),
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DialogReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupDialog()
        setupObserver()
        setupListeners()
        setupInitialReason(savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupDialog() {
        dialog?.apply {
            setCancelable(false)
            window?.apply {
                setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
                val width = (Resources.getSystem().displayMetrics.widthPixels * WIDTH_RATIO).toInt()
                setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
            }
        }
    }

    private fun setupInitialReason(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) binding.rbSpamOrAd.isChecked = true
    }

    private fun setupObserver() {
        viewModel.uiState.observe(viewLifecycleOwner, ::handleUiState)
        viewModel.uiEvent.observe(viewLifecycleOwner, ::handleUiEvent)
    }

    private fun handleUiState(state: ReportUiState) {
        binding.btnDialogReport.isEnabled = state.isButtonEnabled
    }

    private fun handleUiEvent(event: ReportUiEvent) {
        val messageRes =
            when (event) {
                ReportUiEvent.ReportTemplateSuccess -> R.string.common_report_dialog_submit_success_text
                ReportUiEvent.ReportTemplateFailure -> R.string.common_report_dialog_submit_failure_text
            }
        sendResult(messageRes)
    }

    private fun setupListeners() {
        binding.btnDialogReportClose.setOnClickListener {
            dismiss()
        }

        binding.rgReportReason.setOnCheckedChangeListener { _, checkedId ->
            handleReasonChange(checkedId)
        }

        binding.btnDialogReport.setOnClickListener {
            viewModel.reportTemplate()
        }
    }

    private fun handleReasonChange(checkedId: Int) {
        val selectedText =
            when (checkedId) {
                binding.rbSpamOrAd.id -> binding.rbSpamOrAd.text
                binding.rbPrivacyExposure.id -> binding.rbPrivacyExposure.text
                binding.rbFalseInformation.id -> binding.rbFalseInformation.text
                binding.rbInappropriateContent.id -> binding.rbInappropriateContent.text
                else -> return
            }
        viewModel.updateSelectedReason(selectedText.toString())
    }

    private fun sendResult(
        @StringRes messageRes: Int,
    ) {
        setFragmentResult(
            REQUEST_KEY_REPORT,
            bundleOf(ARG_REPORT_RESULT to messageRes),
        )
        dismiss()
    }

    companion object {
        const val REQUEST_KEY_REPORT = "REQUEST_KEY_REPORT"
        const val ARG_REPORT_RESULT = "ARG_REPORT_RESULT"
        private const val ARG_TEMPLATE_ID = "ARG_TEMPLATE_ID"
        private const val WIDTH_RATIO = 0.9

        fun newInstance(templateId: Long): ReportDialog =
            ReportDialog().apply {
                arguments = bundleOf(ARG_TEMPLATE_ID to templateId)
            }
    }
}
