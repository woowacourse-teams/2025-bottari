package com.bottari.presentation.view.edit.personal.item

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bottari.presentation.R
import com.bottari.presentation.base.BaseFragment
import com.bottari.presentation.base.UiState
import com.bottari.presentation.databinding.FragmentPersonalItemEditBinding
import com.bottari.presentation.extension.dpToPx
import com.bottari.presentation.extension.getParcelableCompat
import com.bottari.presentation.extension.getSSAID
import com.bottari.presentation.model.BottariDetailUiModel
import com.bottari.presentation.model.BottariItemUiModel
import com.bottari.presentation.view.edit.personal.item.adapter.PersonalItemEditAdapter

class PersonalItemEditFragment :
    BaseFragment<FragmentPersonalItemEditBinding>(FragmentPersonalItemEditBinding::inflate),
    TextWatcher {
    private val viewModel: PersonalItemEditViewModel by viewModels {
        val bottariDetail =
            arguments.getParcelableCompat<BottariDetailUiModel>(EXTRA_BOTTARI_DETAIL)
        PersonalItemEditViewModel.Factory(requireContext().getSSAID(), bottariDetail)
    }

    private val adapter by lazy {
        PersonalItemEditAdapter {
            viewModel.markItemAsDeleted(it)
        }
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
        viewModel.bottariName.observe(viewLifecycleOwner, ::handleBottariNameState)
        viewModel.bottariItems.observe(viewLifecycleOwner, ::handleItemState)
        viewModel.saveState.observe(viewLifecycleOwner, ::handleSaveState)
    }

    private fun setupUI() {
        binding.rvPersonalItemEdit.adapter = adapter
        binding.rvPersonalItemEdit.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupListener() {
        binding.btnPrevious.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }

        binding.btnPersonalItemAdd.setOnClickListener { addItemFromInput() }

        binding.etPersonalItem.addTextChangedListener(this)

        binding.btnConfirm.setOnClickListener { viewModel.saveChanges() }

        binding.etPersonalItem.setOnEditorActionListener { _, actionId, _ ->
            if (actionId != EditorInfo.IME_ACTION_SEND) return@setOnEditorActionListener false

            addItemFromInput()
            true
        }
    }

    private fun addItemFromInput() {
        viewModel.addNewItemIfNeeded(binding.etPersonalItem.text.toString())
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

    private fun handleItemState(bottariItems: List<BottariItemUiModel>) {
        adapter.submitList(bottariItems)
    }

    private fun handleSaveState(uiState: UiState<Unit>) {
        when (uiState) {
            is UiState.Loading -> Unit
            is UiState.Success -> requireActivity().onBackPressedDispatcher.onBackPressed()
            is UiState.Failure -> showSnackbar(R.string.checklist_item_title_prefix)
        }
    }

    companion object {
        private const val EXTRA_BOTTARI_DETAIL = "EXTRA_BOTTARI_DETAIL"

        private const val DUPLICATE_BORDER_WIDTH_DP = 2
        private const val DISABLED_ALPHA = 0.3f
        private const val ENABLED_ALPHA = 1f

        fun newBundle(bottariDetail: BottariDetailUiModel) =
            Bundle().apply {
                putParcelable(EXTRA_BOTTARI_DETAIL, bottariDetail)
            }
    }
}
