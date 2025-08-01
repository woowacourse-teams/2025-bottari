package com.bottari.presentation.view.edit.personal.item

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseFragment
import com.bottari.presentation.common.extension.dpToPx
import com.bottari.presentation.common.extension.getParcelableArrayListCompat
import com.bottari.presentation.common.extension.getSSAID
import com.bottari.presentation.databinding.FragmentPersonalItemEditBinding
import com.bottari.presentation.model.BottariItemUiModel
import com.bottari.presentation.view.common.alart.CustomAlertDialog
import com.bottari.presentation.view.common.alart.DialogListener
import com.bottari.presentation.view.common.alart.DialogPresetType
import com.bottari.presentation.view.edit.personal.item.adapter.PersonalItemEditAdapter

class PersonalItemEditFragment :
    BaseFragment<FragmentPersonalItemEditBinding>(FragmentPersonalItemEditBinding::inflate),
    TextWatcher {
    private lateinit var onBackPressedCallback: OnBackPressedCallback

    private val viewModel: PersonalItemEditViewModel by viewModels {
        val arguments = requireArguments()
        PersonalItemEditViewModel.Factory(
            ssaid = requireContext().getSSAID(),
            bottariId = arguments.getLong(ARG_EXTRA_BOTTARI_ID),
            title = arguments.getString(ARG_BOTTARI_TITLE) ?: "",
            items = arguments.getParcelableArrayListCompat(ARG_BOTTARI_ITEMS) ?: emptyList(),
        )
    }

    private val adapter by lazy {
        PersonalItemEditAdapter {
            onBackPressedCallback.isEnabled = true
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
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            toggleLoadingIndicator(uiState.isLoading)
            handleBottariNameState(uiState.title)
            handleItemState(uiState.items)
            handleEmptyView(state.items.isEmpty())

        }
        viewModel.uiEvent.observe(viewLifecycleOwner) { event ->
            when (event) {
                PersonalItemEditUiEvent.SaveBottariItemsFailure -> showSnackbar(R.string.common_save_failure_text)
                PersonalItemEditUiEvent.SaveBottariItemsSuccess -> {
                    onBackPressedCallback.isEnabled = false
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        }
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
        onBackPressedCallback =
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
                showExitConfirmationDialog()
            }
        onBackPressedCallback.isEnabled = false
    }

    private fun addItemFromInput() {
        viewModel.addNewItemIfNeeded(binding.etPersonalItem.text.toString())
        binding.etPersonalItem.text.clear()
        onBackPressedCallback.isEnabled = true
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

    private fun handleEmptyView(isEmpty: Boolean) {
        binding.emptyView.clPersonalBottariItemEmptyView.isVisible = isEmpty

    private fun showExitConfirmationDialog() {
        val existingDialog =
            parentFragmentManager.findFragmentByTag(tag) as? CustomAlertDialog

        if (existingDialog?.dialog?.isShowing == true) return

        val dialog =
            CustomAlertDialog
                .newInstance(DialogPresetType.EXIT_WITHOUT_SAVE)
                .setDialogListener(
                    object : DialogListener {
                        override fun onClickPositive() {
                            onBackPressedCallback.isEnabled = false
                            requireActivity().onBackPressedDispatcher.onBackPressed()
                        }

                        override fun onClickNegative() {}
                    },
                )

        dialog.show(parentFragmentManager, tag)
    }

    companion object {
        private const val ARG_EXTRA_BOTTARI_ID = "ARG_EXTRA_BOTTARI_ID"
        private const val ARG_BOTTARI_TITLE = "ARG_BOTTARI_TITLE"
        private const val ARG_BOTTARI_ITEMS = "ARG_BOTTARI_ITEMS"

        private const val DUPLICATE_BORDER_WIDTH_DP = 2
        private const val DISABLED_ALPHA = 0.3f
        private const val ENABLED_ALPHA = 1f

        fun newBundle(
            id: Long,
            title: String,
            items: List<BottariItemUiModel>,
        ) = Bundle().apply {
            putLong(ARG_EXTRA_BOTTARI_ID, id)
            putString(ARG_BOTTARI_TITLE, title)
            putParcelableArrayList(ARG_BOTTARI_ITEMS, ArrayList(items))
        }
    }
}
