package com.bottari.presentation.view.edit.personal.main.rename

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
import com.bottari.logger.LogEventHelper
import com.bottari.presentation.R
import com.bottari.presentation.common.extension.showSnackbar
import com.bottari.presentation.databinding.DialogBottariRenameBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottariRenameDialog :
    DialogFragment(),
    TextWatcher {
    private val viewModel: BottariRenameViewModel by viewModels()

    private var _binding: DialogBottariRenameBinding? = null
    val binding: DialogBottariRenameBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogEventHelper.logScreenEnter(javaClass.simpleName)
    }

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
        binding.etBottariRenameName.setText(viewModel.uiState.value?.title)
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
        viewModel.cacheTitleInput(s.toString())
    }

    private fun setupObserver() {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            if (binding.etBottariRenameName.text.toString() != uiState.title) {
                binding.etBottariRenameName.setText(uiState.title)
            }
        }

        viewModel.uiEvent.observe(viewLifecycleOwner) { uiEvent ->
            when (uiEvent) {
                BottariRenameUiEvent.SaveBottariTitleSuccess -> dismiss()
                BottariRenameUiEvent.SaveBottariTitleFailure -> requireView().showSnackbar(R.string.bottari_rename_failure_text)
            }
        }
    }

    private fun setupListener() {
        binding.etBottariRenameName.addTextChangedListener(this)
        binding.btnBottariRenameClose.setOnClickListener { dismiss() }
        binding.btnBottariRename.setOnClickListener {
            viewModel.saveBottariTitle()
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

    companion object {
        private const val WIDTH_RATIO = 0.9
        private const val DISABLED_ALPHA_VALUE = 0.4f
        private const val ENABLED_ALPHA_VALUE = 1f

        fun newInstance(
            bottariId: Long,
            initialTitle: String,
        ): BottariRenameDialog =
            BottariRenameDialog().apply {
                arguments =
                    Bundle().apply {
                        putLong(BottariRenameViewModel.KEY_BOTTARI_ID, bottariId)
                        putString(BottariRenameViewModel.KEY_INITIAL_TITLE, initialTitle)
                    }
            }
    }
}
