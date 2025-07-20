package com.bottari.presentation.view.edit.personal.item

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bottari.presentation.base.BaseFragment
import com.bottari.presentation.base.UiState
import com.bottari.presentation.databinding.FragmentPersonalItemEditBinding
import com.bottari.presentation.model.ItemUiModel
import com.bottari.presentation.view.edit.personal.item.adapter.PersonalItemEditAdapter

class PersonalItemEditFragment :
    BaseFragment<FragmentPersonalItemEditBinding>(FragmentPersonalItemEditBinding::inflate) {

    private val viewModel: PersonalItemEditViewModel by viewModels {
        PersonalItemEditViewModel.Factory(
            getBottariId()
        )
    }
    private val adapter by lazy {
        PersonalItemEditAdapter {
            viewModel.deleteItem(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObserver()
        setupUI()
        setupListener()
    }

    private fun setupObserver() {
        viewModel.items.observe(viewLifecycleOwner, ::handleItemState)
    }

    private fun setupUI() {
        binding.rvPersonalItemEdit.adapter = adapter
        binding.rvPersonalItemEdit.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupListener() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
            val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            val bottomPadding = if (imeVisible) ime.bottom else systemBars.bottom
            view.setPadding(0, 0, 0, bottomPadding)

            insets
        }

        binding.btnPrevious.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.btnPersonalItemAdd.setOnClickListener {
            viewModel.addItem(binding.etPersonalItem.text.toString())
            binding.etPersonalItem.text.clear()
        }

        binding.etPersonalItem.setOnEditorActionListener { view, actionId, _ ->
            if (actionId != EditorInfo.IME_ACTION_NONE) return@setOnEditorActionListener false

            viewModel.addItem(view.text.toString())
            binding.etPersonalItem.text.clear()
            true
        }
    }

    private fun getBottariId(): Long = arguments?.getLong(EXTRA_BOTTARI_ID) ?: INVALID_BOTTARI_ID

    private fun handleItemState(uiState: UiState<List<ItemUiModel>>) {
        when (uiState) {
            is UiState.Loading -> {}
            is UiState.Success -> adapter.submitList(uiState.data)
            is UiState.Failure -> {}
        }
    }

    companion object {
        private const val INVALID_BOTTARI_ID = -1L
        private const val EXTRA_BOTTARI_ID = "EXTRA_BOTTARI_ID"

        fun newBundle(bottariId: Long) = Bundle().apply {
            putLong(EXTRA_BOTTARI_ID, bottariId)
        }
    }
}
