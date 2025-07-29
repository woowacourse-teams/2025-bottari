package com.bottari.presentation.view.market.my

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.bottari.presentation.R
import com.bottari.presentation.base.BaseFragment
import com.bottari.presentation.base.UiState
import com.bottari.presentation.databinding.FragmentMyBottariTemplateBinding
import com.bottari.presentation.extension.getSSAID
import com.bottari.presentation.model.BottariTemplateUiModel
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
        viewModel.myBottariTemplates.observe(viewLifecycleOwner, ::handleMyBottariTemplateState)
        viewModel.uiEvent.observe(viewLifecycleOwner, ::showSnackBar)
    }

    private fun setupUI() {
        binding.rvMyBottariTemplate.adapter = adapter
    }

    private fun setupListener() {
        binding.btnPrevious.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun handleMyBottariTemplateState(uiState: UiState<List<BottariTemplateUiModel>>) {
        when (uiState) {
            is UiState.Loading -> Unit
            is UiState.Success -> adapter.submitList(uiState.data)
            is UiState.Failure -> Unit
        }
    }

    private fun showSnackBar(event: MyBottariTemplateUiEvent) {
        val stringRes =
            when (event) {
                MyBottariTemplateUiEvent.DELETE_MY_TEMPLATE_SUCCESS -> R.string.my_bottari_template_screen_delete_success
                MyBottariTemplateUiEvent.DELETE_MY_TEMPLATE_FAILURE -> R.string.my_bottari_template_screen_delete_failure
            }
        showSnackbar(stringRes)
    }

    companion object {
        private const val ARG_BOTTARI_TEMPLATE_ID = "ARG_BOTTARI_TEMPLATE_ID"

        fun newBundle(bottariTemplateId: Long): Bundle =
            Bundle().apply {
                putLong(ARG_BOTTARI_TEMPLATE_ID, bottariTemplateId)
            }
    }
}
