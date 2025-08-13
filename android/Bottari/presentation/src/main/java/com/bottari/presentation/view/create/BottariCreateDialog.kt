package com.bottari.presentation.view.create

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
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.bottari.domain.model.bottari.BottariType
import com.bottari.logger.LogEventHelper
import com.bottari.presentation.R
import com.bottari.presentation.common.extension.showSnackbar
import com.bottari.presentation.databinding.DialogBottariCreateBinding
import com.bottari.presentation.view.edit.personal.PersonalBottariEditActivity
import com.bottari.presentation.view.edit.team.TeamBottariEditActivity

class BottariCreateDialog :
    DialogFragment(),
    TextWatcher {
    private val viewModel: BottariCreateViewModel by viewModels {
        BottariCreateViewModel.Factory(
            type = requireArguments().getString(ARG_BOTTARI_TYPE) ?: error(ERROR_BOTTARI_TYPE),
            defaultTitle = getString(R.string.bottari_create_default_title_text),
        )
    }
    private var _binding: DialogBottariCreateBinding? = null
    val binding: DialogBottariCreateBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogEventHelper.logScreenEnter(javaClass.simpleName)
    }

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
        viewModel.updateBottariTitle(s.toString())
    }

    override fun onTextChanged(
        s: CharSequence?,
        start: Int,
        before: Int,
        count: Int,
    ) {
    }

    private fun setupObserver() {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            handleCreateButtonState(uiState.isCanCreate)
            if (binding.etBottariCreateName.text.toString() == uiState.bottariTitle) return@observe
            binding.etBottariCreateName.setText(uiState.bottariTitle)
            binding.etBottariCreateName.setSelection(uiState.bottariTitle.length)
        }

        viewModel.uiEvent.observe(viewLifecycleOwner) { uiEvent ->
            when (uiEvent) {
                is BottariCreateUiEvent.CreatePersonalBottariSuccess -> navigateToPersonalEdit(uiEvent.bottariId)
                is BottariCreateUiEvent.CreateTeamBottariSuccess -> navigateToTeamBottariEdit(uiEvent.bottariId)
                BottariCreateUiEvent.CreateBottariFailure -> showSnackbar(R.string.bottari_create_failure_text)
            }
        }
    }

    private fun handleCreateButtonState(isCanCreate: Boolean) {
        binding.btnBottariCreate.isEnabled = isCanCreate
        binding.btnBottariCreate.alpha =
            if (isCanCreate) ENABLED_ALPHA_VALUE else DISABLED_ALPHA_VALUE
    }

    private fun setupListener() {
        binding.etBottariCreateName.addTextChangedListener(this)
        binding.btnBottariCreateClose.setOnClickListener { dismiss() }
        binding.btnBottariCreate.setOnClickListener {
            viewModel.createBottari()
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

    private fun navigateToPersonalEdit(bottariId: Long?) {
        if (bottariId != null) {
            val intent = PersonalBottariEditActivity.newIntent(requireContext(), bottariId, true)
            startActivity(intent)
        }
        dismiss()
    }

    private fun navigateToTeamBottariEdit(bottariId: Long?) {
        if (bottariId != null) {
            val intent = TeamBottariEditActivity.newIntent(requireContext(), bottariId, true)
            startActivity(intent)
        }
        dismiss()
    }

    private fun showSnackbar(
        @StringRes messageRes: Int,
    ) {
        requireActivity()
            .findViewById<View>(android.R.id.content)
            .showSnackbar(messageRes) {
                dismiss()
            }
    }

    companion object {
        private const val WIDTH_RATIO = 0.9
        private const val DISABLED_ALPHA_VALUE = 0.4f
        private const val ENABLED_ALPHA_VALUE = 1f

        private const val ARG_BOTTARI_TYPE = "ARG_BOTTARI_TYPE"
        private const val ERROR_BOTTARI_TYPE = "[ERROR] 보따리 타입을 찾을 수 없습니다"

        fun newInstance(type: BottariType): BottariCreateDialog =
            BottariCreateDialog().apply {
                arguments = bundleOf(ARG_BOTTARI_TYPE to type.name)
            }
    }
}
