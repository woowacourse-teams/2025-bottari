package com.bottari.presentation.view.edit.personal.main.rename

import BottariRenameViewModel
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.bottari.presentation.base.UiState
import com.bottari.presentation.databinding.DialogBottariRenameBinding
import com.bottari.presentation.extension.getSSAID
import com.google.android.material.snackbar.Snackbar

class BottariRenameDialog :
    DialogFragment(),
    TextWatcher {
    private val viewModel: BottariRenameViewModel by viewModels {
        val bottariId = requireArguments().getLong(EXTRA_BOTTARI_ID)
        val oldTitle = requireArguments().getString(EXTRA_OLD_TITLE).orEmpty()
        BottariRenameViewModel.Factory(bottariId, oldTitle)
    }

    private var _binding: DialogBottariRenameBinding? = null
    val binding: DialogBottariRenameBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DialogBottariRenameBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        setupListener()
    }

    override fun onStart() {
        super.onStart()
        setupDialog()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun afterTextChanged(s: Editable?) {}

    override fun beforeTextChanged(
        s: CharSequence?,
        start: Int,
        count: Int,
        after: Int,
    ) {
    }

    override fun onTextChanged(
        s: CharSequence?,
        start: Int,
        before: Int,
        count: Int,
    ) {
        val isEnabled = s.isNullOrBlank().not()
        val alphaValue = if (isEnabled) ENABLED_ALPHA_VALUE else DISABLED_ALPHA_VALUE
        binding.btnBottariRename.isClickable = isEnabled
        binding.btnBottariRename.alpha = alphaValue
    }

    private fun setupObserver() {
        viewModel.renameSuccess.observe(viewLifecycleOwner, ::handleRenameState)
    }

    private fun setupListener() {
        binding.etBottariRenameName.addTextChangedListener(this)
        binding.btnBottariRenameClose.setOnClickListener { dismiss() }
        binding.btnBottariRename.setOnClickListener {
            val title = binding.etBottariRenameName.text.toString()
            viewModel.renameBottari(ssaid = requireContext().getSSAID(), newTitle = title)
        }
    }

    private fun setupDialog() {
        val metrics = Resources.getSystem().displayMetrics
        val width = (metrics.widthPixels * WIDTH_RATIO).toInt()
        dialog?.run {
            setCancelable(false)
            window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
            window?.setLayout(
                width,
                ViewGroup.LayoutParams.WRAP_CONTENT,
            )
        }
    }

    private fun handleRenameState(uiState: UiState<Unit?>) {
        when (uiState) {
            is UiState.Loading -> Unit
            is UiState.Success -> {
                parentFragmentManager.setFragmentResult(
                    RENAME_RESULT_KEY,
                    Bundle(),
                )
                dismiss()
            }
            is UiState.Failure -> {
                Snackbar
                    .make(binding.root, uiState.message.toString(), Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
    }

    companion object {
        private const val WIDTH_RATIO = 0.9
        private const val DISABLED_ALPHA_VALUE = 0.4f
        private const val ENABLED_ALPHA_VALUE = 1f

        const val RENAME_RESULT_KEY = "RENAME_RESULT_KEY"

        private const val EXTRA_BOTTARI_ID = "EXTRA_BOTTARI_ID"
        private const val EXTRA_OLD_TITLE = "EXTRA_OLD_TITLE"

        fun newInstance(
            bottariId: Long,
            oldTitle: String,
        ): BottariRenameDialog =
            BottariRenameDialog().apply {
                arguments =
                    Bundle().apply {
                        putLong(EXTRA_BOTTARI_ID, bottariId)
                        putString(EXTRA_OLD_TITLE, oldTitle)
                    }
            }
    }
}
