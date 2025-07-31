package com.bottari.presentation.view.template.my

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseFragment
import com.bottari.presentation.common.extension.getSSAID
import com.bottari.presentation.databinding.FragmentMyBottariTemplateBinding
import com.bottari.presentation.view.template.TemplateNavigator
import com.bottari.presentation.view.template.my.adapter.MyTemplateAdapter
import com.bottari.presentation.view.template.my.listener.MyBottariTemplateEventListener

class MyTemplateFragment :
    BaseFragment<FragmentMyBottariTemplateBinding>(FragmentMyBottariTemplateBinding::inflate),
    MyBottariTemplateEventListener {
    private val viewModel: MyTemplateViewModel by viewModels {
        MyTemplateViewModel.Factory(
            requireContext().getSSAID(),
        )
    }
    private val adapter: MyTemplateAdapter by lazy { MyTemplateAdapter(this) }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        setupUI()
        setupListener()
    }

    override fun onDeleteClick(bottariTemplateId: Long) {
        viewModel.deleteBottariTemplate(bottariTemplateId)
    }

    override fun onDetailClick(bottariTemplateId: Long) {
        (requireActivity() as? TemplateNavigator)?.navigateToDetail(bottariTemplateId, true)
    }

    private fun setupObserver() {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            toggleLoadingIndicator(uiState.isLoading)
            adapter.submitList(uiState.bottariTemplates)
        }

        viewModel.uiEvent.observe(viewLifecycleOwner) { uiEvent ->
            when (uiEvent) {
                MyTemplateUiEvent.FetchMyTemplateFailure -> showSnackbar(R.string.template_my_template_fetch_failure_text)
                MyTemplateUiEvent.DeleteMyTemplateFailure -> showSnackbar(R.string.template_my_template_delete_failure_text)
                MyTemplateUiEvent.DeleteMyTemplateSuccess -> showSnackbar(R.string.template_my_template_delete_success_text)
            }
        }
    }

    private fun setupUI() {
        binding.rvMyBottariTemplate.adapter = adapter
    }

    private fun setupListener() {
        binding.btnPrevious.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    companion object {
        private const val ARG_BOTTARI_TEMPLATE_ID = "ARG_BOTTARI_TEMPLATE_ID"

        fun newBundle(bottariTemplateId: Long): Bundle =
            Bundle().apply {
                putLong(ARG_BOTTARI_TEMPLATE_ID, bottariTemplateId)
            }
    }
}
