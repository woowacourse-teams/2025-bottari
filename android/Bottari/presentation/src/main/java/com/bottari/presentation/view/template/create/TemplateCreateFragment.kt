package com.bottari.presentation.view.template.create

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.view.isEmpty
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseFragment
import com.bottari.presentation.common.extension.getSSAID
import com.bottari.presentation.databinding.FragmentTemplateCreateBinding
import com.bottari.presentation.view.common.decoration.SideSpaceItemDecoration
import com.bottari.presentation.view.template.create.adapter.TemplateCreateMyBottariAdapter
import com.bottari.presentation.view.template.create.adapter.TemplateCreateMyBottariItemAdapter

class TemplateCreateFragment : BaseFragment<FragmentTemplateCreateBinding>(FragmentTemplateCreateBinding::inflate) {
    private val viewModel: TemplateCreateViewModel by viewModels {
        TemplateCreateViewModel.Factory(requireContext().getSSAID())
    }
    private val snapHelper by lazy { LinearSnapHelper() }
    private val itemAdapter by lazy { TemplateCreateMyBottariItemAdapter() }
    private val bottariAdapter by lazy {
        TemplateCreateMyBottariAdapter(::handleBottariClick)
    }
    private val onScrollIdleListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(
                recyclerView: RecyclerView,
                newState: Int,
            ) = handleScrollStateChanged(newState)
        }
    private val onGlobalLayoutListener =
        object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() = handleGlobalLayout(this)
        }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        setupUI()
        setupListeners()
    }

    private fun setupObserver() {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            toggleLoadingIndicator(uiState.isLoading)
            handleEmptyView(uiState.shouldShowEmptyView)
            handleCreateButtonState(uiState.canCreateTemplate)
            bottariAdapter.submitList(uiState.bottaries)
            itemAdapter.submitList(uiState.currentBottariItems)
        }
        viewModel.uiEvent.observe(viewLifecycleOwner) { uiEvent ->
            when (uiEvent) {
                TemplateCreateUiEvent.FetchMyBottariesFailure -> showSnackbar(R.string.template_fetch_bottari_details_failure_text)
                TemplateCreateUiEvent.CreateTemplateFailure -> showSnackbar(R.string.template_create_failure_text)
                TemplateCreateUiEvent.CreateTemplateSuccuss ->
                    showSnackbar(R.string.template_create_success_text) {
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                    }
            }
        }
    }

    private fun setupUI() {
        binding.rvTemplateCreateMyBottariItem.adapter = itemAdapter
        binding.rvTemplateCreateMyBottariItem.itemAnimator = null
        binding.rvTemplateCreateMyBottari.apply {
            adapter = bottariAdapter
            itemAnimator = null
            snapHelper.attachToRecyclerView(this)
        }
    }

    private fun setupListeners() {
        binding.btnTemplateCreate.setOnClickListener { viewModel.createTemplate() }
        binding.btnPrevious.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
        binding.rvTemplateCreateMyBottari.addOnScrollListener(onScrollIdleListener)
        binding.rvTemplateCreateMyBottari.viewTreeObserver.addOnGlobalLayoutListener(
            onGlobalLayoutListener,
        )
    }

    private fun handleBottariClick(pos: Long) {
        binding.rvTemplateCreateMyBottari.smoothScrollToPosition(pos.toInt())
    }

    private fun handleGlobalLayout(listener: ViewTreeObserver.OnGlobalLayoutListener) {
        val recyclerView = binding.rvTemplateCreateMyBottari
        if (recyclerView.isEmpty()) return

        recyclerView.addItemDecoration(SideSpaceItemDecoration(ITEM_SIDE_SPACE_RATIO))
        recyclerView.viewTreeObserver.removeOnGlobalLayoutListener(listener)
    }

    private fun handleScrollStateChanged(newState: Int) {
        if (newState != RecyclerView.SCROLL_STATE_IDLE) return

        val position = findCenterItemPosition() ?: return
        val bottariId = bottariAdapter.currentList[position].id
        viewModel.changeBottari(bottariId)
    }

    private fun findCenterItemPosition(): Int? {
        val layoutManager = binding.rvTemplateCreateMyBottari.layoutManager ?: return null
        val centerView = snapHelper.findSnapView(layoutManager) ?: return null
        return binding.rvTemplateCreateMyBottari
            .getChildAdapterPosition(centerView)
            .takeIf { it in bottariAdapter.currentList.indices }
    }

    private fun handleEmptyView(shouldShowEmptyView: Boolean) {
        binding.emptyView.root.visibility = if (shouldShowEmptyView) View.VISIBLE else View.GONE
    }

    private fun handleCreateButtonState(canCreateTemplate: Boolean) {
        binding.btnTemplateCreate.isEnabled = canCreateTemplate
        val alpha =
            if (canCreateTemplate) CAN_CREATE_TEMPLATE_ALPHA_VALUE else CANNOT_CREATE_TEMPLATE_ALPHA_VALUE
        binding.btnTemplateCreate.alpha = alpha
    }

    companion object {
        private const val ITEM_SIDE_SPACE_RATIO = 0.3f
        private const val CAN_CREATE_TEMPLATE_ALPHA_VALUE = 1f
        private const val CANNOT_CREATE_TEMPLATE_ALPHA_VALUE = 0.5f
    }
}
