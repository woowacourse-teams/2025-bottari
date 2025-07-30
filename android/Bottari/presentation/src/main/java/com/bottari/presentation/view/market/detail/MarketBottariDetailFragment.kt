package com.bottari.presentation.view.market.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.bottari.presentation.base.BaseFragment
import com.bottari.presentation.base.UiState
import com.bottari.presentation.databinding.FragmentMarketBottariDetailBinding
import com.bottari.presentation.extension.getSSAID
import com.bottari.presentation.model.BottariTemplateUiModel
import com.bottari.presentation.view.edit.personal.PersonalBottariEditActivity
import com.bottari.presentation.view.market.detail.adapter.MarketBottariDetailAdapter

class MarketBottariDetailFragment : BaseFragment<FragmentMarketBottariDetailBinding>(FragmentMarketBottariDetailBinding::inflate) {
    private val viewModel: MarketBottariDetailViewModel by viewModels {
        MarketBottariDetailViewModel.Factory(
            ssaid = requireContext().getSSAID(),
            bottarID = getBottariId(),
        )
    }
    private val adapter by lazy { MarketBottariDetailAdapter() }

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
        viewModel.bottariTemplate.observe(viewLifecycleOwner, ::handleBottariTemplateState)
        viewModel.createSuccess.observe(viewLifecycleOwner, ::handleTakeSuccess)
    }

    private fun setupUI() {
        binding.rvMarketBottariDetail.adapter = adapter
        if (getIsMyTemplate()) binding.btnTakeTemplate.visibility = View.GONE
    }

    private fun setupListener() {
        binding.btnPrevious.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.btnTakeTemplate.setOnClickListener {
            takeBottariTemplate()
        }
    }

    private fun getBottariId(): Long = requireArguments().getLong(ARG_BOTTARI_ID, INVALID_BOTTARI_ID)

    private fun getIsMyTemplate(): Boolean = requireArguments().getBoolean(ARG_IS_MY_TEMPLATE, false)

    private fun takeBottariTemplate() {
        viewModel.takeBottariTemplate()
    }

    private fun navigateBottariEdit(bottariId: Long?) {
        if (bottariId == null) return
        startActivity(PersonalBottariEditActivity.newIntent(requireContext(), bottariId, true))
        requireActivity().finish()
    }

    private fun handleBottariTemplateState(uiState: UiState<BottariTemplateUiModel>) {
        when (uiState) {
            is UiState.Loading -> Unit
            is UiState.Success -> {
                binding.tvBottariTitle.text = uiState.data.title
                adapter.submitList(uiState.data.items)
            }

            is UiState.Failure -> Unit
        }
    }

    private fun handleTakeSuccess(uiState: UiState<Long?>) {
        when (uiState) {
            is UiState.Loading -> Unit
            is UiState.Success -> navigateBottariEdit(uiState.data)
            is UiState.Failure -> Unit
        }
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
