package com.bottari.presentation.view.home.profile

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import com.bottari.presentation.R
import com.bottari.presentation.base.BaseFragment
import com.bottari.presentation.base.UiState
import com.bottari.presentation.databinding.FragmentProfileBinding
import com.bottari.presentation.extension.getSSAID
import com.google.android.material.snackbar.Snackbar

class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {
    private val viewModel: ProfileViewModel by viewModels { ProfileViewModel.Factory(requireContext().getSSAID()) }
    private val inputManager: InputMethodManager by lazy {
        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        setupListener()
    }

    private fun setupObserver() {
        viewModel.nickname.observe(viewLifecycleOwner, ::handleNicknameState)
        viewModel.nicknameEvent.observeEvent(viewLifecycleOwner, ::handleNicknameEvent)
    }

    private fun setupListener() {
        setupNicknameEditListener()
        setupNicknameEditButtonClickListener()
    }

    private fun handleNicknameState(uiState: UiState<String>) {
        when (uiState) {
            is UiState.Loading -> Unit
            is UiState.Success -> {
                binding.etNicknameEdit.text =
                    Editable.Factory.getInstance().newEditable(uiState.data)
            }

            is UiState.Failure -> {
                binding.etNicknameEdit.text =
                    Editable.Factory.getInstance().newEditable(uiState.message)
            }
        }
    }

    private fun handleNicknameEvent(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
    }

    private fun setupNicknameEditListener() {
        binding.etNicknameEdit.setOnEditorActionListener { _, actionId, _ ->
            handleEditorAction(actionId)
        }
        binding.etNicknameEdit.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                confirmNicknameEdit()
            }
        }
    }

    private fun setupNicknameEditButtonClickListener() {
        binding.btnNicknameEdit.setOnClickListener {
            if (binding.etNicknameEdit.isFocused) {
                setEditMode(false)
                binding.btnNicknameEdit.setImageResource(R.drawable.btn_edit)
                return@setOnClickListener
            }
            setEditMode(true)
            binding.btnNicknameEdit.setImageResource(R.drawable.btn_confirm)
        }
    }

    private fun handleEditorAction(actionId: Int): Boolean {
        if (actionId == EditorInfo.IME_ACTION_DONE ||
            actionId == EditorInfo.IME_ACTION_NEXT ||
            actionId == EditorInfo.IME_ACTION_UNSPECIFIED
        ) {
            setEditMode(false)
            return true
        }
        return false
    }

    private fun confirmNicknameEdit() {
        val newNickname = binding.etNicknameEdit.text.toString()
        binding.btnNicknameEdit.setImageResource(R.drawable.btn_edit)
        viewModel.saveNickname(newNickname)
    }

    private fun setEditMode(enabled: Boolean) {
        binding.etNicknameEdit.apply {
            isFocusable = enabled
            isFocusableInTouchMode = enabled
            isCursorVisible = enabled
            isClickable = enabled
            isLongClickable = enabled
            isEnabled = true
            if (enabled) {
                requestFocus()
                setSelection(text.length)
                showKeyboard(this)
                return
            }
            clearFocus()
            hideKeyboard(this)
        }
    }

    private fun showKeyboard(view: View) {
        view.post { inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT) }
    }

    private fun hideKeyboard(view: View) {
        inputManager.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()
    }
}
