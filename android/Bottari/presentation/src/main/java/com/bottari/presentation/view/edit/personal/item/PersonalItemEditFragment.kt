package com.bottari.presentation.view.edit.personal.item

import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bottari.presentation.R
import com.bottari.presentation.base.BaseFragment
import com.bottari.presentation.base.UiState
import com.bottari.presentation.databinding.FragmentPersonalItemEditBinding
import com.bottari.presentation.extension.dpToPx
import com.bottari.presentation.model.ItemUiModel
import com.bottari.presentation.view.edit.personal.item.adapter.PersonalItemEditAdapter

class PersonalItemEditFragment :
    BaseFragment<FragmentPersonalItemEditBinding>(FragmentPersonalItemEditBinding::inflate),
    TextWatcher {
    private val viewModel: PersonalItemEditViewModel by viewModels {
        PersonalItemEditViewModel.Factory(getBottariId())
    }

    private val adapter by lazy {
        PersonalItemEditAdapter {
            viewModel.deleteItem(it)
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
        viewModel.items.observe(viewLifecycleOwner, ::handleItemState)
    }

    private fun setupUI() {
        setupInsets()

        binding.rvPersonalItemEdit.adapter = adapter
        binding.rvPersonalItemEdit.layoutManager = LinearLayoutManager(requireContext())
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

    private fun getBottariId(): Long = arguments?.getLong(EXTRA_BOTTARI_ID) ?: INVALID_BOTTARI_ID

    private fun setupInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            val bottomInset = calculateBottomInset(insets, imeVisible)
            view.setPadding(0, 0, 0, bottomInset)
            insets
        }
    }

    private fun calculateBottomInset(
        insets: WindowInsetsCompat,
        imeVisible: Boolean,
    ): Int =
        when {
            imeVisible -> insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
            Build.VERSION.SDK_INT < Build.VERSION_CODES.R ->
                insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom

            else -> DEFAULT_BOTTOM_INSET
        }

    private fun addItemFromInput() {
        viewModel.addItem(binding.etPersonalItem.text.toString())
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

    private fun handleBottariNameState(uiState: UiState<String>) {
        when (uiState) {
            is UiState.Loading -> Unit
            is UiState.Success -> binding.tvBottariTitle.text = uiState.data
            is UiState.Failure -> Unit
        }
    }

    private fun handleItemState(uiState: UiState<List<ItemUiModel>>) {
        when (uiState) {
            is UiState.Loading -> Unit
            is UiState.Success -> adapter.submitList(uiState.data)
            is UiState.Failure -> Unit
        }
    }

    companion object {
        private const val INVALID_BOTTARI_ID = -1L
        private const val EXTRA_BOTTARI_ID = "EXTRA_BOTTARI_ID"

        private const val DUPLICATE_BORDER_WIDTH_DP = 2
        private const val DISABLED_ALPHA = 0.3f
        private const val ENABLED_ALPHA = 1f
        private const val DEFAULT_BOTTOM_INSET = 0

        fun newBundle(bottariId: Long) =
            Bundle().apply {
                putLong(EXTRA_BOTTARI_ID, bottariId)
            }
    }
}
