package com.bottari.presentation.view.edit.team.item.main

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat.getColor
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseFragment
import com.bottari.presentation.common.extension.applyImeBottomPadding
import com.bottari.presentation.common.extension.dpToPx
import com.bottari.presentation.common.extension.getParcelableCompat
import com.bottari.presentation.common.extension.setTextIfDifferent
import com.bottari.presentation.common.extension.showKeyboard
import com.bottari.presentation.databinding.FragmentTeamBottariItemEditBinding
import com.bottari.presentation.model.BottariItemTypeUiModel
import com.bottari.presentation.view.edit.team.TeamBottariEditNavigator
import com.bottari.presentation.view.edit.team.item.main.adapter.TeamItemEditFragmentAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class TeamItemEditFragment :
    BaseFragment<FragmentTeamBottariItemEditBinding>(
        FragmentTeamBottariItemEditBinding::inflate,
    ) {
    private val viewModel: TeamItemEditViewModel by viewModels {
        TeamItemEditViewModel.Factory(
            requireArguments().getParcelableCompat(ARG_KEY_TAB_TYPE),
        )
    }
    private val adapter: TeamItemEditFragmentAdapter by lazy {
        TeamItemEditFragmentAdapter(this, requireArguments().getLong(ARG_KEY_BOTTARI_ID))
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

    private fun setupObserver() {
        viewModel.uiState.observe(viewLifecycleOwner, ::handleUiState)
    }

    private fun setupUI() {
        binding.root.applyImeBottomPadding()
        setupTabLayout()
    }

    private fun setupListener() {
        binding.viewItemInput.etItemInput.doAfterTextChanged { newText ->
            viewModel.updateInput(newText.toString())
        }
        binding.viewItemInput.btnPersonalItemSend.setOnClickListener { viewModel.createItem() }
        binding.btnPrevious.setOnClickListener {
            (requireActivity() as? TeamBottariEditNavigator)?.navigateBack()
        }
        binding.vpTeamBottariItemEdit.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    viewModel.updateTabType(TeamItemEditFragmentAdapter.typeFromPosition(position))
                }
            },
        )
        binding.viewItemInput.etItemInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId != EditorInfo.IME_ACTION_SEND) return@setOnEditorActionListener false
            viewModel.createItem()
            true
        }
    }

    private fun handleUiState(uiState: TeamItemEditUiState) {
        handleEditTextState(uiState)
        binding.viewItemInput.etItemInput.setTextIfDifferent(uiState.itemInputText)
        handleSendButtonState(uiState.canSend)
    }

    private fun setupTabLayout() {
        binding.vpTeamBottariItemEdit.adapter = adapter
        binding.vpTeamBottariItemEdit.offscreenPageLimit = adapter.itemCount
        binding.vpTeamBottariItemEdit.isUserInputEnabled = false
        TabLayoutMediator(
            binding.tlTeamBottariItemEdit,
            binding.vpTeamBottariItemEdit,
            ::setupTabs,
        ).attach()
        selectInitialTab()
    }

    private fun setupTabs(
        tab: TabLayout.Tab,
        position: Int,
    ) {
        tab.text = TeamItemEditFragmentAdapter.tabTitleFromPosition(position, requireContext())
    }

    private fun selectInitialTab() {
        val type = requireArguments().getParcelableCompat<BottariItemTypeUiModel>(ARG_KEY_TAB_TYPE)
        binding.vpTeamBottariItemEdit
            .setCurrentItem(TeamItemEditFragmentAdapter.positionFromType(type), false)
    }

    private fun handleEditTextState(uiState: TeamItemEditUiState) {
        val background =
            binding.viewItemInput.etItemInput.background
                .mutate()
        if (background is GradientDrawable) {
            val colorRes = if (uiState.isAlreadyExist) R.color.red else R.color.transparent
            val strokeColor = getColor(requireContext(), colorRes)
            background.setStroke(requireContext().dpToPx(DUPLICATE_BORDER_WIDTH_DP), strokeColor)
        }

        binding.viewItemInput.etItemInput.setTextIfDifferent(uiState.itemInputText)
        if (uiState.itemInputText.isNotBlank()) {
            binding.viewItemInput.etItemInput.setSelection(uiState.itemInputText.length)
            showKeyboard(binding.viewItemInput.etItemInput)
            return
        }
        binding.viewItemInput.etItemInput.clearFocus()
    }

    private fun handleSendButtonState(canSend: Boolean) {
        binding.viewItemInput.btnPersonalItemSend.run {
            isEnabled = canSend
            alpha = if (canSend) SEND_BUTTON_ENABLED_ALPHA else SEND_BUTTON_DISABLED_ALPHA
        }
    }

    companion object {
        private const val ARG_KEY_BOTTARI_ID = "ARG_KEY_BOTTARI_ID"
        private const val ARG_KEY_TAB_TYPE = "ARG_KEY_TAB_TYPE"
        private const val DUPLICATE_BORDER_WIDTH_DP = 2
        private const val SEND_BUTTON_ENABLED_ALPHA = 1f
        private const val SEND_BUTTON_DISABLED_ALPHA = 0.5f

        fun newInstance(
            bottariId: Long,
            requireTabType: BottariItemTypeUiModel,
        ): TeamItemEditFragment =
            TeamItemEditFragment().apply {
                arguments =
                    bundleOf(
                        ARG_KEY_BOTTARI_ID to bottariId,
                        ARG_KEY_TAB_TYPE to requireTabType,
                    )
            }
    }
}
