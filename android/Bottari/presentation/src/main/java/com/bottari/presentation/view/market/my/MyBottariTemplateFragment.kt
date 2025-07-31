package com.bottari.presentation.view.market.my

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseFragment
import com.bottari.presentation.common.extension.getSSAID
import com.bottari.presentation.databinding.FragmentMyBottariTemplateBinding
import com.bottari.presentation.view.market.MarketNavigator
import com.bottari.presentation.view.market.my.adapter.MyBottariTemplateAdapter
import com.bottari.presentation.view.market.my.listener.MyBottariTemplateEventListener

class MyBottariTemplateFragment :
    BaseFragment<FragmentMyBottariTemplateBinding>(FragmentMyBottariTemplateBinding::inflate),
    MyBottariTemplateEventListener {
    private val viewModel: MyBottariTemplateViewModel by viewModels {
        MyBottariTemplateViewModel.Factory(
            requireContext().getSSAID(),
        )
    }
    private val adapter: MyBottariTemplateAdapter by lazy { MyBottariTemplateAdapter(this) }

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
        (requireActivity() as? MarketNavigator)?.navigateToDetail(bottariTemplateId, true)
    }

    private fun setupObserver() {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            toggleLoadingIndicator(uiState.isLoading)
            adapter.submitList(uiState.bottariTemplates)
        }

        viewModel.uiEvent.observe(viewLifecycleOwner) { uiEvent ->
            when (uiEvent) {
                MyBottariTemplateUiEvent.FetchMyTemplateFailure -> showSnackbar(R.string.market_my_template_fetch_failure_text)
                MyBottariTemplateUiEvent.DeleteMyTemplateFailure -> showSnackbar(R.string.market_my_template_delete_failure_text)
                MyBottariTemplateUiEvent.DeleteMyTemplateSuccess -> showSnackbar(R.string.market_my_template_delete_success_text)
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
