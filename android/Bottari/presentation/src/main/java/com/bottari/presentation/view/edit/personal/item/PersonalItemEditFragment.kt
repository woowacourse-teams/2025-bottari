package com.bottari.presentation.view.edit.personal.item

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseFragment
import com.bottari.presentation.common.extension.applyImeBottomPadding
import com.bottari.presentation.common.extension.dpToPx
import com.bottari.presentation.common.extension.showSnackbar
import com.bottari.presentation.databinding.FragmentPersonalItemEditBinding
import com.bottari.presentation.model.bottari.PersonalChecklistItemUiModel
import com.bottari.presentation.view.edit.personal.item.adapter.PersonalItemEditAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonalItemEditFragment :
    BaseFragment<FragmentPersonalItemEditBinding>(FragmentPersonalItemEditBinding::inflate),
    TextWatcher {
    private val viewModel: PersonalItemEditViewModel by viewModels()

    private val adapter by lazy {
        PersonalItemEditAdapter(viewModel::deleteItem)
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

    override fun beforeTextChanged(
        p0: CharSequence?,
        p1: Int,
        p2: Int,
        p3: Int,
    ) {
    }

    override fun afterTextChanged(p0: Editable?) {}

    override fun onTextChanged(
        p0: CharSequence?,
        p1: Int,
        p2: Int,
        p3: Int,
    ) {
        val inputText = p0?.toString()?.trim().orEmpty()
        updateDuplicateStateUI(inputText)
    }

    private fun setupObserver() {
        collectWithLifecycle(viewModel.uiState) { uiState ->
            toggleLoadingIndicator(uiState.isLoading)
            handleBottariNameState(uiState.title)
            handleItemState(uiState.items)
            handleEmptyView(uiState.isEmpty)
        }
        collectWithLifecycle(viewModel.uiEvent) { event ->
            when (event) {
                PersonalItemEditUiEvent.SaveBottariItemFailure ->
                    requireView().showSnackbar(R.string.common_save_failure_text)

                PersonalItemEditUiEvent.FetchBottariItemsFailure ->
                    requireView().showSnackbar(R.string.bottari_personal_item_fetch_failure_text)

                PersonalItemEditUiEvent.DeleteItemFailure ->
                    requireView().showSnackbar(R.string.bottari_personal_item_delete_failure_text)
            }
        }
    }

    private fun setupUI() {
        binding.rvPersonalItemEdit.adapter = adapter
        binding.rvPersonalItemEdit.layoutManager = LinearLayoutManager(requireContext())
        binding.root.applyImeBottomPadding()
    }

    private fun setupListener() {
        binding.btnPrevious.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
        binding.btnPersonalItemAdd.setOnClickListener { addItemFromInput() }
        binding.etPersonalItem.addTextChangedListener(this)
        binding.etPersonalItem.setOnEditorActionListener { _, actionId, _ ->
            if (actionId != EditorInfo.IME_ACTION_SEND) return@setOnEditorActionListener false
            addItemFromInput()
            true
        }
    }

    private fun addItemFromInput() {
        viewModel.saveItem(binding.etPersonalItem.text.toString())
        binding.etPersonalItem.text.clear()
    }

    private fun updateDuplicateStateUI(text: String) {
        val isDuplicate = adapter.currentList.any { it.name == text }
        updateDuplicateStateItemAddButton(isDuplicate)
        updateDuplicateStateEtPersonalItem(isDuplicate)
    }

    private fun updateDuplicateStateItemAddButton(isDuplicate: Boolean) {
        binding.btnPersonalItemAdd.run {
            isEnabled = !isDuplicate
            alpha = if (isDuplicate) DISABLED_ALPHA else ENABLED_ALPHA
        }
    }

    private fun updateDuplicateStateEtPersonalItem(isDuplicate: Boolean) {
        val background = binding.etPersonalItem.background.mutate()
        if (background is GradientDrawable) {
            val colorRes = if (isDuplicate) R.color.red else R.color.transparent
            val strokeColor = ContextCompat.getColor(requireContext(), colorRes)
            background.setStroke(requireContext().dpToPx(DUPLICATE_BORDER_WIDTH_DP), strokeColor)
        }
    }

    private fun handleBottariNameState(title: String) {
        binding.tvBottariTitle.text = title
    }

    private fun handleItemState(items: List<PersonalChecklistItemUiModel>) {
        adapter.submitList(items)
    }

    private fun handleEmptyView(isEmpty: Boolean) {
        binding.emptyView.clPersonalBottariItemEmptyView.isVisible = isEmpty
    }

    companion object {
        private const val DUPLICATE_BORDER_WIDTH_DP = 2
        private const val DISABLED_ALPHA = 0.3f
        private const val ENABLED_ALPHA = 1f

        fun newBundle(
            id: Long,
            title: String,
        ) = Bundle().apply {
            putLong(PersonalItemEditViewModel.KEY_BOTTARI_ID, id)
            putString(PersonalItemEditViewModel.KEY_BOTTARI_TITLE, title)
        }
    }
}
