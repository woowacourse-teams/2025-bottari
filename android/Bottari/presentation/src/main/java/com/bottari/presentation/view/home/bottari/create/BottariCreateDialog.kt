package com.bottari.presentation.view.home.bottari.create

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.bottari.presentation.databinding.DialogBottariCreateBinding
import com.bottari.presentation.view.edit.PersonalBottariEditActivity

class BottariCreateDialog :
    DialogFragment(),
    TextWatcher {
    private val viewModel: BottariCreateViewModel by viewModels()
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
        setupListener()
    }

    override fun onStart() {
        super.onStart()
        dialog?.run {
            setCancelable(false)
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
            )
        }
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
        binding.btnBottariCreate.isClickable = isEnabled
        binding.btnBottariCreate.alpha = alphaValue
    }

    private fun setupListener() {
        binding.etBottariCreateName.addTextChangedListener(this)
        binding.btnBottariCreateClose.setOnClickListener { dismiss() }
        binding.btnBottariCreate.setOnClickListener {
            viewModel.createBottari(binding.etBottariCreateName.text.toString())
            navigateToEdit()
        }
    }

    private fun navigateToEdit(){
        val intent = Intent(requireContext(), PersonalBottariEditActivity::class.java)
        intent.putExtra(BOTTARI_ID, viewModel.bottariId)
        startActivity(intent)

    }

    companion object {
        private const val BOTTARI_ID = "BOTTARI_ID"
        private const val DISABLED_ALPHA_VALUE = 0.4f
        private const val ENABLED_ALPHA_VALUE = 1f
    }
}
