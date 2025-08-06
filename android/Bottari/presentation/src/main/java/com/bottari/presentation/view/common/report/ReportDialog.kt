package com.bottari.presentation.view.common.report

import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.bottari.presentation.R
import com.bottari.presentation.common.extension.getSSAID
import com.bottari.presentation.common.extension.showCustomSnackbar
import com.bottari.presentation.databinding.DialogReportBinding

class ReportDialog : DialogFragment() {
    private val viewModel: ReportViewModel by viewModels {
        ReportViewModel.Factory(
            ssaid = requireContext().getSSAID(),
            templateId = arguments?.getLong(ARG_TEMPLATE_ID)!!,
        )
    }

    private var _binding: DialogReportBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DialogReportBinding.inflate(layoutInflater)
        return binding.root
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

    override fun onStart() {
        super.onStart()
        setupDialog()
    }

    private fun setupObserver() {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            binding.btnDialogReport.isEnabled = uiState.reason.isNotEmpty()
        }

        viewModel.uiEvent.observe(viewLifecycleOwner) { uiEvent ->
            when (uiEvent) {
                ReportUiEvent.ReportTemplateSuccess -> {
                    binding.root.showCustomSnackbar(R.string.common_report_dialog_submit_success_text)
                    dismiss()
                }

                ReportUiEvent.ReportTemplateFailure -> binding.root.showCustomSnackbar(R.string.common_report_dialog_submit_failure_text)
            }
        }
    }

    private fun setupUI() {}

    private fun setupListener() {
        binding.btnDialogReportClose.setOnClickListener { dismiss() }
        binding.rgReportReason.setOnCheckedChangeListener(::handleReasonChange)
        binding.btnDialogReport.setOnClickListener { viewModel.reportTemplate() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleReasonChange(
        view: RadioGroup,
        checkedId: Int,
    ) {
        val reason =
            when (checkedId) {
                binding.rbSpamOrAd.id -> binding.rbSpamOrAd.text
                binding.rbPrivacyExposure.id -> binding.rbPrivacyExposure.text
                binding.rbFalseInformation.id -> binding.rbFalseInformation.text
                binding.rbInappropriateContent.id -> binding.rbInappropriateContent.text
                else -> ""
            }
        viewModel.updateSelectedReason(reason.toString())
    }

    private fun setupDialog() {
        val metrics = Resources.getSystem().displayMetrics
        val width = (metrics.widthPixels * WIDTH_RATIO).toInt()
        dialog?.run {
            setCancelable(false)
            window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
            window?.setLayout(
                width,
                ViewGroup.LayoutParams.WRAP_CONTENT,
            )
        }
    }

    companion object {
        private const val WIDTH_RATIO = 0.9
        private const val ARG_TEMPLATE_ID = "ARG_TEMPLATE_ID"

        fun newInstance(templateId: Long): ReportDialog =
            ReportDialog().apply {
                arguments = Bundle().apply { putLong(ARG_TEMPLATE_ID, templateId) }
            }
    }
}
