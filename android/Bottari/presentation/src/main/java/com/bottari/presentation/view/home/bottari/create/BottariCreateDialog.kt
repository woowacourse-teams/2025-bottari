package com.bottari.presentation.view.home.bottari.create

import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.bottari.presentation.R
import com.bottari.presentation.databinding.DialogBottariCreateBinding
import com.bottari.presentation.extension.getSSAID
import com.bottari.presentation.view.edit.personal.PersonalBottariEditActivity
import com.google.android.material.snackbar.Snackbar

class BottariCreateDialog :
    DialogFragment(),
    TextWatcher {
    private val viewModel: BottariCreateViewModel by viewModels {
        BottariCreateViewModel.Factory(getString(R.string.default_bottari_title))
    }
    private var _binding: DialogBottariCreateBinding? = null
    val binding: DialogBottariCreateBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DialogBottariCreateBinding.inflate(layoutInflater)
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

    override fun beforeTextChanged(
        s: CharSequence?,
        start: Int,
        count: Int,
        after: Int,
    ) {
    }

    override fun afterTextChanged(s: Editable?) {
        viewModel.updateBottariTitle(s.toString().trim())
    }

    override fun onTextChanged(
        s: CharSequence?,
        start: Int,
        before: Int,
        count: Int,
    ) {
        val isEnabled = s.isNullOrBlank().not()
        val alphaValue = if (isEnabled) ENABLED_ALPHA_VALUE else DISABLED_ALPHA_VALUE
        binding.btnBottariCreate.isClickable = isEnabled
        binding.btnBottariCreate.alpha = alphaValue
    }

    private fun setupObserver() {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            if (binding.etBottariCreateName.text.toString() == uiState.bottariTitle) return@observe
            binding.etBottariCreateName.setText(uiState.bottariTitle)
            binding.etBottariCreateName.setSelection(uiState.bottariTitle.length)
        }

        viewModel.uiEvent.observe(viewLifecycleOwner) { uiEvent ->
            when (uiEvent) {
                is BottariCreateUiEvent.CreateBottariSuccess -> navigateToScreen(uiEvent.bottariId)
                BottariCreateUiEvent.CreateBottariFailure -> showSnackBar(R.string.create_bottari_failure)
            }
        }
    }

    private fun setupListener() {
        binding.etBottariCreateName.addTextChangedListener(this)
        binding.btnBottariCreateClose.setOnClickListener { dismiss() }
        binding.btnBottariCreate.setOnClickListener {
            val title = binding.etBottariCreateName.text.toString()
            viewModel.createBottari(requireContext().getSSAID(), title)
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

    private fun showSnackBar(
        @StringRes message: Int,
    ) {
        Snackbar
            .make(
                requireActivity().findViewById(android.R.id.content),
                message,
                Snackbar.LENGTH_SHORT,
            ).show()
        dismiss()
    }

    private fun navigateToScreen(bottariId: Long?) {
        if (bottariId == null) return
        val intent = PersonalBottariEditActivity.newIntent(requireContext(), bottariId, true)
        startActivity(intent)
        dismiss()
    }

    companion object {
        private const val WIDTH_RATIO = 0.9
        private const val DISABLED_ALPHA_VALUE = 0.4f
        private const val ENABLED_ALPHA_VALUE = 1f
    }
}
